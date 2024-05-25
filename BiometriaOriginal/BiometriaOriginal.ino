#include <Adafruit_Fingerprint.h>
#include <WiFi.h>
#include <HTTPClient.h>

#define LED_RGB_R 25
#define LED_RGB_G 26
#define LED_RGB_B 27
#define BUZZER_PIN 12
#define PIN_SOLENOIDE 32

const uint32_t password = 0x0;
HardwareSerial fingerSerial(1); // Usando a porta serial 1 do ESP32

Adafruit_Fingerprint fingerSensor = Adafruit_Fingerprint(&fingerSerial, password);

const char* ssid = "testesenai";
const char* passwordWifi = "12345678";
const char* pegarPosicaoBiometriaUrl = "http://192.168.0.13:8080/acesso/pegarPosicaoBiometria";
const char* modoCadastroEndpointUrl = "http://192.168.0.13:8080/acesso/modoCadastroIniciado";
const char* encerrarCadastroBiometriaUrl = "http://192.168.0.13:8080/acesso/encerrarCadastroBiometria";

unsigned long lastModeCheckTime = 0;
const unsigned long modeCheckInterval = 10000; // Intervalo de verificação do modo cadastro em milissegundos (10 segundos)
const unsigned long ledDuration = 3000; // 3 segundos de duração do LED aceso

void setup() {
    Serial.begin(9600); // Inicia a comunicação serial para mensagens de depuração
    fingerSerial.begin(57600, SERIAL_8N1, 16, 17); // RX = GPIO16, TX = GPIO17 no ESP32
    delay(100);
    
    pinMode(LED_RGB_R, OUTPUT);
    pinMode(LED_RGB_G, OUTPUT);
    pinMode(LED_RGB_B, OUTPUT);
    pinMode(BUZZER_PIN, OUTPUT);
    pinMode(PIN_SOLENOIDE, OUTPUT);

    // Inicia conexão WiFi
    Serial.println("Conectando-se à rede WiFi...");
    WiFi.begin(ssid, passwordWifi);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    Serial.println("Conectado à rede WiFi!");

    // Teste de conexão com o sensor biométrico
    Serial.println("Iniciando teste de conexão com o sensor biométrico...");
    if (!fingerSensor.verifyPassword()) {
        Serial.println("Erro ao verificar a senha do sensor biométrico!");
        while (true) {}; // Loop Infinito
    }
    Serial.println("Conexão com o sensor biométrico estabelecida com sucesso!");
}

void loop() {
    // Verifica se é hora de verificar o modo cadastro
    if (millis() - lastModeCheckTime >= modeCheckInterval) {
        lastModeCheckTime = millis(); // Atualiza o último tempo de verificação
        verificarModoCadastro();
    }

    // Verifica se uma impressão digital está presente
    checkFingerprint();
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
        storeFingerprint(); // Se o modo de cadastro estiver iniciado, armazena a digital
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
        return -1; // Retorna -1 para indicar erro
    }

    int httpResponseCode = http.GET(); 
    int position = -1; // Valor padrão caso algo dê errado

    if (httpResponseCode == 200) {
        String response = http.getString(); 
        position = response.toInt(); // Converta a resposta para inteiro
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
        
        turnOnLED("verde");
        playShortSound();
        activateSolenoide();
        delay(ledDuration);
        turnOffLED();
    }
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
    delay(5000); // Ativa por 5 segundos
    digitalWrite(PIN_SOLENOIDE, LOW);
}

void playShortSound() {
    tone(BUZZER_PIN, 1000); // 1000 Hz
    delay(100); 
    noTone(BUZZER_PIN);
}

void playLongSound() {
    tone(BUZZER_PIN, 1000); // 1000 Hz
    delay(2000); // 2 segundos
    noTone(BUZZER_PIN);
}
