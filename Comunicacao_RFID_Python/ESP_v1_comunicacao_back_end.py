from time import ticks_ms
from machine import Pin, SPI, Timer, PWM
from mfrc522 import MFRC522
import urequests
import network
import time

sck = Pin(19, Pin.OUT)
mosi = Pin(23, Pin.OUT)
miso = Pin(21, Pin.OUT)
spi = SPI(baudrate=100000, polarity=0, phase=0, sck=sck, mosi=mosi, miso=miso)

sda = Pin(22, Pin.OUT)

led_rgb_r = Pin(25, Pin.OUT)
led_rgb_g = Pin(26, Pin.OUT)
led_rgb_b = Pin(27, Pin.OUT)

buzzer_pin = 12  
buzzer_pwm = PWM(Pin(buzzer_pin))
frequencia_padrao = 1000  


pin_solenoide = Pin(32, Pin.OUT)  

solenoide_ativada = False

solenoide_timer = Timer(3000)

WIFI_SSID = "testesenai"
WIFI_PASSWORD = "12345678"

def connect_wifi(ssid, password):
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    if not wlan.isconnected():
        print('Conectando à rede Wi-Fi...')
        wlan.connect(ssid, password)
        while not wlan.isconnected():
            pass
    print('Conectado à rede Wi-Fi:', ssid)

def turn_on_led(color):
    led_rgb_r.value(0)
    led_rgb_g.value(0)
    led_rgb_b.value(0)
    
    if color == "verde":
        led_rgb_g.value(1)  
    elif color == "vermelho":
        led_rgb_r.value(1) 

def activate_solenoide():
    global solenoide_ativada
    solenoide_ativada = True
    pin_solenoide.value(1)  
    solenoide_timer.init(period=3000, mode=Timer.ONE_SHOT, callback=lambda t: deactivate_solenoide())

def deactivate_solenoide():
    global solenoide_ativada
    solenoide_ativada = False
    pin_solenoide.value(0)  

def tocar_som_curto():
    buzzer_pwm.freq(frequencia_padrao)
    buzzer_pwm.duty(1000)  
    time.sleep(0.05)  
    buzzer_pwm.duty(0) 

def tocar_som_longo():
    buzzer_pwm.freq(frequencia_padrao)
    buzzer_pwm.duty(512)  
    time.sleep(1)  
    buzzer_pwm.duty(0) 

def read_and_store_request(url_read, url_store):
    try:
        last_read_time = ticks_ms()
        while True:
            rdr = MFRC522(spi, sda)
            uid = ""
            (stat, tag_type) = rdr.request(rdr.REQIDL)
            if stat == rdr.OK:
                (stat, raw_uid) = rdr.anticoll()
                if stat == rdr.OK:
                    uid = ("0x%02x%02x%02x%02x" % (raw_uid[0], raw_uid[1], raw_uid[2], raw_uid[3]))
                    print("UID:", uid)
                    
                    response = urequests.get(url_read.format(uid))
                    if response.text == "ok":
                        turn_on_led("verde")
                        tocar_som_curto()  
                        activate_solenoide() 
                    else:
                        turn_on_led("vermelho")
                        tocar_som_longo()  
                        if solenoide_ativada:
                            deactivate_solenoide()  
                    response.close()
                    
                    send_rfid_to_backend(uid)
                    
                    last_read_time = ticks_ms()
                    
            if ticks_ms() - last_read_time >= 2000:
            
                turn_on_led("none")

    except KeyboardInterrupt:
        print("Bye")
    except Exception as e:
        print("Error:", e)
        turn_on_led("vermelho")

def send_rfid_to_backend(rfid):
    try:
        store_url = "http://10.187.244.130:8080/acesso/armazenar"
        payload = {"rfid": rfid}
        response = urequests.post(store_url, json=payload)
        print("Store Response:", response.text)
        response.close()
    except Exception as e:
        print("Error sending RFID to backend:", e)
        raise e

read_base_url = "http://10.187.244.130:8080/acesso/consultar/1/{}"

connect_wifi(WIFI_SSID, WIFI_PASSWORD)

while True:
    read_and_store_request(read_base_url, "")

