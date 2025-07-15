package com.cefet.vocealuga.veiculo;

public enum TipoTransacaoVeiculo {
    COMPRA("Compra"),
    VENDA("Venda"),
    TRANSFERENCIA_SOLICITADA("Transferência Solicitada"),
    TRANSFERENCIA_ACEITA("Transferência aceita pela filial de origem"),
    TRANSFERENCIA_RECUSADA("Transferência recusada pela filial de origem"),
    MANUTENCAO("Manutenção");

    private final String descricao;

    TipoTransacaoVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}