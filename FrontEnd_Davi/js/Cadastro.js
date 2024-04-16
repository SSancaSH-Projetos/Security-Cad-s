document.addEventListener("DOMContentLoaded", function() {
    var cadastroForm = document.getElementById("cadastroForm");
  
    cadastroForm.addEventListener("submit", function(event) {
        event.preventDefault();
  
        var nomeInput = document.getElementById("nome");
        var emailInput = document.getElementById("email");
        var senhaInput = document.getElementById("senha");
  
        if (nomeInput.value === "" || emailInput.value === "" || senhaInput.value === "") {
            alert("Por favor, preencha todos os campos.");
        } else {
            // Armazenar o nome no localStorage (pode ser usado para um propósito mais permanente)
            localStorage.setItem("nomeUsuario", nomeInput.value);
  
            // Redirecionar para a página inicial
            window.location.href = "PaginaInicial.html";
        }
    });
  });