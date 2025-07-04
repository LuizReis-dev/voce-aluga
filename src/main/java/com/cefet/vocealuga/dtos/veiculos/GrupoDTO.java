package com.cefet.vocealuga.dtos.veiculos;

public class GrupoDTO {
	private Integer id;
	private String nome;

	public GrupoDTO(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
