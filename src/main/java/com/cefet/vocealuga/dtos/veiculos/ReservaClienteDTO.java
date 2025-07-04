package com.cefet.vocealuga.dtos.veiculos;

import com.cefet.vocealuga.models.FormaPagamento;

import java.time.LocalDate;

public class ReservaClienteDTO {
	private Integer clienteId;
	private Integer grupoId;
	private Integer modeloId;
	private Integer filialId;
	private LocalDate dataEntrega;
	private LocalDate dataDevolucao;
	private FormaPagamento formaPagamento;

	public ReservaClienteDTO() {
	}

	public ReservaClienteDTO(Integer clienteId, Integer grupoId, Integer modeloId, LocalDate dataEntrega,
			LocalDate dataDevolucao, FormaPagamento formaPagamento, Integer filialId) {
		this.clienteId = clienteId;
		this.grupoId = grupoId;
		this.modeloId = modeloId;
		this.dataEntrega = dataEntrega;
		this.dataDevolucao = dataDevolucao;
		this.formaPagamento = formaPagamento;
		this.filialId = filialId;
	}

	public Integer getClienteId() {
		return clienteId;
	}

	public void setClienteId(Integer clienteId) {
		this.clienteId = clienteId;
	}

	public Integer getGrupoId() {
		return grupoId;
	}

	public void setGrupoId(Integer grupoId) {
		this.grupoId = grupoId;
	}

	public Integer getModeloId() {
		return modeloId;
	}

	public void setModeloId(Integer modeloId) {
		this.modeloId = modeloId;
	}

	public LocalDate getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDate dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Integer getFilialId() {
		return filialId;
	}

	public void setFilialId(Integer filialId) {
		this.filialId = filialId;
	}
}
