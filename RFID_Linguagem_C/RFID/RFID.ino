#include <SPI.h>
#include <MFRC522.h>
#include <WiFi.h>
#include <HTTPClient.h>

#define SCK_PIN 19
#define MOSI_PIN 23
#define MISO_PIN 21
#define SS_PIN 22
#define LED_RGB_R 25
#define LED_RGB_G 26
#define LED_RGB_B 27
#define BUZZER_PIN 12
#define PIN_SOLENOIDE 32
#define WIFI_SSID "testesenai"
#define WIFI_PASSWORD "12345678"

MFRC522 mfrc522(SS_PIN, -1);

unsigned long lastConnectionTime = 0;
const unsigned long connectionInterval = 10000; // 10 segundos de intervalo
const unsigned long readInterval = 500; // 500 milissegundos de intervalo entre leituras
const unsigned long ledDuration = 2000; // 2 segundos de duração do LED aceso
unsigned long ledStartTime = 0; // Hora de início do tempo do LED

void connectWiFi(const char* ssid, const char* password) {
    Serial.print("Conectando à rede Wi-Fi: ");
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    Serial.println(" Conectado!");
}

void turnOnLED(const char* color) {
    digitalWrite(LED_RGB_R, LOW);
    digitalWrite(LED_RGB_G, LOW);
    digitalWrite(LED_RGB_B, LOW);

    if (strcmp(color, "verde") == 0) {
        digitalWrite(LED_RGB_G, HIGH);
        ledStartTime = millis(); // Registra o tempo de início do LED aceso
    } else if (strcmp(color, "azul") == 0) {
        digitalWrite(LED_RGB_B, HIGH);
    }
}

void turnOffLED() {
    digitalWrite(LED_RGB_R, LOW);
    digitalWrite(LED_RGB_G, LOW);
    digitalWrite(LED_RGB_B, LOW);
}

void activateSolenoide() {
    digitalWrite(PIN_SOLENOIDE, HIGH);
    delay(3000); // Ativa por 3 segundos
    digitalWrite(PIN_SOLENOIDE, LOW);
}

void playShortSound() {
    tone(BUZZER_PIN, 1000); // 1000 Hz
    delay(50); // 50 milissegundos
    noTone(BUZZER_PIN);
}

void playLongSound() {
    tone(BUZZER_PIN, 1000); // 1000 Hz
    delay(1000); // 1 segundo
    noTone(BUZZER_PIN);
}

void sendRFIDtoBackend(const char* rfid) {
    HTTPClient http;
    String store_url = "http://192.168.0.13:8080/acesso/armazenar";
    String payload = "{\"rfid\": \"" + String(rfid) + "\"}";
    
    http.begin(store_url);
    http.addHeader("Content-Type", "application/json");

    int httpResponseCode = http.POST(payload);

    if (httpResponseCode > 0) {
        String response = http.getString();
        Serial.print("Resposta do armazenamento: ");
        Serial.println(response);
        Serial.println("Hexadecimal do cartão armazenado com sucesso.");
    } else {
        Serial.print("Erro ao enviar o RFID para o backend: ");
        Serial.println(httpResponseCode);
    }

    http.end();
}

void readAndStoreRequest(const char* url_read) {
    unsigned long startTime = millis(); // Registra o tempo de início da leitura do cartão
    while (millis() - startTime < 200) {
        // Tente ler o cartão por 200 milissegundos
        if (mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial()) {
            Serial.println("Cartão detectado.");

            String uid = "";
            for (byte i = 0; i < mfrc522.uid.size; i++) {
                uid += String(mfrc522.uid.uidByte[i] < 0x10 ? "0" : "");
                uid += String(mfrc522.uid.uidByte[i], HEX);
            }

            String url = url_read + uid;
            Serial.print("URL da requisição: ");
            Serial.println(url);

            // Realiza a requisição HTTP
            HTTPClient http;
            http.begin(url);
            int httpResponseCode = http.GET();

            // Verifica o resultado da requisição e executa as ações correspondentes
            if (httpResponseCode == 200) {
                Serial.println("Cartão cadastrado.");
                turnOnLED("verde");
                playShortSound();
                activateSolenoide();
                delay(ledDuration); // Mantém o LED verde ligado por 2 segundos
                turnOffLED(); // Desliga o LED verde após 2 segundos

                // Chama a função para armazenar o cartão no backend dentro do if
                sendRFIDtoBackend(uid.c_str());
            } else {
                Serial.println("Cartão não cadastrado.");
                turnOnLED("azul"); // Alterado para "azul"
                playLongSound();
                delay(ledDuration); // Mantém o LED azul ligado por 2 segundos
                turnOffLED(); // Desliga o LED azul após 2 segundos

                // Chama a função para armazenar o cartão no backend dentro do else
                sendRFIDtoBackend(uid.c_str());
            }

            http.end();
            break; // Sai do loop assim que o cartão for lido
        }
    }
}

void setup() {
    Serial.begin(9600);
    pinMode(LED_RGB_R, OUTPUT);
    pinMode(LED_RGB_G, OUTPUT);
    pinMode(LED_RGB_B, OUTPUT);
    pinMode(BUZZER_PIN, OUTPUT);
    pinMode(PIN_SOLENOIDE, OUTPUT);
    SPI.begin(SCK_PIN, MISO_PIN, MOSI_PIN, SS_PIN);
    mfrc522.PCD_Init();
    connectWiFi(WIFI_SSID, WIFI_PASSWORD);
}

void loop() {
    unsigned long currentTime = millis();

    // Verifica se é hora de fazer a requisição
    if (currentTime - lastConnectionTime >= connectionInterval) {
        readAndStoreRequest("http://192.168.0.13:8080/acesso/consultar/1/");
        lastConnectionTime = currentTime;
    }

    delay(readInterval); // Aguarde intervalo entre leituras
}
