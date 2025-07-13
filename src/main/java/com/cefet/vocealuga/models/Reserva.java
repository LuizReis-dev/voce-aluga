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

	@ManyToOne
	@JoinColumn(name = "filial_id")
	private Filial filial;

	@Column(name = "data_entrega", nullable = false)
	private LocalDate dataEntrega;

	@Column(name = "data_devolucao", nullable = false)
	private LocalDate dataDevolucao;

	@Column(nullable = false)
	private BigDecimal valor;

	@Column(name = "origem", nullable = true)
	private String origem;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private StatusReserva status = StatusReserva.CRIADA;

	@ManyToOne
	@JoinColumn(name = "veiculo_original_id")
	private Veiculo veiculoOriginal;

	@OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Pagamento pagamento;

	public Reserva() {
	}

	public Reserva(Integer id, Veiculo veiculo, Cliente cliente, Operador operador, LocalDate dataEntrega,
                   LocalDate dataDevolucao, BigDecimal valor, Filial filial, String origem) {
		this.id = id;
		this.veiculo = veiculo;
		this.cliente = cliente;
		this.operador = operador;
		this.dataEntrega = dataEntrega;
		this.dataDevolucao = dataDevolucao;
		this.valor = valor;
		this.filial = filial;
        this.origem = origem;
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

	public LocalDate getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDate dataEntegra) {
		this.dataEntrega = dataEntegra;
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

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public StatusReserva getStatus() {
		return status;
	}

	public void setStatus(StatusReserva status) {
		this.status = status;
	}

	public Veiculo getVeiculoOriginal() {
		return veiculoOriginal;
	}

	public void setVeiculoOriginal(Veiculo veiculoOriginal) {
		this.veiculoOriginal = veiculoOriginal;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}
}
