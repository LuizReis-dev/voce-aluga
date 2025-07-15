package com.cefet.vocealuga.pagamento;

import com.cefet.vocealuga.reserva.Reserva;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagamento")
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_de_pagamento")
    private FormaPagamento formaPagamento;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDate dataPagamento;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "nota_fiscal", nullable = false)
    private String notaFiscal;

    private Boolean reembolsado;
    public Pagamento() {
    }

    public Pagamento(Integer id, Reserva reserva, FormaPagamento formaPagamento, LocalDate dataPagamento, BigDecimal valor, String notaFiscal) {
        this.id = id;
        this.reserva = reserva;
        this.formaPagamento = formaPagamento;
        this.valor = valor;
        this.notaFiscal = notaFiscal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public Boolean getReembolsado() {
        return reembolsado;
    }

    public void setReembolsado(Boolean reembolsado) {
        this.reembolsado = reembolsado;
    }
}
