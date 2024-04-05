 // Função para fazer a chamada e preencher a tabela assim que a página é recarregada
 (function() {
    function preencherTabela() {
        var tabela = document.getElementById('tabelaRelatorio');
        var tbody = tabela.getElementsByTagName('tbody')[0];
        
        // Fazendo a requisição AJAX
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    // Limpa o conteúdo atual da tabela
                    tbody.innerHTML = '';

                    // Preenche a tabela com os dados retornados
                    var alunos = JSON.parse(xhr.responseText);
                    alunos.forEach(function(aluno) {
                        var newRow = tbody.insertRow();
                        newRow.insertCell().appendChild(document.createTextNode(aluno.nome));
                        newRow.insertCell().appendChild(document.createTextNode(aluno.armario.numero));
                        newRow.insertCell().appendChild(document.createTextNode(aluno.curso));
                        newRow.insertCell().appendChild(document.createTextNode(aluno.matricula));
                        newRow.insertCell().appendChild(document.createTextNode(aluno.cartaoRFID.numeroCartao));
                    });
                } else {
                    console.error('Erro ao obter dados do servidor: ' + xhr.status);
                }
            }
        };
        xhr.open('GET', 'http://localhost:8080/relatorio/pegarTodos', true);
        xhr.send();
    }

    // Chamada da função para preencher a tabela ao carregar a página
    preencherTabela();
})();