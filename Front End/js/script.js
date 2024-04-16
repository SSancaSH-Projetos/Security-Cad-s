// Função para armazenar os dados do aluno no armazenamento local do navegador
function armazenarDadosAluno(nome, email, numeroArmario, numeroRFID, matricula, curso) {
    const aluno = {
        nome: nome,
        email: email,
        numeroArmario: numeroArmario,
        numeroRFID: numeroRFID,
        matricula: matricula,
        curso: curso
    };
    localStorage.setItem('aluno', JSON.stringify(aluno));
}

// Função para exibir os dados do aluno na Página Inicial
function exibirDadosAluno() {
    const aluno = JSON.parse(localStorage.getItem('aluno'));
    if (aluno) {
        // Seleciona todos os elementos com a classe 'assento'
        const assentos = document.querySelectorAll('.assento');
        
       // Itera sobre os elementos 'assento' para atualizar o conteúdo com as informações do aluno correspondente
       assentos.forEach(function(assento) {
        // Verifica se o número do armário do assento corresponde ao número do armário do aluno
        if (assento.dataset.numero === aluno.numeroArmario) {
            // Seleciona o tooltip dentro do assento e atualiza o conteúdo com as informações do aluno
            const tooltip = assento.querySelector('.tooltip');
            tooltip.textContent = `Nome: ${aluno.nome}, Curso: ${aluno.curso}, Matrícula: ${aluno.matricula}`;
        }
    });
    }
}

        
        // Seleciona todos os elementos com a classe 'assento' dentro da div 'plano-cartesiano'
        const assentos = planoCartesiano.querySelectorAll('.assento');
        
          // Cria um novo elemento div para o aluno
          const alunoDiv = document.createElement('div');
          alunoDiv.classList.add('assento');
          alunoDiv.dataset.numero = aluno.numeroArmario;
          
          // Define o conteúdo do tooltip
          const tooltip = document.createElement('div');
          tooltip.classList.add('tooltip');
          tooltip.textContent = `Nome: ${aluno.nome}, Curso: ${aluno.curso}, Matrícula: ${aluno.matricula}`;
          
          // Adiciona o tooltip à div do aluno
          alunoDiv.appendChild(tooltip);
          
          // Adiciona o aluno à div do plano cartesiano
          planoCartesiano.appendChild(alunoDiv);