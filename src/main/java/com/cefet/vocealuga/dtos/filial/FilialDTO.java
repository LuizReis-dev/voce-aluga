package com.cefet.vocealuga.dtos.filial;

public class FilialDTO {
    private Integer id;
    private String nome;
    private String enderecoCompleto;

    public FilialDTO(Integer id, String nome, String enderecoCompleto) {
        this.id = id;
        this.nome = nome;
        this.enderecoCompleto = enderecoCompleto;
    }

    public FilialDTO() {
    }

    public Integer getId() {
        return id;
    }

    public FilialDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public FilialDTO setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }
}
