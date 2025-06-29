package com.cefet.vocealuga.webservices.exceptions;

public class WebserviceException extends RuntimeException {
    private boolean bloqueante;

    public WebserviceException(String message, boolean bloqueante) {
        super(message);
        this.bloqueante = bloqueante;
    }

    public boolean isBloqueante() {
        return bloqueante;
    }

    public void setBloqueante(boolean bloqueante) {
        this.bloqueante = bloqueante;
    }
}
