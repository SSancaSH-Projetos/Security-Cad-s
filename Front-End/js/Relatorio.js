function aplicarFiltroNomeCurso() {
  var input = document.getElementById("filtroNomeCurso");
  var filter = input.value.toUpperCase();
  var table = document.getElementById("tabelaRelatorio");
  var tbody = table.getElementsByTagName("tbody")[0];
  var tr = tbody.getElementsByTagName("tr");
  for (var i = 0; i < tr.length; i++) {
    var td = tr[i].getElementsByTagName("td")[2]; // 3ª coluna (0-indexed) contém o nome do curso
    if (td) {
      var txtValue = td.textContent || td.innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }
  }
}

function aplicarFiltroNome() {
  var filtroNome = document.getElementById('filtroNome').value.toLowerCase();
  var tabela = document.getElementById('tabelaRelatorio');
  var linhas = tabela.getElementsByTagName('tbody')[0].getElementsByTagName('tr');
  for (var i = 0; i < linhas.length; i++) {
    var linha = linhas[i];
    var colunaNome = linha.getElementsByTagName('td')[0]; // Considerando que a coluna do nome do aluno é a primeira
    if (filtroNome === "" || colunaNome.textContent.toLowerCase().includes(filtroNome)) {
      linha.style.display = "";
    } else {
      linha.style.display = "none";
    }
  }
}

function aplicarFiltroNumMatricula() {
  var inputNumMatricula = document.getElementById('filtroNumMatricula').value.toUpperCase();
  var tabela = document.getElementById('tabelaRelatorio');
  var linhas = tabela.getElementsByTagName('tr');
  for (var i = 0; i < linhas.length; i++) {
    var colunaNumMatricula = linhas[i].getElementsByTagName('td')[3]; // Índice da coluna do número de matrícula
    if (colunaNumMatricula) {
      var textoNumMatricula = colunaNumMatricula.textContent || colunaNumMatricula.innerText;
      if (textoNumMatricula.toUpperCase().indexOf(inputNumMatricula) > -1) {
        linhas[i].style.display = '';
      } else {
        linhas[i].style.display = 'none';
      }
    }
  }
}


document.addEventListener('DOMContentLoaded', () => {
    preencherTabela();
  
    // Evento de clique para fechar o modal
    document.querySelector('.close').onclick = closeModal;
  });

async function preencherTabela() {
const tabela = document.getElementById('tabelaRelatorio');
const tbody = tabela.getElementsByTagName('tbody')[0];
  
try {
      const response = await fetch('http://localhost:8080/relatorio/pegarTodos');
      if (response.ok) {
        const alunos = await response.json();
  
        // Limpa o conteúdo atual da tabela
        tbody.innerHTML = '';
  
        // Preenche a tabela com os dados retornados
        alunos.forEach(aluno => criarLinhaTabela(aluno, tbody));
      } else {
        console.error('Erro ao obter dados do servidor:', response.status);
      }
    } catch (error) {
      console.error('Erro ao obter dados do servidor:', error);
    }
}
  
function criarLinhaTabela(aluno, tbody) {
    const newRow = tbody.insertRow();
    newRow.insertCell().appendChild(document.createTextNode(aluno.nome));
    newRow.insertCell().appendChild(document.createTextNode(aluno.armario.numero));
    newRow.insertCell().appendChild(document.createTextNode(aluno.curso));
    newRow.insertCell().appendChild(document.createTextNode(aluno.matricula));
    newRow.insertCell().appendChild(document.createTextNode(aluno.cartaoRFID.numeroCartao));
    newRow.insertCell().appendChild(document.createTextNode(aluno.biometria.numeroBiometria));
  
    const actionsCell = newRow.insertCell();
  
    // Adiciona o botão de editar
    const editButton = criarBotao('edit', () => editarInformacoes(aluno));
    editButton.classList.add('table-button'); // Adiciona a classe do estilo
    actionsCell.appendChild(editButton);
  
    // Adiciona o botão de excluir
    const deleteButton = criarBotao('trash-alt', () => excluirInformacoes(aluno.matricula));
    deleteButton.classList.add('table-button'); // Adiciona a classe do estilo
    actionsCell.appendChild(deleteButton);
}

function criarBotao(iconClass, onClick) {
    const button = document.createElement('button');
    button.innerHTML = `<i class="fas fa-${iconClass}"></i>`;
    button.onclick = onClick;
    return button;
}

async function excluirInformacoes(matricula) {
    const confirmacao = confirm('Tem certeza de que deseja excluir as informações deste aluno?');
    if (confirmacao) {
      try {
        const response = await fetch(`http://localhost:8080/aluno/delete/${matricula}`, {
          method: 'DELETE'
        });
  
        if (response.ok) {
          console.log(`Aluno com matrícula ${matricula} foi excluído com sucesso.`);
          preencherTabela();
        } else {
          console.error(`Erro ao excluir aluno com matrícula ${matricula}:`, response.statusText);
        }
      } catch (error) {
        console.error(`Erro ao excluir aluno com matrícula ${matricula}:`, error);
      }
    }
}

function editarInformacoes(aluno) {
    document.getElementById('idAluno').value = aluno.idAluno;
    document.getElementById('editNome').value = aluno.nome;
    document.getElementById('editArmario').value = aluno.armario.numero;
    document.getElementById('editCurso').value = aluno.curso;
    document.getElementById('editMatricula').value = aluno.matricula;
    document.getElementById('editRFID').value = aluno.cartaoRFID.numeroCartao;
    document.getElementById('editBiometria').value = aluno.biometria.numeroBiometria;
    openModal();
  
    document.getElementById('saveButton').onclick = () => salvarAlteracoes(aluno.matricula);
}

async function salvarAlteracoes(matriculaAntiga) {
    const idAluno = document.getElementById('idAluno').value;
    const novoNome = document.getElementById('editNome').value;
    const novoArmario = document.getElementById('editArmario').value;
    const novoCurso = document.getElementById('editCurso').value;
    const novaMatricula = document.getElementById('editMatricula').value;
    const novoCartaoRFID = document.getElementById('editRFID').value;
    const novaBiometria = document.getElementById('editBiometria').value;

    const dadosAtualizados = {
      nome: novoNome,
      numeroArmario: novoArmario ,
      curso: novoCurso,
      matricula: novaMatricula,
      numeroCartaoRFID: novoCartaoRFID,
      numeroDaBiometria: novaBiometria
    };
  
    console.log(dadosAtualizados)
    console.log(idAluno)
  
    try {
      const response = await fetch(`http://localhost:8080/aluno/editarAluno/${idAluno}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dadosAtualizados)
      });
  
      if (response.ok) {
        console.log('Informações do aluno atualizadas com sucesso.');
        preencherTabela();
        alert('As informações foram salvas com sucesso!');
      } else {
        console.error('Erro ao atualizar informações do aluno:', response.statusText);
      }
    } catch (error) {
      console.error('Erro ao atualizar informações do aluno:', error);
    }
  
    closeModal();
}
  
function openModal() {
    document.getElementById('editModal').style.display = 'block';
}
  
function closeModal() {
    document.getElementById('editModal').style.display = 'none';
}