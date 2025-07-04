package com.cefet.vocealuga.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "reserva")
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// Relação com Usuario (muitos para um)
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	// Relação com Veiculo (muitos para um)
	@ManyToOne
	@JoinColumn(name = "veiculo_id")
	private Veiculo veiculo;

	@Column(name = "data_entrega")
	private LocalDate dataEntrega;

	@Column(name = "data_devolucao")
	private LocalDate dataDevolucao;

	@Column
	private BigDecimal valor;

	public Reserva() {
	}

	public Reserva(Integer id, Usuario usuario, Veiculo veiculo, LocalDate dataEntrega, LocalDate dataDevolucao,
			BigDecimal valor) {
		this.id = id;
		this.usuario = usuario;
		this.veiculo = veiculo;
		this.dataEntrega = dataEntrega;
		this.dataDevolucao = dataDevolucao;
		this.valor = valor;
	}

	public Integer getId() {
		return id;
	}

	public Reserva setId(Integer id) {
		this.id = id;
		return this;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public Reserva setUsuario(Usuario usuario) {
		this.usuario = usuario;
		return this;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public Reserva setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
		return this;
	}

	public LocalDate getDataEntrega() {
		return dataEntrega;
	}

	public Reserva setDataEntrega(LocalDate dataEntrega) {
		this.dataEntrega = dataEntrega;
		return this;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}

	public Reserva setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
		return this;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public Reserva setValor(BigDecimal valor) {
		this.valor = valor;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Reserva))
			return false;
		Reserva reserva = (Reserva) o;
		return Objects.equals(id, reserva.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
