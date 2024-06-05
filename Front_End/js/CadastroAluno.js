// script.js

document.getElementById('cadastroAlunoForm').addEventListener('submit', function(event) {
    event.preventDefault();
    
    var nome = document.getElementById('nome').value.trim();
    var email = document.getElementById('email').value.trim();
    var numeroArmario = document.getElementById('numeroArmario').value.trim();
    var numeroCartaoRFID = document.getElementById('numeroCartaoRFID').value.trim();
    var matricula = document.getElementById('matricula').value.trim();
    var curso = document.getElementById('curso').value.trim();

    // Verificar se a matrícula já está cadastrada
    if (verificarMatriculaDuplicada(matricula)) {
        exibirMensagemErro("Erro: A matrícula já está cadastrada!");
        return; // Interrompe o cadastro
    }

    // Array para armazenar as informações dos alunos
    let alunos = [];

    if (!/^[A-Za-zÀ-ú\s]+$/.test(nome)) {
        alert('Nome deve conter apenas letras e acentos.');
        return;
    }
    if (!/^[0-9]+$/.test(numeroArmario)) {
        alert('Número do Armário deve conter apenas números.');
        return;
    }
    if (!/^[A-Za-z0-9]+$/.test(numeroCartaoRFID)) {
        alert('Número do Cartão RFID deve conter apenas letras e números.');
        return;
    }
    if (!/^[0-9]+$/.test(matricula)) {
        alert('Matrícula deve conter apenas números.');
        return;
    }
    if (!/^[A-Za-z0-9\s]+$/.test(curso)) {
        alert('Curso deve conter apenas letras e números.');
        return;
    }

    var aluno = {
        nome: nome,
        email: email,
        numeroArmario: numeroArmario,
        numeroCartaoRFID: numeroCartaoRFID,
        matricula: matricula,
        curso: curso
    };

    localStorage.setItem('alunoCadastro', JSON.stringify(aluno));
    $('#modalMensagem').modal('show');
    
    // Limpar o formulário
    document.getElementById('cadastroAlunoForm').reset();
});

function verificarMatriculaDuplicada(matricula) {
    for (let i = 0; i < localStorage.length; i++) {
        let chave = localStorage.key(i);
        let aluno = JSON.parse(localStorage.getItem(chave));
        if (aluno && aluno.matricula === matricula) {
            return true;
        }
    }
    return false;
}

function exibirMensagemErro(mensagem) {
    const mensagemDiv = document.getElementById("mensagem");
    mensagemDiv.innerHTML = `<div class="alert alert-danger">${mensagem}</div>`;
}

document.getElementById("lerRFIDBtn").addEventListener("click", function() {
    fetch("http://localhost:8080/acesso/ultimoRFID")
    .then(response => {
        if (response.ok) {
            return response.json(); 
        }
        throw new Error("Erro ao obter o último RFID."); 
    })
    .then(data => {
        if (data && data.rfid) {
            var rfidValue = data.rfid;
            document.getElementById("numeroCartaoRFID").value = rfidValue;
            if (rfidValue === sessionStorage.getItem('lastRFID')) {
                $('#modalRFIDERRO').modal('show');
            } else {
                $('#modalRFIDSucesso').modal('show');
                sessionStorage.setItem('lastRFID', rfidValue);
            }
        } else {
            throw new Error("Resposta inválida do servidor.");
        }
    })
    .catch(error => {
        console.error("Erro:", error); 
        document.getElementById("responseMessage").innerText = "Erro ao obter o último RFID.";
    });
});


function chamarUltima() {
    fetch("http://localhost:8080/acesso/obterUltimaBiometria")
        .then(response => {
            if (response.ok) {
                return response.text(); 
            }
            throw new Error("Erro ao obter a última biometria.");
        })
        .then(ultimaBiometria => {
            document.getElementById("biometria").value = ultimaBiometria;
            alert("Última biometria aproximada preenchida no input.");
        })
        .catch(error => {
            console.error("Erro:", error);
            alert("Erro ao obter a última biometria.");
        });
}


// Função para iniciar o cadastro de biometria
function iniciaCadastroBiometria() {
    const numeroBiometria = document.getElementById("numeroArmario").value;
    
    if (numeroBiometria.trim() === "") {
        alert("Por favor, preencha as informações anteriores.");
        return;
    }

    fetch("http://localhost:8080/acesso/iniciarCadastroBiometria", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: numeroBiometria 
    })
    .then(response => {
        if (response.ok) {
            return response.text(); 
        }
        throw new Error("Erro ao iniciar cadastro biometria.");
    })
    .then(numeroPosicao => {
        document.getElementById("numeroArmario").value = numeroPosicao;
        alert("Cadastro da biometria iniciado.");

        // Abre o modal
        $('#biometriaModal').modal('show');

        // Espera 4 segundos antes de começar a verificar a mensagem atualizada
        setTimeout(function() {
            verificarMensagemAtual();
            // Começa a verificar a mensagem atualizada periodicamente
            setInterval(verificarMensagemAtual, 1000); // Verifica a cada segundo
        }, 4000);
    })
    .catch(error => {
        console.error("Erro:", error);
        alert("Erro ao completar cadastro.");
    });
}


// Função para verificar a mensagem atualizada do backend
function verificarMensagemAtual() {
    fetch("http://localhost:8080/acesso/mensagemAtual")
    .then(response => {
        if (response.ok) {
            return response.json(); // Convertendo a resposta para JSON
        }
        throw new Error("Erro ao obter mensagem atual.");
    })
    .then(data => {
        // Verifica se a mensagem não está vazia
        if (data && data.mensagem.trim() !== "") {
            // Atualiza o conteúdo do modal com a nova mensagem
            atualizarConteudoModal(data.mensagem);
        }
    })
    .catch(error => {
        console.error("Erro:", error);
    });
}

// Função para atualizar o conteúdo do modal com a nova mensagem
function atualizarConteudoModal(mensagem) {
    const conteudoModal = document.getElementById('modalTexto');
    conteudoModal.innerText = mensagem;
}





document.getElementById("cadastroAlunoForm").addEventListener("submit", function (event) {
    event.preventDefault();

    const url = "http://localhost:8080/aluno/cadastrarInformacoesAluno";

    const dadosFormulario = {
        nome: document.getElementById("nome").value,
        email: document.getElementById("email").value,
        numeroArmario: document.getElementById("numeroArmario").value,
        numeroCartaoRFID: document.getElementById("numeroCartaoRFID").value,
        matricula: document.getElementById("matricula").value,
        curso: document.getElementById("curso").value,
        numeroDaBiometria: document.getElementById("numeroArmario").value
    };

    // Enviar a solicitação ao backend e aguardar pela resposta do cadastro
    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(dadosFormulario),
    })
        .then((response) => {
            console.log(response);
            if (response.ok == false) {
                throw new Error(`Erro ao cadastrar aluno: ${response.status} - ${response.statusText}`);
            }
        })
        .then((data) => {
            console.log(data);
            // Limpar campos após o cadastro bem-sucedido
            document.getElementById("nome").value = "";
            document.getElementById("email").value = "";
            document.getElementById("numeroArmario").value = "";
            document.getElementById("numeroCartaoRFID").value = "";
            document.getElementById("matricula").value = "";
            document.getElementById("curso").value = "";
            $('#modalMensagem').modal('show');
        });
});

/*Validação da entrada de dados digitada pelos usúarios*/ 
document.getElementById('cadastroAlunoForm').addEventListener('submit', function (event) {
    // Validação dos campos
    var nome = document.getElementById('nome').value.trim();
    var email = document.getElementById('email').value.trim();
    var numeroArmario = document.getElementById('numeroArmario').value.trim();
    var numeroCartaoRFID = document.getElementById('numeroCartaoRFID').value.trim();
    var matricula = document.getElementById('matricula').value.trim();
    var curso = document.getElementById('curso').value.trim();

    if (!/^[A-Za-zÀ-ú\s]+$/.test(nome)) {
        exibirErro('Nome deve conter apenas letras e acentos.');
        event.preventDefault();
        return;
    }

    if (!/^[0-9]+$/.test(numeroArmario)) {
        exibirErro('Número do Armário deve conter apenas números.');
        event.preventDefault();
        return;
    }

    if (!/^[A-Za-z0-9]+$/.test(numeroCartaoRFID)) {
        exibirErro('Número do Cartão RFID deve conter apenas letras e números.');
        event.preventDefault();
        return;
    }

    if (!/^[0-9]+$/.test(matricula)) {
        exibirErro('Matrícula deve conter apenas números.');
        event.preventDefault();
        return;
    }

    if (!/^[A-Za-z0-9\s]+$/.test(curso)) {
        exibirErro('Curso deve conter apenas letras e números.');
        event.preventDefault();
        return;
    }
});
