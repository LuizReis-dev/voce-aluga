function buscarCliente(cpf) {
    zerarTela();
    if (!cpf || cpf.length < 11) return;

    fetch(`/admin/api/v1/clientes/${cpf}`)
        .then(async response => {
            const mensagemErro = document.getElementById("mensagemErro");
            const mensagemTexto = document.getElementById("mensagemTexto");

            if (!response.ok) {
                const erro = await response.json();

                mensagemTexto.textContent = erro.mensagem;
                mensagemErro.classList.remove("hidden");

                limparCampos();
                if(!erro.bloqueante) {
                    disableCampos('input-cliente', false);
                }

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

            disableCampos('input-cliente', false);
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

function disableCampos(classname, estado) {
    document.querySelectorAll(`.${classname}`).forEach(input => input.disabled = estado);
}

function zerarTela() {
    document.getElementById("mensagemErro").classList.add("hidden");
    limparCampos();
    disableCampos('input-cliente', true);
}