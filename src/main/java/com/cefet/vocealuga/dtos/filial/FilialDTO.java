package com.cefet.vocealuga.dtos.filial;

public class FilialDTO {
    private Integer id;
    private String nome;

    public FilialDTO(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
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
}
