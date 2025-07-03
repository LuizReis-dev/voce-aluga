let clienteId = 0;

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
                document.getElementById("cep").value = "";
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
            document.getElementById("cep").value = cliente.cep.replace(/^(\d{5})(\d{3})$/, "$1-$2");
            document.getElementById("rua").value = cliente.rua;
            document.getElementById("numero").value = cliente.numero;
            document.getElementById("complemento").value = cliente.complemento;
            document.getElementById("cidade").value = cliente.cidade;
            document.getElementById("uf").value = cliente.uf;

            disableCampos('input-cliente', false);
            disableCampos('input-endereco', false);
            document.getElementById("cep").disabled = false;

        })
        .catch(error => {
            console.error("Erro ao buscar cliente:", error);
            zerarTela();
        });
}

function buscarCep(valorCep) {
    const cep = valorCep.replace(/\D/g, '');

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

function cadastrarCliente(event) {
    event.preventDefault();

    const botaoSalvar = document.getElementById("botaoSalvar");
    botaoSalvar.disabled = true;

    const mensagemErro = document.getElementById("mensagemErro");
    const mensagemTexto = document.getElementById("mensagemTexto");

    const cliente = {
        cpf: document.getElementById("cpf").value.replace(/\D/g, ''),
        nome: document.getElementById("nome").value,
        email: document.getElementById("email").value,
        telefone: document.getElementById("telefone").value,
        dataNascimento: document.getElementById("dataNascimento").value,
        cnh: document.getElementById("cnh").value,
        apolice: document.getElementById("apolice").value,
        cep: document.getElementById("cep").value,
        rua: document.getElementById("rua").value,
        numero: document.getElementById("numero").value,
        complemento: document.getElementById("complemento").value,
        cidade: document.getElementById("cidade").value,
        uf: document.getElementById("uf").value.toUpperCase()
    };

    fetch("/admin/api/v1/clientes", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(cliente)
    })
        .then(function(response) {
            if (!response.ok) {
                return response.text().then(function(erro) {
                    mensagemTexto.textContent = "Erro ao cadastrar cliente.";
                    mensagemErro.classList.remove("hidden");
                    botaoSalvar.disabled = false;

                    throw new Error("Erro no cadastro");
                });
            }
            return response.json();
        })
        .then(function(responseBody) {
            mensagemErro.classList.add("hidden");
            botaoSalvar.disabled = false;
            clienteId = responseBody.id;
            document.getElementById("form-cliente").style.display = "none";
            document.getElementById("form-reserva").style.display = "block";

        })
        .catch(function(error) {
            console.error("Erro de rede ou do servidor:", error);
            if (mensagemErro.classList.contains("hidden")) {
                mensagemTexto.textContent = "Erro de sistema ao cadastrar reserva.";
                mensagemErro.classList.remove("hidden");
            }
            botaoSalvar.disabled = false;
        });
}

function buscarModelos(grupo) {
    const mensagemErro = document.getElementById("mensagemErro");
    const mensagemTexto = document.getElementById("mensagemTexto");
    if(grupo === "") return;
    let erroBuscaModelos = false;
    const dataEntregaInput = document.getElementById("dataEntrega");
    const dataDevolucaoInput = document.getElementById("dataDevolucao");

    fetch(`/admin/api/v1/modelos/${grupo}/${dataEntregaInput.value}/${dataDevolucaoInput.value}`)
        .then(response => {
            const mensagemErro = document.getElementById("mensagemErro");
            const mensagemTexto = document.getElementById("mensagemTexto");
            const selectModelo = document.getElementById("modelo");
            disableCampos("input-reserva", true);
            if (!response.ok) {
                mensagemTexto.textContent = "Erro ao buscar modelos.";
                mensagemErro.classList.remove("hidden");
                erroBuscaModelos = true;
                return null;
            }

            return response.json().then(modelos => {
                const modelosDisponiveis = modelos.filter(m => m.quantidade > 0);

                if (modelosDisponiveis.length === 0) {
                    mensagemTexto.textContent = "Nenhum veículo disponível, orientar aluguel pelo site.";
                    mensagemErro.classList.remove("hidden");
                    selectModelo.disabled = true;
                    erroBuscaModelos = true;
                    return;
                }

                selectModelo.innerHTML = '<option value="" selected>Selecione um Modelo</option>';
                selectModelo.disabled = false;
                mensagemErro.classList.add("hidden");

                modelosDisponiveis.forEach(modelo => {
                    const option = document.createElement("option");
                    option.value = modelo.id;
                    option.textContent = `${modelo.marca} ${modelo.modelo} (${modelo.ano})`;
                    selectModelo.appendChild(option);
                });
                disableCampos("input-reserva", false);

            });
        })
        .catch(error => {
            console.error("Erro ao buscar modelos:", error);
            const mensagemErro = document.getElementById("mensagemErro");
            const mensagemTexto = document.getElementById("mensagemTexto");
            mensagemTexto.textContent = "Erro ao buscar modelos.";
            mensagemErro.classList.remove("hidden");
            disableCampos("input-reserva", true);
            erroBuscaModelos = true;
        });

    fetch("/admin/api/v1/reservas/calcular-valor", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            grupoId: parseInt(grupo),
            dateEntrega: dataEntregaInput.value,
            dataDevolucao: dataDevolucaoInput.value
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao calcular valor da reserva.");
            }
            return response.json();
        })
        .then(data => {
            exibirValorReserva(data.valorReserva);
        })
        .catch(error => {
            console.error("Erro ao calcular valor da reserva:", error);
            mensagemTexto.textContent = "Erro ao calcular valor da reserva.";
            mensagemErro.classList.remove("hidden");
        });

}

function zerarHora(data) {
    data.setHours(0, 0, 0, 0);
    return data;
}

function handleDataEntregaChange(dataEntregaInput) {
    zeraTelaReserva();
    const dataDevolucaoInput = document.getElementById("dataDevolucao");

    dataDevolucaoInput.value = "";
    dataDevolucaoInput.disabled = true;

    const mensagemErro = document.getElementById("mensagemErro");
    const mensagemTexto = document.getElementById("mensagemTexto");

    if (dataEntregaInput.value === "") return;

    const [ano, mes, dia] = dataEntregaInput.value.split("-");
    const dataEntrega = zerarHora(new Date(ano, mes - 1, dia));
    const hoje = zerarHora(new Date());

    mensagemErro.classList.add("hidden");

    if (dataEntrega < hoje) {
        mensagemTexto.textContent = "A data de entrega não pode ser anterior a hoje.";
        mensagemErro.classList.remove("hidden");
        dataEntregaInput.value = "";
    }

    dataDevolucaoInput.disabled = false;

}

function handleDataDevolucaoChange(dataDevolucaoInput) {
    zeraTelaReserva();
    const mensagemErro = document.getElementById("mensagemErro");
    const mensagemTexto = document.getElementById("mensagemTexto");

    const dataEntregaInput = document.getElementById("dataEntrega");

    if (dataDevolucaoInput.value === "") return;

    mensagemErro.classList.add("hidden");

    if (dataEntregaInput.value === "") {
        mensagemTexto.textContent = "Preencha a data de entrega antes da data de devolução.";
        mensagemErro.classList.remove("hidden");
        dataDevolucaoInput.value = "";
        return;
    }

    const [anoE, mesE, diaE] = dataEntregaInput.value.split("-");
    const [anoD, mesD, diaD] = dataDevolucaoInput.value.split("-");
    const dataEntrega = zerarHora(new Date(anoE, mesE - 1, diaE));
    const dataDevolucao = zerarHora(new Date(anoD, mesD - 1, diaD));
    const hoje = zerarHora(new Date());

    mensagemErro.classList.add("hidden");

    if (dataDevolucao < hoje) {
        mensagemTexto.textContent = "A data de devolução não pode ser anterior a hoje.";
        mensagemErro.classList.remove("hidden");
        dataDevolucaoInput.value = "";
        return;
    }

    if (dataDevolucao < dataEntrega) {
        mensagemTexto.textContent = "A data de devolução não pode ser anterior à data de entrega.";
        mensagemErro.classList.remove("hidden");
        dataDevolucaoInput.value = "";
        return;
    }
    document.getElementById("grupo").disabled = false;
}

function exibirValorReserva(valor) {
    document.getElementById("valorTotal").value = `R$ ${valor}`;
}

function cadastrarReserva(event) {
    event.preventDefault();

    if (clienteId === 0) {
        alert("Ocorreu um erro, recomende reserva pelo site");
        return;
    }
    const grupo = document.getElementById("grupo").value;
    if(grupo === "") {
        alert("Preencha o grupo!");
        return
    }

    const modelo = document.getElementById("modelo").value;
    if(modelo === "") {
        alert("Preencha o modelo!");
        return;
    }

    const dataEntrega = document.getElementById("dataEntrega").value;
    if(dataEntrega === "") {
        alert("Preencha a dataEntrega!");
        return;
    }

    const dataDevolucao = document.getElementById("dataDevolucao").value;
    if(dataDevolucao === "") {
        alert("Preencha a dataDevolucao!");
        return;
    }

    const formaPagamento = document.getElementById("formaPagamento").value;
    if(formaPagamento === "") {
        alert("Preencha a forma de pagamento!");
        return;
    }

    const requestBody = {
        clienteId: clienteId,
        grupoId: grupo,
        modeloId: modelo,
        dataEntrega: dataEntrega,
        dataDevolucao: dataDevolucao,
        formaPagamento: formaPagamento
    }

    console.log(requestBody)
    const mensagemErro = document.getElementById("mensagemErro");
    const mensagemTexto = document.getElementById("mensagemTexto");

    const mensagemSucesso = document.getElementById("mensagemSucesso");
    const mensagemTextoSucesso = document.getElementById("mensagemTextoSucesso");
    fetch("/admin/api/v1/reservas", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(requestBody)
    })
        .then(function(response) {
            if (!response.ok) {
                return response.json().then(function(erro) {
                    mensagemTexto.textContent = erro.mensagem || "Erro ao cadastrar reserva.";
                    mensagemErro.classList.remove("hidden");
                });
            }

            mensagemTextoSucesso.textContent = "Reserva cadastrada com sucesso!";
            mensagemSucesso.classList.remove("hidden");
        })
        .catch(function(error) {
            console.error("Erro de rede ou do servidor:", error);
            if (mensagemErro.classList.contains("hidden")) {
                mensagemTexto.textContent = "Erro de rede ao cadastrar cliente.";
                mensagemErro.classList.remove("hidden");
            }
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

function zeraTelaReserva() {
    document.getElementById("modelo").value = "";
    document.getElementById("grupo").value = "";
    document.getElementById("valorTotal").value = "";
    document.getElementById("formaPagamento").value = "";
    disableCampos("input-reserva", true);
}
