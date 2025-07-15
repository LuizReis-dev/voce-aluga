package com.cefet.vocealuga.veiculo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class VendaVeiculoDTO {

    @NotNull(message = "Modelo é obrigatório")
    private Integer modeloId;

    @NotBlank(message = "Placa é obrigatória")
    @Pattern(
            regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$",
            message = "Placa deve estar no padrão Mercosul (ex: ABC1D23)"
    )
    private String placa;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser maior que zero")
    private Double valor;

    public VendaVeiculoDTO() {
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

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
