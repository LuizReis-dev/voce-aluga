package com.cefet.vocealuga.veiculo.dtos;

public class ModeloMarcaDTO {
	private String modelo;
	private String marca;

	public ModeloMarcaDTO(String modelo, String marca) {
		this.modelo = modelo;
		this.marca = marca;

	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
}