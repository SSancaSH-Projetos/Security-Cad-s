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

    // Função para aplicar o filtro por nome do curso
    function aplicarFiltroNomeCurso() {
        var input, filter, tabela, tbody, tr, td, i, txtValue;
        input = document.getElementById("filtroNomeCurso");
        filter = input.value.toUpperCase();
        tabela = document.getElementById("tabelaRelatorio");
        tbody = tabela.getElementsByTagName("tbody")[0];
        tr = tbody.getElementsByTagName("tr");
        for (i = 0; i < tr.length; i++) {
            td = tr[i].getElementsByTagName("td")[2]; // 3ª coluna (0-indexed) contém o nome do curso
            if (td) {
                txtValue = td.textContent || td.innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    tr[i].style.display = "";
                } else {
                    tr[i].style.display = "none";
                }
            }
        }
    }

    // Adicionando um listener de evento ao input para chamar a função de filtragem
    document.getElementById("filtroNomeCurso").addEventListener("keyup", aplicarFiltroNomeCurso);

})();
