function calcularValorReserva(grupo, dataEntregaInput, dataDevolucaoInput) {
	fetch("/api/v1/reservas/calcular-valor", {
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
			console.log(data.valorReserva)
		})
		.catch(error => {
			console.error("Erro ao calcular valor da reserva:", error);
		});
}

function exibirValorReserva(valor) {
	document.getElementById("valorTotal").textContent = `R$ ${valor},00`;
}

function handleDataEntregaChange(dataEntregaInput) {
	const dataDevolucaoInput = document.getElementById("dataDevolucao");

	dataDevolucaoInput.value = "";
	dataDevolucaoInput.disabled = true;

	const mensagemErro = document.getElementById("mensagemErro");
	const mensagemTexto = document.getElementById("mensagemTexto");

	if (dataEntregaInput.value === "") return;

	const [ano, mes, dia] = dataEntregaInput.value.split("-");
	const dataEntrega = zerarHora(new Date(ano, mes - 1, dia));
	const hoje = zerarHora(new Date());


	if (dataEntrega < hoje) {
		dataEntregaInput.value = "";
	}

	dataDevolucaoInput.disabled = false;

	if (dataDevolucaoInput.value !== "") {
		const grupo = document.getElementById("grupo").value; // pegando grupo
		calcularValorReserva(grupo, dataEntregaInput, dataDevolucaoInput);
	}
}

function handleDataDevolucaoChange(dataDevolucaoInput) {
	const dataEntregaInput = document.getElementById("dataEntrega");

	if (dataDevolucaoInput.value === "") return;


	if (dataEntregaInput.value === "") {
		dataDevolucaoInput.value = "";
		return;
	}

	const [anoE, mesE, diaE] = dataEntregaInput.value.split("-");
	const [anoD, mesD, diaD] = dataDevolucaoInput.value.split("-");
	const dataEntrega = zerarHora(new Date(anoE, mesE - 1, diaE));
	const dataDevolucao = zerarHora(new Date(anoD, mesD - 1, diaD));
	const hoje = zerarHora(new Date());

	if (dataDevolucao < hoje) {
		dataDevolucaoInput.value = "";
		return;
	}

	if (dataDevolucao < dataEntrega) {
		dataDevolucaoInput.value = "";
		return;
	}
	document.getElementById("grupo").disabled = false;

	if (dataEntregaInput.value !== "" && dataDevolucaoInput.value !== "") {
		const grupo = document.getElementById("grupo").value;
		calcularValorReserva(grupo, dataEntregaInput, dataDevolucaoInput);
	}
}

function zerarHora(data) {
	data.setHours(0, 0, 0, 0);
	return data;
}