package com.cefet.vocealuga.models;

public enum EstadoVeiculo {
    DISPONIVEL("Disponível"),
    RESERVADO("Reservado"),
    EM_MANUTENCAO("Em manutenção"),
    EM_TRANSFERENCIA("Em transferência"),
    VENDIDO("Vendido"),
    INDISPONIVEL("Indisponível");

    private final String descricao;

    EstadoVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}