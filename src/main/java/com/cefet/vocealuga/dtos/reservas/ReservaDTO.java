package com.cefet.vocealuga.dtos.reservas;

import com.cefet.vocealuga.models.StatusReserva;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReservaDTO {
    private Integer id;
    private String nomeCliente;
    private String cpfCliente;
    private String modelo;
    private BigDecimal valor;
    private StatusReserva status;
    private String origem;
    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;

    public ReservaDTO(Integer id, String nomeCliente, String cpfCliente, String modelo, BigDecimal valor, StatusReserva status, String origem, LocalDate dataEntrega, LocalDate dataDevolucao) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.modelo = modelo;
        this.valor = valor;
        this.status = status;
        this.origem = origem;
        this.dataEntrega = dataEntrega;
        this.dataDevolucao = dataDevolucao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
}
