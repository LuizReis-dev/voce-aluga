package com.cefet.vocealuga.dtos.veiculos;

import jakarta.validation.constraints.NotNull;

public class SolicitacaoTransferenciaDTO {
    @NotNull(message = "Modelo é obrigatório")
    private Integer modeloId;
    @NotNull(message = "Filial é obrigatório")
    private Integer filialId;

    public SolicitacaoTransferenciaDTO(Integer modeloId, Integer filialId) {
        this.modeloId = modeloId;
        this.filialId = filialId;
    }

    public SolicitacaoTransferenciaDTO() {
    }

    public Integer modeloId() {
        return modeloId;
    }

    public SolicitacaoTransferenciaDTO setModeloId(Integer modeloId) {
        this.modeloId = modeloId;
        return this;
    }

    public Integer filialId() {
        return filialId;
    }

    public SolicitacaoTransferenciaDTO setFilialId(Integer filialId) {
        this.filialId = filialId;
        return this;
    }
}