function buscarCliente(cpf) {
    if (!cpf || cpf.length < 11) return;

    fetch(`/admin/api/v1/clientes/${cpf}`)
        .then(async response => {
            const mensagemErro = document.getElementById("mensagemErro");
            const mensagemTexto = document.getElementById("mensagemTexto");
            const botaoSalvar = document.getElementById("botaoSalvar");

            if (!response.ok) {
                const erro = await response.json();

                mensagemTexto.textContent = erro.mensagem || "Cliente não encontrado.";
                mensagemErro.classList.remove("hidden");

                limparCampos();
                document.querySelectorAll(".input-cliente").forEach(input => input.disabled = false);

                return null;
            }

            mensagemErro.classList.add("hidden");
            return response.json();
        })
        .then(cliente => {
            if (!cliente) return;

            document.getElementById("nome").value = cliente.nome;
            document.getElementById("email").value = cliente.email;
            document.getElementById("telefone").value = cliente.telefone;
            document.getElementById("dataNascimento").value = cliente.dataNascimento;

            document.querySelectorAll(".input-cliente").forEach(input => input.disabled = false);
            document.getElementById("botaoSalvar").disabled = false;
        })
        .catch(error => {
            console.error("Erro ao buscar cliente:", error);
        });
}

function limparCampos() {
    document.querySelectorAll(".input-cliente").forEach(input => {
        input.value = "";
        input.disabled = true;
    });
}
