function buscarCep(valorCep) {
    const cep = valorCep.replace(/\D/g, '');

    if (cep.length !== 8) return limparEndereco();

    fetch(`https://viacep.com.br/ws/${cep}/json`)
        .then(async response => {
            if (!response.ok) {
                limparEndereco();
                return null;
            }

            const data = await response.json();

            if (data.erro) {
                limparEndereco();
                return null;
            }

            return data;
        })
        .then(endereco => {
            if (!endereco) return;
            document.getElementById("rua").value = endereco.logradouro || "";
            document.getElementById("cidade").value = endereco.localidade || "";
            document.getElementById("uf").value = endereco.uf || "";
            document.getElementById("complemento").value = endereco.complemento || "";
        })
        .catch(error => {
            limparEndereco();
        });
}

function limparEndereco() {
    document.querySelectorAll(".input-endereco").forEach(input => {
        input.value = "";
    });
}
