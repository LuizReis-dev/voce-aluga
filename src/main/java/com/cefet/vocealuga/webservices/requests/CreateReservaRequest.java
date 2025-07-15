package com.cefet.vocealuga.webservices.requests;

import com.cefet.vocealuga.pagamento.FormaPagamento;

import java.time.LocalDate;

public class CreateReservaRequest {
    private Integer clienteId;
    private Integer grupoId;
    private Integer modeloId;
    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;
    private FormaPagamento formaPagamento;

    public CreateReservaRequest() {
    }

    public CreateReservaRequest(Integer clienteId, Integer grupoId, Integer modeloId, LocalDate dataEntrega, LocalDate dataDevolucao, FormaPagamento formaPagamento) {
        this.clienteId = clienteId;
        this.grupoId = grupoId;
        this.modeloId = modeloId;
        this.dataEntrega = dataEntrega;
        this.dataDevolucao = dataDevolucao;
        this.formaPagamento = formaPagamento;
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
}
