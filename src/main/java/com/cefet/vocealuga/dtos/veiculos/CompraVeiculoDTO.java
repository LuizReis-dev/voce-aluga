package com.cefet.vocealuga.dtos.veiculos;

import jakarta.validation.constraints.*;

public class CompraVeiculoDTO {

    @NotNull(message = "Modelo é obrigatório")
    private Integer modeloId;

    @NotBlank(message = "Placa é obrigatória")
    @Pattern(
            regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$",
            message = "Placa deve estar no padrão Mercosul (ex: ABC1D23)"
    )
    private String placa;

    @NotBlank(message = "Chassi é obrigatório")
    @Size(min = 17, max = 17, message = "Chassi deve ter exatamente 17 caracteres")
    private String chassi;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    private Double valor;

    @NotBlank(message = "Cor é obrigatória")
    private String cor;

    @Min(value = 0, message = "Quilometragem não pode ser negativa")
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
