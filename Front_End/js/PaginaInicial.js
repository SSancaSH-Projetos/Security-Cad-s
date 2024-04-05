document.addEventListener('DOMContentLoaded', function () {
    var menu = document.getElementById('menu');
    menu.style.opacity = '1';
    menu.style.transform = 'translateY(0)';

    // Obtém os lugares da tabela
    var assentos = document.querySelectorAll('.assento');

    // Simula dados cadastrados (números de armários ocupados)
    var armariosOcupados = [];

    // Adiciona evento de clique a cada assento
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

