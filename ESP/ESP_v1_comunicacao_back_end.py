from time import sleep_ms
from machine import Pin, SPI
from mfrc522 import MFRC522
import urequests
import network

# Configuração do SPI
sck = Pin(19, Pin.OUT)
mosi = Pin(23, Pin.OUT)
miso = Pin(21, Pin.OUT)
spi = SPI(baudrate=100000, polarity=0, phase=0, sck=sck, mosi=mosi, miso=miso)

# Pino SDA
sda = Pin(22, Pin.OUT)

# Configuração da rede Wi-Fi
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

# Função para ler o cartão RFID e fazer a solicitação HTTP
def read_and_request(url):
    try:
        while True:
            # Inicializa o leitor RFID
            rdr = MFRC522(spi, sda)
            uid = ""
            (stat, tag_type) = rdr.request(rdr.REQIDL)
            if stat == rdr.OK:
                (stat, raw_uid) = rdr.anticoll()
                if stat == rdr.OK:
                    # Obtém o UID do cartão RFID
                    uid = ("0x%02x%02x%02x%02x" % (raw_uid[0], raw_uid[1], raw_uid[2], raw_uid[3]))
                    print("UID:", uid)
                    
                    # Faz a solicitação HTTP com o UID e o número do armário
                    request_url = url.format(uid)
                    response = urequests.get(request_url)
                    print("Response:", response.text)
                    response.close()
                    
                    sleep_ms(100)
    except KeyboardInterrupt:
        print("Bye")

# Endereço base da solicitação HTTP
base_url = "http://10.110.12.35:8080/acesso/consultar/1/{}"

# Conecta à rede Wi-Fi
connect_wifi(WIFI_SSID, WIFI_PASSWORD)

# Executa a função de leitura e solicitação continuamente
while True:
    read_and_request(base_url)
