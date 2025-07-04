package com.cefet.vocealuga.webservices.responses;

public class CreateClienteResponse {
    private String mensagem;
    private Integer id;

    public CreateClienteResponse() {
    }

    public CreateClienteResponse(String mensagem, Integer id) {
        this.mensagem = mensagem;
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
