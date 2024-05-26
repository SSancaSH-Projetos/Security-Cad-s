#include <SPI.h>
#include <MFRC522.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <Adafruit_Fingerprint.h>

// Definições de pinos
#define SCK_PIN 19
#define MOSI_PIN 23
#define MISO_PIN 21
#define SS_PIN 22
#define LED_RGB_R 25
#define LED_RGB_G 26
#define LED_RGB_B 27
#define BUZZER_PIN 12
#define PIN_SOLENOIDE 32
#define FINGERPRINT_RX_PIN 16
#define FINGERPRINT_TX_PIN 17

// Configurações Wi-Fi
#define WIFI_SSID "testesenai"
#define WIFI_PASSWORD "12345678"

// URLs do servidor
const char* pegarPosicaoBiometriaUrl = "http://192.168.0.13:8080/acesso/pegarPosicaoBiometria";
const char* modoCadastroEndpointUrl = "http://192.168.0.13:8080/acesso/modoCadastroIniciado";
const char* encerrarCadastroBiometriaUrl = "http://192.168.0.13:8080/acesso/encerrarCadastroBiometria";
const char* verificarBiometriaUrl = "http://192.168.0.13:8080/acesso/biometria";

// Constantes e variáveis
MFRC522 mfrc522(SS_PIN, -1);
HardwareSerial fingerSerial(1);
Adafruit_Fingerprint fingerSensor = Adafruit_Fingerprint(&fingerSerial);

unsigned long lastModeCheckTime = 0;
const unsigned long modeCheckInterval = 10000; // Intervalo de verificação do modo cadastro em milissegundos (10 segundos)
const unsigned long connectionInterval = 5000; // Intervalo de 5 segundos entre verificações RFID
const unsigned long ledDuration = 2000; // 2 segundos de duração do LED aceso
unsigned long lastConnectionTime = 0;

void setup() {
    Serial.begin(9600);
    fingerSerial.begin(57600, SERIAL_8N1, FINGERPRINT_RX_PIN, FINGERPRINT_TX_PIN);
    SPI.begin(SCK_PIN, MISO_PIN, MOSI_PIN, SS_PIN);
    mfrc522.PCD_Init();

    pinMode(LED_RGB_R, OUTPUT);
    pinMode(LED_RGB_G, OUTPUT);
    pinMode(LED_RGB_B, OUTPUT);
    pinMode(BUZZER_PIN, OUTPUT);
    pinMode(PIN_SOLENOIDE, OUTPUT);

    connectWiFi(WIFI_SSID, WIFI_PASSWORD);

    if (!fingerSensor.verifyPassword()) {
        Serial.println("Erro ao verificar a senha do sensor biométrico!");
        while (true) {}; // Loop Infinito
    }
    Serial.println("Conexão com o sensor biométrico estabelecida com sucesso!");
}

void loop() {
    unsigned long currentTime = millis();

    // Verificação do modo cadastro
    if (currentTime - lastModeCheckTime >= modeCheckInterval) {
        lastModeCheckTime = currentTime;
        verificarModoCadastro();
    }

    // Verificação de impressão digital
    checkFingerprint();

    // Verificação de RFID
    if (currentTime - lastConnectionTime >= connectionInterval) {
        lastConnectionTime = currentTime;
        readAndStoreRequest("http://192.168.0.13:8080/acesso/consultar/1/");
    }
}

// Funções auxiliares
void connectWiFi(const char* ssid, const char* password) {
    Serial.print("Conectando à rede Wi-Fi: ");
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    Serial.println(" Conectado!");
}

void verificarModoCadastro() {
    HTTPClient http;
    if (!http.begin(modoCadastroEndpointUrl)) {
        Serial.println("Erro ao conectar-se ao servidor (Modo Cadastro).");
        return;
    }

    int httpResponseCode = http.GET(); 
    String response = http.getString(); 

    Serial.print("Resposta do servidor (Modo Cadastro): ");
    Serial.println(response);

    if (httpResponseCode == 200 && response == "true") {
        Serial.println("Modo de cadastro iniciado. Armazenando digital...");
        storeFingerprint(); 
    } else {
        Serial.println("Modo de cadastro não iniciado. Não é possível armazenar a digital.");
    }

    http.end();
}

void storeFingerprint() {
    int location = obterPosicaoBiometria();

    if(location < 1 || location > 149) {
        Serial.println(F("Posição inválida"));
        return;
    }

    Serial.print("Armazenando digital na posição: ");
    Serial.println(location);

    Serial.println(F("Encoste o dedo no sensor"));

    while (fingerSensor.getImage() != FINGERPRINT_OK);
    
    if (fingerSensor.image2Tz(1) != FINGERPRINT_OK) {
        Serial.println(F("Erro image2Tz 1"));
        return;
    }
    
    Serial.println(F("Tire o dedo do sensor"));
    delay(2000);

    while (fingerSensor.getImage() != FINGERPRINT_NOFINGER);

    Serial.println(F("Encoste o mesmo dedo no sensor"));

    while (fingerSensor.getImage() != FINGERPRINT_OK);

    if (fingerSensor.image2Tz(2) != FINGERPRINT_OK) {
        Serial.println(F("Erro image2Tz 2"));
        return;
    }

    if (fingerSensor.createModel() != FINGERPRINT_OK) {
        Serial.println(F("Erro createModel"));
        return;
    }

    if (fingerSensor.storeModel(location) != FINGERPRINT_OK) {
        Serial.println(F("Erro storeModel"));
        return;
    }

    Serial.println(F("Sucesso!!!"));
    encerrarCadastroBiometria();
}

void encerrarCadastroBiometria() {
    HTTPClient http;
    http.begin(encerrarCadastroBiometriaUrl);
    http.addHeader("Content-Type", "application/json");

    int httpResponseCode = http.POST("false");
    if (httpResponseCode == 200) {
        Serial.println("Cadastro de biometria encerrado com sucesso!");
    } else {
        Serial.println("Erro ao encerrar o cadastro de biometria.");
    }

    http.end();
}

int obterPosicaoBiometria() {
    HTTPClient http;

    if (!http.begin(pegarPosicaoBiometriaUrl)) {
        Serial.println("Erro ao conectar-se ao servidor (Obter Posição Biometria).");
        return -1;
    }

    int httpResponseCode = http.GET(); 
    int position = -1; 

    if (httpResponseCode == 200) {
        String response = http.getString(); 
        position = response.toInt();
    } else {
        Serial.println("Erro ao obter a posição da biometria.");
    }

    http.end();
    return position;
}

void checkFingerprint() {
    if (fingerSensor.getImage() == FINGERPRINT_OK) {
        Serial.println("Digital detectada! Verificando...");
        
        if (fingerSensor.image2Tz() != FINGERPRINT_OK) {
            Serial.println("Erro ao converter a imagem da digital.");
            return;
        }
        
        if (fingerSensor.fingerFastSearch() != FINGERPRINT_OK) {
            Serial.println("Digital não encontrada no banco de dados.");
            turnOnLED("vermelho");
            playLongSound();
            delay(ledDuration);
            turnOffLED();
            return;
        }

        Serial.print("Digital encontrada com confiança de ");
        Serial.print(fingerSensor.confidence);
        Serial.print(" na posição ");
        Serial.println(fingerSensor.fingerID);

        // Converte o fingerID para String e usa para ambas as variáveis
        String fingerIDStr = String(fingerSensor.fingerID);

        // Verificar no banco de dados
        verificarBiometriaNoBanco(fingerIDStr, fingerIDStr);
    }
}

void verificarBiometriaNoBanco(String numeroArmario, String fingerID) {
    if (WiFi.status() == WL_CONNECTED) {
        HTTPClient http;
        String url = String(verificarBiometriaUrl) + "/" + numeroArmario + "/" + fingerID;

        Serial.print("URL da requisição: ");
        Serial.println(url);

        http.begin(url);
        int httpCode = http.GET();

        if (httpCode == HTTP_CODE_OK) {
            String payload = http.getString();
            if (payload == "ok") {
                Serial.println("Digital confirmada no banco de dados. Acesso liberado!");
                turnOnLED("verde");
                playShortSound();
                activateSolenoide();
            } else {
                Serial.println("Digital não confirmada no banco de dados. Acesso negado!");
                turnOnLED("vermelho");
                playLongSound();
            }
        } else {
            Serial.println("Erro ao verificar digital no banco de dados. Código de resposta HTTP: " + String(httpCode));
            turnOnLED("vermelho");
            playLongSound();
        }

        http.end();
    } else {
        Serial.println("Não conectado ao Wi-Fi.");
        turnOnLED("vermelho");
        playLongSound();
    }

    delay(ledDuration);
    turnOffLED();
}


void readAndStoreRequest(const char* url_read) {
    unsigned long startTime = millis(); 
    while (millis() - startTime < 3000) {
        if (mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial()) {
            Serial.println("Cartão detectado.");

            String uid = "";
            for (byte i = 0; i < mfrc522.uid.size; i++) {
                uid += String(mfrc522.uid.uidByte[i] < 0x10 ? "0" : "");
                uid += String(mfrc522.uid.uidByte[i], HEX);
            }

            String url = String(url_read) + uid;
            Serial.print("URL da requisição: ");
            Serial.println(url);

            HTTPClient http;
            http.begin(url);
            int httpResponseCode = http.GET();

            sendRFIDtoBackend(uid.c_str());

            if (httpResponseCode == 200) {
                Serial.println("Cartão cadastrado. Liberado o Acesso!");
                turnOnLED("verde");
                playShortSound();
                activateSolenoide();
                delay(ledDuration);
                turnOffLED();
            } else {
                Serial.println("Cartão não cadastrado.");
                turnOnLED("vermelho");
                playLongSound();
                delay(ledDuration);
                turnOffLED();
            }

            http.end();
            break;
        }
    }
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

void turnOnLED(const char* color) {
    digitalWrite(LED_RGB_R, LOW);
    digitalWrite(LED_RGB_G, LOW);
    digitalWrite(LED_RGB_B, LOW);

    if (strcmp(color, "verde") == 0) {
        digitalWrite(LED_RGB_G, HIGH);
    } else if (strcmp(color, "vermelho") == 0) {
        digitalWrite(LED_RGB_R, HIGH);
    }
}

void turnOffLED() {
    digitalWrite(LED_RGB_R, LOW);
    digitalWrite(LED_RGB_G, LOW);
    digitalWrite(LED_RGB_B, LOW);
}

void activateSolenoide() {
    digitalWrite(PIN_SOLENOIDE, HIGH);
    delay(3000);
    digitalWrite(PIN_SOLENOIDE, LOW);
}

void playShortSound() {
    tone(BUZZER_PIN, 1000);
    delay(100);
    noTone(BUZZER_PIN);
}

void playLongSound() {
    tone(BUZZER_PIN, 1000);
    delay(2000);
    noTone(BUZZER_PIN);
}

