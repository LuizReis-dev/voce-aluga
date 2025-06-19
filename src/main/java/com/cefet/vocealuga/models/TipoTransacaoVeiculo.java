package com.cefet.vocealuga.models;

public enum TipoTransacaoVeiculo {
    COMPRA("Compra"),
    VENDA("Venda"),
    TRANSACAO_FILIAL("Transferência entre filiais"),
    MANUTENCAO("Manutenção");

    private final String descricao;

    TipoTransacaoVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}