#include <Adafruit_Fingerprint.h>
#include <WiFi.h>
#include <HTTPClient.h>

const uint32_t password = 0x0;
HardwareSerial fingerSerial(1); // Usando a porta serial 1 do ESP32

Adafruit_Fingerprint fingerSensor = Adafruit_Fingerprint(&fingerSerial, password);

const char* ssid = "testesenai";
const char* passwordWifi = "12345678";
const char* verificarEndpointUrl = "http://10.110.12.35:8080/acesso/consultar/biometria/1/";
const char* armazenarEndpointUrl = "http://10.110.12.35:8080/acesso/armazenarBiometria";

void setup() {
    Serial.begin(9600); // Inicia a comunicação serial para mensagens de depuração
    fingerSerial.begin(57600, SERIAL_8N1, 16, 17); // RX = GPIO16, TX = GPIO17 no ESP32
    delay(100);
    
    // Inicia conexão WiFi
    Serial.println("Conectando-se à rede WiFi...");
    WiFi.begin(ssid, passwordWifi);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    Serial.println("Conectado à rede WiFi!");

    // Teste de comunicação serial
    Serial.println("Iniciando teste de comunicação serial...");
    Serial.println("Teste de comunicação serial OK!");
    
    // Teste de conexão com o sensor biométrico
    Serial.println("Iniciando teste de conexão com o sensor biométrico...");
    if (!fingerSensor.verifyPassword()) {
        Serial.println("Erro ao verificar a senha do sensor biométrico!");
        while (true) {}; // Loop Infinito
    }
    Serial.println("Conexão com o sensor biométrico estabelecida com sucesso!");
}

void loop() {
    verificaDigital();
    delay(1000);
}

void verificaDigital() {
    Serial.println("Encoste o dedo no sensor");
    while (fingerSensor.getImage() != FINGERPRINT_OK){};

    if (fingerSensor.image2Tz() != FINGERPRINT_OK){
        Serial.println("Erro image2Tz");
        return;
    }

    Serial.println("Digital capturada. Enviando para verificação...");
    enviarParaVerificar();
}

void enviarParaVerificar() {
    // Convertendo o ID da digital para uma string hexa
    String biometriaHex = String(fingerSensor.fingerID, HEX);

    // Enviar a digital para o endpoint de verificação
    String url = String(verificarEndpointUrl) + biometriaHex;
    Serial.print("URL da requisição (Verificação): ");
    Serial.println(url);
    
    HTTPClient http;
    
    if (!http.begin(url)) {
        Serial.println("Erro ao conectar-se ao servidor (Verificação).");
        return;
    }

    int httpResponseCode = http.GET(); // Enviar uma solicitação GET
    String response = http.getString(); // Obter a resposta do servidor

    Serial.print("Resposta do servidor (Verificação): ");
    Serial.println(response);

    if (httpResponseCode == 200) {
        Serial.println("Acesso Liberado!");
    } else {
        Serial.println("Acesso Negado!");
        // Se o acesso for negado, armazena a digital aproximada
        armazenarDigital();
    }

    http.end();
}

void armazenarDigital() {
    // Convertendo o ID da digital para uma string hexa
    String biometriaHex = String(fingerSensor.fingerID, HEX);

    // Enviar a digital para o endpoint de armazenamento
    String url = armazenarEndpointUrl;
    Serial.print("URL da requisição (Armazenamento): ");
    Serial.println(url);
    
    HTTPClient http;
    
    if (!http.begin(url)) {
        Serial.println("Erro ao conectar-se ao servidor (Armazenamento).");
        return;
    }

    http.addHeader("Content-Type", "application/json");
    int httpResponseCode = http.POST("{\"biometriaHex\":\"" + biometriaHex + "\"}"); // Enviar uma solicitação POST com JSON
    String response = http.getString(); // Obter a resposta do servidor

    Serial.print("Resposta do servidor (Armazenamento): ");
    Serial.println(response);

    if (httpResponseCode == 200) {
        Serial.println("Digital armazenada com sucesso!");
    } else {
        Serial.println("Erro ao armazenar a digital!");
    }

    http.end();
}
