function preencherTabela() {
  var tabela = document.getElementById('tabelaRelatorio');
  var tbody = tabela.getElementsByTagName('tbody')[0];

  // Fazendo a requisição AJAX
  var xhr = new XMLHttpRequest();
  xhr.onreadystatechange = function () {
    if (xhr.readyState === XMLHttpRequest.DONE) {
      if (xhr.status === 200) {
        // Limpa o conteúdo atual da tabela
        tbody.innerHTML = '';

        // Preenche a tabela com os dados retornados
        var alunos = JSON.parse(xhr.responseText);
        alunos.forEach(function (aluno) {
          var newRow = tbody.insertRow();
          newRow.insertCell().appendChild(document.createTextNode(aluno.nome));
          newRow.insertCell().appendChild(document.createTextNode(aluno.armario.numero));
          newRow.insertCell().appendChild(document.createTextNode(aluno.curso));
          newRow.insertCell().appendChild(document.createTextNode(aluno.matricula));
          newRow.insertCell().appendChild(document.createTextNode(aluno.cartaoRFID.numeroCartao));

          // Cria uma única célula para os botões de editar e excluir
          var actionsCell = newRow.insertCell();

          // Adiciona o botão de editar
          var editButton = document.createElement('button');
          editButton.innerHTML = '<i class="fas fa-edit"></i>';
          editButton.onclick = function() {
            editarInformacoes(aluno.matricula);
          };
          actionsCell.appendChild(editButton);

          // Adiciona o botão de excluir
          var deleteButton = document.createElement('button');
          deleteButton.innerHTML = '<i class="fas fa-trash-alt"></i>';
          deleteButton.onclick = function() {
            excluirInformacoes(aluno.matricula);
          };
          actionsCell.appendChild(deleteButton);
        });
      } else {
        console.error('Erro ao obter dados do servidor: ' + xhr.status);
      }
    }
  };
  xhr.open('GET', 'http://localhost:8080/relatorio/pegarTodos', true);
  xhr.send();
}

// Função para editar informações de um aluno
function editarInformacoes(matricula) {
  var novoNome = prompt('Digite o novo nome do aluno:');

  if (novoNome !== null) {
    // Aqui você pode fazer uma requisição AJAX para atualizar o nome no servidor
    alert('Nome do aluno atualizado com sucesso!');
  }
}

// Função para excluir informações de um aluno
function excluirInformacoes(matricula) {
  var confirmacao = confirm('Tem certeza de que deseja excluir as informações deste aluno?');

  if (confirmacao) {
    // Aqui você pode fazer uma requisição AJAX para excluir o aluno do servidor
    alert('Informações do aluno excluídas com sucesso!');
  }
}


// URL do endpoint que irá chamar a função
const endpointUrl = 'http://localhost:8080/aluno/editarInformacoes/';

// Dados para enviar na requisição
const dados = {
  matricula: '123456' // Substitua '123456' pelo valor desejado
};

// Configuração da requisição
const options = {
  method: 'POST', // Método da requisição
  headers: {
    'Content-Type': 'application/json' // Tipo de conteúdo da requisição
  },
  body: JSON.stringify(dados) // Convertendo os dados para JSON
};

// Fazendo a requisição usando fetch
fetch(endpointUrl, options)
  .then(response => {
    if (!response.ok) {
      throw new Error('Erro ao fazer a requisição.');
    }
    return response.json(); // Retornando os dados da resposta como JSON
  })
  .then(data => {
    console.log('Resposta:', data); // Exibindo a resposta no console
  })
  .catch(error => {
    console.error('Erro:', error); // Exibindo erros no console, caso ocorram
  });


function excluirInformacao(matricula) {

  // Fazendo a requisição para excluir o aluno com a matrícula fornecida
  fetch('http://localhost:8080/aluno/delete/' + matricula, {
    method: 'DELETE'
  })
  .then(response => {
    if (response.ok) {
      console.log('Aluno com matrícula', matricula, 'foi excluído com sucesso.');
      // Recarrega a página
      window.location.reload();
    } else {
      console.error('Erro ao excluir aluno com matrícula', matricula + ':', response.statusText);
    }
  })
  .catch(error => {
    console.error('Erro ao excluir aluno com matrícula', matricula + ':', error);
  });
}





// Função para excluir informações de um aluno
function excluirInformacoes(matricula) {
  var confirmacao = confirm('Tem certeza de que deseja excluir as informações deste aluno?');

  if (confirmacao) {
    var tabela = document.getElementById('tabelaRelatorio');
    var tbody = tabela.getElementsByTagName('tbody')[0];
    var linhas = tbody.getElementsByTagName('tr');

    for (var i = 0; i < linhas.length; i++) {
      var matriculaAluno = linhas[i].getElementsByTagName('td')[3].textContent;

      if (matriculaAluno === matricula) {
        tbody.removeChild(linhas[i]);
        alert('Informações do aluno excluídas com sucesso!');
        break;
      }
    }
  }
}

// Chamada da função para preencher a tabela ao carregar a página
preencherTabela();

