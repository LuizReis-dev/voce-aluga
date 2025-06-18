package com.cefet.vocealuga.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Table(name = "gerenciamento_transacao_veiculo")
@Entity
public class GerenciamentoTransacaoVeiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao", nullable = false)
    private TipoTransacaoVeiculo tipoTransacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origem_filial_id")
    private Filial filialOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_filial_id")
    private Filial filialDestino;

    @Column(name = "data_transacao", nullable = false)
    private LocalDate dataTransacao;

    @Column(name = "data_fim_transacao")
    private LocalDate dataFimTransacao;

    private Double valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operador_id", nullable = false)
    private Operador operador;

    public GerenciamentoTransacaoVeiculo(Integer id, TipoTransacaoVeiculo tipoTransacao, Veiculo veiculo, Filial filialOrigem, Filial filialDestino, LocalDate dataTransacao, LocalDate dataFimTransacao, Double valor, Operador operador) {
        this.id = id;
        this.tipoTransacao = tipoTransacao;
        this.veiculo = veiculo;
        this.filialOrigem = filialOrigem;
        this.filialDestino = filialDestino;
        this.dataTransacao = dataTransacao;
        this.dataFimTransacao = dataFimTransacao;
        this.valor = valor;
        this.operador = operador;
    }

    public GerenciamentoTransacaoVeiculo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoTransacaoVeiculo getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(TipoTransacaoVeiculo tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Filial getFilialOrigem() {
        return filialOrigem;
    }

    public void setFilialOrigem(Filial filialOrigem) {
        this.filialOrigem = filialOrigem;
    }

    public Filial getFilialDestino() {
        return filialDestino;
    }

    public void setFilialDestino(Filial filialDestino) {
        this.filialDestino = filialDestino;
    }

    public LocalDate getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDate dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public LocalDate getDataFimTransacao() {
        return dataFimTransacao;
    }

    public void setDataFimTransacao(LocalDate dataFimTransacao) {
        this.dataFimTransacao = dataFimTransacao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }
}
