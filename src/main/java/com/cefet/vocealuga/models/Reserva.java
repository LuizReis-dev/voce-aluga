package com.cefet.vocealuga.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "operador_id")
    private Operador operador;

    @Column(name = "data_entrega", nullable = false)
    private LocalDate dataEntegra;

    @Column(name = "data_devolucao", nullable = false)
    private LocalDate dataDevolucao;

    @Column(nullable = false)
    private BigDecimal valor;

    public Reserva() {
    }

    public Reserva(Integer id, Veiculo veiculo, Cliente cliente, Operador operador, LocalDate dataEntegra, LocalDate dataDevolucao, BigDecimal valor) {
        this.id = id;
        this.veiculo = veiculo;
        this.cliente = cliente;
        this.operador = operador;
        this.dataEntegra = dataEntegra;
        this.dataDevolucao = dataDevolucao;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    public LocalDate getDataEntegra() {
        return dataEntegra;
    }

    public void setDataEntegra(LocalDate dataEntegra) {
        this.dataEntegra = dataEntegra;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
