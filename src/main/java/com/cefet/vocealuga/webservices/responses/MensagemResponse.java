package com.cefet.vocealuga.webservices.responses;

public class MensagemResponse {
    private String mensagem;
    private boolean bloqueante;

    public MensagemResponse(String mensagem, boolean bloqueante) {
        this.mensagem = mensagem;
        this.bloqueante = bloqueante;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isBloqueante() {
        return bloqueante;
    }

    public void setBloqueante(boolean bloqueante) {
        this.bloqueante = bloqueante;
    }
}
