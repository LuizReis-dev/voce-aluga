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
                    document.getElementById("cep").disabled = false;
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
            document.getElementById("cnh").value = cliente.cnh;
            document.getElementById("apolice").value = cliente.apolice;
            document.getElementById("cep").value = cliente.cep;
            document.getElementById("rua").value = cliente.rua;
            document.getElementById("numero").value = cliente.numero;
            document.getElementById("complemento").value = cliente.complemento;
            document.getElementById("cidade").value = cliente.cidade;
            document.getElementById("uf").value = cliente.uf;

            disableCampos('input-cliente', false);
            disableCampos('input-endereco', false);
            document.getElementById("botaoSalvar").disabled = false;
        })
        .catch(error => {
            console.error("Erro ao buscar cliente:", error);
            zerarTela();
        });
}

function limparCampos() {
    document.querySelectorAll(".input-cliente").forEach(input => {
        input.value = "";
        input.disabled = true;
    });
    document.querySelectorAll(".input-endereco").forEach(input => {
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
    disableCampos('input-cliente', false);

}