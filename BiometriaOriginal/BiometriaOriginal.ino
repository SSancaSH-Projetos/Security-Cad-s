#include <Adafruit_Fingerprint.h>
#include <WiFi.h>
#include <HTTPClient.h>

const uint32_t password = 0x0;
HardwareSerial fingerSerial(1); // Usando a porta serial 1 do ESP32

Adafruit_Fingerprint fingerSensor = Adafruit_Fingerprint(&fingerSerial, password);

const char* ssid = "testesenai";
const char* passwordWifi = "12345678";
const char* pegarPosicaoBiometriaUrl = "http://10.110.12.35:8080/acesso/pegarPosicaoBiometria";
const char* modoCadastroEndpointUrl = "http://10.110.12.35:8080/acesso/modoCadastroIniciado";
const char* encerrarCadastroBiometriaUrl = "http://10.110.12.35:8080/acesso/encerrarCadastroBiometria";

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
    static unsigned long lastModeCheckTime = 0;
    const unsigned long modeCheckInterval = 10000; // Intervalo de verificação do modo cadastro em milissegundos (15 segundos)

    // Verifica se é hora de verificar o modo cadastro
    if (millis() - lastModeCheckTime >= modeCheckInterval) {
        lastModeCheckTime = millis(); // Atualiza o último tempo de verificação

        // Verifica se o modo cadastro está ativado
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

    // Verificar se o modo de cadastro está iniciado
    if (httpResponseCode == 200 && response == "true") {
        Serial.println("Modo de cadastro iniciado. Armazenando digital...");
        storeFingerprint(); // Se o modo de cadastro estiver iniciado, armazena a digital
    } else {
        Serial.println("Modo de cadastro não iniciado. Não é possível armazenar a digital.");
    }

    http.end();
}

void storeFingerprint() {
    // Obtém a posição para armazenar a digital do endpoint
    int location = obterPosicaoBiometria();

    // Verifica se a posição é válida ou não
    if(location < 1 || location > 149)
    {
        // Se chegou aqui a posição obtida é inválida, então abortamos os próximos passos
        Serial.println(F("Posição inválida"));
        return;
    }

    Serial.print("Armazenando digital na posição: ");
    Serial.println(location);

    Serial.println(F("Encoste o dedo no sensor"));

    //Espera até pegar uma imagem válida da digital
    while (fingerSensor.getImage() != FINGERPRINT_OK);
    
    //Converte a imagem para o primeiro padrão
    if (fingerSensor.image2Tz(1) != FINGERPRINT_OK)
    {
        //Se chegou aqui deu erro, então abortamos os próximos passos
        Serial.println(F("Erro image2Tz 1"));
        return;
    }
    
    Serial.println(F("Tire o dedo do sensor"));

    delay(2000);

    //Espera até tirar o dedo
    while (fingerSensor.getImage() != FINGERPRINT_NOFINGER);

    //Antes de guardar precisamos de outra imagem da mesma digital
    Serial.println(F("Encoste o mesmo dedo no sensor"));

    //Espera até pegar uma imagem válida da digital
    while (fingerSensor.getImage() != FINGERPRINT_OK);

    //Converte a imagem para o segundo padrão
    if (fingerSensor.image2Tz(2) != FINGERPRINT_OK)
    {
        //Se chegou aqui deu erro, então abortamos os próximos passos
        Serial.println(F("Erro image2Tz 2"));
        return;
    }

    //Cria um modelo da digital a partir dos dois padrões
    if (fingerSensor.createModel() != FINGERPRINT_OK)
    {
        //Se chegou aqui deu erro, então abortamos os próximos passos
        Serial.println(F("Erro createModel"));
        return;
    }

    //Guarda o modelo da digital no sensor
    if (fingerSensor.storeModel(location) != FINGERPRINT_OK)
    {
        //Se chegou aqui deu erro, então abortamos os próximos passos
        Serial.println(F("Erro storeModel"));
        return;
    }

    //Se chegou aqui significa que todos os passos foram bem sucedidos
    Serial.println(F("Sucesso!!!"));

    // Após o cadastro ser concluído com sucesso, chamamos a função para encerrar o cadastro
    encerrarCadastroBiometria();
}

void encerrarCadastroBiometria() {
    HTTPClient http;
    http.begin(encerrarCadastroBiometriaUrl);
    http.addHeader("Content-Type", "application/json");

    // Enviamos o valor false no corpo da requisição para indicar que o cadastro deve ser encerrado
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
        // Se a solicitação for bem-sucedida, leia a posição da resposta
        String response = http.getString(); 
        position = response.toInt(); // Converta a resposta para inteiro
    } else {
        Serial.println("Erro ao obter a posição da biometria.");
    }

    http.end();
    return position;
}

// Função para verificar a presença de uma impressão digital
void checkFingerprint() {
    // Verifica se uma imagem de digital foi detectada
    if (fingerSensor.getImage() == FINGERPRINT_OK) {
        Serial.println("Digital detectada! Verificando...");
        
        // Converte a imagem em um formato adequado para comparação
        if (fingerSensor.image2Tz() != FINGERPRINT_OK) {
            Serial.println("Erro ao converter a imagem da digital.");
            return;
        }
        
        // Procura por esta digital no banco de dados
        if (fingerSensor.fingerFastSearch() != FINGERPRINT_OK) {
            Serial.println("Digital não encontrada no banco de dados.");
            return;
        }
        
        // Se chegou até aqui, a digital foi encontrada
        Serial.print("Digital encontrada com confiança de ");
        Serial.print(fingerSensor.confidence);
        Serial.print(" na posição ");
        Serial.println(fingerSensor.fingerID);
    }
}
