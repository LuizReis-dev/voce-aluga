package com.cefet.vocealuga.webservices.responses;

import java.math.BigDecimal;

public class CalculoValorReservaResponse {
    private BigDecimal valorReserva;

    public CalculoValorReservaResponse() {

    }
    public CalculoValorReservaResponse(BigDecimal valorReserva) {
        this.valorReserva = valorReserva;
    }
    public BigDecimal getValorReserva() {
        return valorReserva;
    }
    public void setValorReserva(BigDecimal valorReserva) {}
}
