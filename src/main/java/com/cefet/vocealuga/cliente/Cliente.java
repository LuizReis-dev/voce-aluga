package com.cefet.vocealuga.cliente;

import com.cefet.vocealuga.filial.Endereco;
import com.cefet.vocealuga.usuario.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cnh;
    private String apolice;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    private Usuario usuario;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "endereco_id", unique = true, nullable = false)
    private Endereco endereco;
    public Cliente() {
    }

    public Cliente(Integer id, String cnh, String apolice, Usuario usuario, Endereco endereco) {
        this.id = id;
        this.cnh = cnh;
        this.apolice = apolice;
        this.usuario = usuario;
        this.endereco = endereco;
    }

    public Integer getId() {
        return id;
    }

    public Cliente setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getCnh() {
        return cnh;
    }

    public Cliente setCnh(String cnh) {
        this.cnh = cnh;
        return this;
    }

    public String getApolice() {
        return apolice;
    }

    public Cliente setApolice(String apolice) {
        this.apolice = apolice;
        return this;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Cliente setUsuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public Cliente setEndereco(Endereco endereco) {
        this.endereco = endereco;
        return this;
    }
}
