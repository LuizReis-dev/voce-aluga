package com.cefet.vocealuga.webservices.requests;

import java.time.LocalDate;

public class CalculoValorReservaRequest {
    private Integer grupoId;
    private LocalDate dateEntrega;
    private LocalDate dataDevolucao;

    public CalculoValorReservaRequest() {
    }

    public CalculoValorReservaRequest(Integer grupoId, LocalDate dateEntrega, LocalDate dataDevolucao) {
        this.grupoId = grupoId;
        this.dateEntrega = dateEntrega;
        this.dataDevolucao = dataDevolucao;
    }

    public Integer getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(Integer grupoId) {
        this.grupoId = grupoId;
    }

    public LocalDate getDateEntrega() {
        return dateEntrega;
    }

    public void setDateEntrega(LocalDate dateEntrega) {
        this.dateEntrega = dateEntrega;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
}
