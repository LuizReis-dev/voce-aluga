package com.cefet.vocealuga.reserva;

public enum StatusReserva {
    CRIADA("Criada"),
    AGUARDANDO_ENTREGA("Aguardando Entrega"),
    CANCELADA("Cancelada"),
    ENTREGUE("Entregue"),
    FINALIZADA("Finalizada");

    private String descricao;

    StatusReserva(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
