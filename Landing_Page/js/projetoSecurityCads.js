// Função para rolar até o topo da página
function scrollToTop() {
    document.body.scrollTop = 0;
    document.documentElement.scrollTop = 0;
}

// Mostrar ou ocultar o botão de rolar para o topo com base no scroll
window.onscroll = function () {
    var btn = document.getElementById("scrollToTopBtn");
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        btn.style.display = "block";
    } else {
        btn.style.display = "none";
    }
};

// Função para abrir o pop-up
function openModal() {
  document.getElementById("myModal").style.display = "block";
}

// Função para fechar o pop-up
function closeModal() {
  document.getElementById("myModal").style.display = "none";
}


// Função para rolar até o topo da página
function scrollToTop() {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}

// Mostrar ou ocultar o botão de rolar para o topo com base no scroll
window.onscroll = function () {
  var btn = document.getElementById("scrollToTopBtn");
  if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
      btn.style.display = "block";
  } else {
      btn.style.display = "none";
  }

  
};
