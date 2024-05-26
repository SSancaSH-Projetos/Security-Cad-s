// Função para exibir os dados dos alunos nos armários correspondentes
function exibirDadosNosArmarios() {
    const alunos = JSON.parse(localStorage.getItem('alunos')) || [];
  
    alunos.forEach(aluno => {
        const infoArmario = document.getElementById('infoArmario' + aluno.numeroArmario);
        if (infoArmario) {
            infoArmario.innerText = `Nome: ${aluno.nome}, Curso: ${aluno.curso}, Matrícula: ${aluno.matricula}`;
        }
    });
  }
  
  // Chama a função quando o conteúdo do DOM estiver completamente carregado
  document.addEventListener('DOMContentLoaded', exibirDadosNosArmarios);
  
  
  
  document.addEventListener('DOMContentLoaded', function () {
    var menu = document.getElementById('menu');
    menu.style.opacity = '1';
    menu.style.transform = 'translateY(0)';
  
    // Obtém os lugares da tabela
    var assentos = document.querySelectorAll('.assento');
  
    // Recupera dados de armários ocupados do localStorage
    var armariosOcupados = JSON.parse(localStorage.getItem('armariosOcupados')) || [];
  
    // Marca os armários ocupados
    assentos.forEach(function (assento) {
        var numeroArmario = assento.getAttribute('data-numero');
        if (armariosOcupados.includes(numeroArmario)) {
            assento.classList.add('ocupado');
        }
  
        assento.addEventListener('click', function () {
            assento.classList.toggle('selecionado');
            var ocupadoIndex = armariosOcupados.indexOf(numeroArmario);
  
            if (assento.classList.contains('selecionado') && ocupadoIndex === -1) {
                armariosOcupados.push(numeroArmario);
            } else if (!assento.classList.contains('selecionado') && ocupadoIndex !== -1) {
                armariosOcupados.splice(ocupadoIndex, 1);
            }
  
            // Salva o estado atualizado dos armários ocupados no localStorage
            localStorage.setItem('armariosOcupados', JSON.stringify(armariosOcupados));
        });
    });
  });
  
  document.addEventListener("DOMContentLoaded", function() {
    var nomeAluno = localStorage.getItem("nomeAluno");
  
    if (nomeAluno) {
        var bemVindoElement = document.getElementById("bemVindo");
        bemVindoElement.textContent = "Bem-vindo, " + nomeAluno + "!";
    }
  });
  
  // Lógica de cadastro de aluno
  document.getElementById('cadastroAlunoForm').addEventListener('submit', function (event) {
    event.preventDefault();
  
    var numeroArmario = document.getElementById('numeroArmario').value;
    var armariosOcupados = JSON.parse(localStorage.getItem('armariosOcupados')) || [];
  
    if (!armariosOcupados.includes(numeroArmario)) {
        // Salva as informações do aluno no localStorage
        var aluno = {
            nome: document.getElementById('nome').value,
            email: document.getElementById('email').value,
            numeroArmario: numeroArmario,
            numeroCartaoRFID: document.getElementById('numeroCartaoRFID').value,
            matricula: document.getElementById('matricula').value,
            curso: document.getElementById('curso').value
        };
  
        localStorage.setItem('aluno_' + numeroArmario, JSON.stringify(aluno));
  
        // Atualiza a lista de armários ocupados
        armariosOcupados.push(numeroArmario);
        localStorage.setItem('armariosOcupados', JSON.stringify(armariosOcupados));
  
        // Atualiza o estado do armário na interface
        document.querySelector(`.assento[data-numero="${numeroArmario}"]`).classList.add('ocupado');
  
        // Mostra mensagem de sucesso
        $('#modalMensagem').modal('show');
    } else {
        // Mostra mensagem de erro se o armário já estiver ocupado
        $('#modalRFIDERRO').modal('show');
    }
  });
  