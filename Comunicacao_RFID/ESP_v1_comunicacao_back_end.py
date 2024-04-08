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

# Função para ler o cartão RFID, armazenar o UID e fazer outra solicitação HTTP
def read_and_store_request(url_read, url_store):
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
                    request_url = url_read.format(uid)
                    print(request_url)
                    response = urequests.get(request_url)
                    print("Response:", response.text)
                    response.close()

                    # Chama a função para enviar o UID para o backend
                    send_rfid_to_backend(uid)
                    
                    sleep_ms(100)
    except KeyboardInterrupt:
        print("Bye")

# Função para enviar o UID para o backend
def send_rfid_to_backend(rfid):
    try:
        # URL do endpoint para armazenar o UID no backend
        store_url = "http://10.187.244.161:8080/acesso/armazenar"
        
        # Formata o payload da requisição
        payload = {"rfid": rfid}
        
        # Faz a solicitação HTTP POST para enviar o UID para o backend
        response = urequests.post(store_url, json=payload)
        print("Store Response:", response.text)
        response.close()
    except Exception as e:
        print("Error sending RFID to backend:", e)

# Endereço base para a solicitação de leitura do cartão RFID
read_base_url = r"http://10.187.244.161:8080/acesso/consultar/1/{}"

# Conecta à rede Wi-Fi
connect_wifi(WIFI_SSID, WIFI_PASSWORD)

# Executa a função de leitura e solicitação continuamente
while True:
    read_and_store_request(read_base_url, "")
