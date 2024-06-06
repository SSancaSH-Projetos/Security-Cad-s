function toggleExpansao() {
    const botao = document.querySelector('.botao-expansivel');
    botao.classList.toggle('botao-expandido');
}


const overlays = document.querySelectorAll('.overlay');

setInterval(() => {
    overlays.forEach(overlay => {
        overlay.querySelector('.text').classList.toggle('visible');
        overlay.querySelector('.card-text').classList.toggle('visible');
    });
}, 10000);
