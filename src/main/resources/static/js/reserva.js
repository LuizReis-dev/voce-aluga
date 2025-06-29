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
            document.getElementById("cep").disabled = false;

            document.getElementById("botaoSalvar").disabled = false;
        })
        .catch(error => {
            console.error("Erro ao buscar cliente:", error);
            zerarTela();
        });
}
function buscarCep(valorCep) {
    const cep = valorCep.value.replace(/\D/g, '');

    if(cep.length !== 8) return limparEndereco();

    fetch(`https://viacep.com.br/ws/${cep}/json`)
        .then(async response => {
            const mensagemErro = document.getElementById("mensagemErro");
            const mensagemTexto = document.getElementById("mensagemTexto");

            if (!response.ok) {
                mensagemTexto.textContent = "Erro ao buscar o CEP.";
                mensagemErro.classList.remove("hidden");
                limparEndereco();
                return null;
            }

            const data = await response.json();

            if (data.erro) {
                mensagemTexto.textContent = "CEP não encontrado.";
                mensagemErro.classList.remove("hidden");
                limparEndereco();
                return null;
            }

            mensagemErro.classList.add("hidden");
            return data;
        })
        .then(endereco => {
            if (!endereco) return;

            document.getElementById("rua").value = endereco.logradouro || "";
            document.getElementById("cidade").value = endereco.localidade || "";
            document.getElementById("uf").value = endereco.uf || "";
            document.getElementById("complemento").value = endereco.complemento || "";

            disableCampos('input-endereco', false);
        })
        .catch(error => {
            console.error("Erro ao buscar endereço via CEP:", error);
            limparEndereco();
        });
}
function limparCampos() {
    limparClientes();
    limparEndereco();
}
function limparClientes() {
    document.querySelectorAll(".input-cliente").forEach(input => {
        input.value = "";
        input.disabled = true;
    });
}
function limparEndereco() {
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
}

