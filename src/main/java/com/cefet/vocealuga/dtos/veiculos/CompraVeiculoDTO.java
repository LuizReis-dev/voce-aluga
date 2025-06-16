package com.cefet.vocealuga.dtos.veiculos;

public class CompraVeiculoDTO {
    private Integer modeloId;
    private String placa;
    private String chassi;
    private Double valor;
    private String cor;
    private Integer quilometragem;

    public CompraVeiculoDTO() {
    }

    public CompraVeiculoDTO(Integer modeloId, String placa, String chassi, Double valor) {
        this.modeloId = modeloId;
        this.placa = placa;
        this.chassi = chassi;
        this.valor = valor;
    }

    public Integer getModeloId() {
        return modeloId;
    }

    public void setModeloId(Integer modeloId) {
        this.modeloId = modeloId;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(Integer quilometragem) {
        this.quilometragem = quilometragem;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}
