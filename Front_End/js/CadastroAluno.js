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
            // Exibir mensagem de sucesso após receber resposta do backend
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
