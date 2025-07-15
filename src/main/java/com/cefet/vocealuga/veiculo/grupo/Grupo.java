package com.cefet.vocealuga.veiculo.grupo;

import com.cefet.vocealuga.veiculo.modelo.ModeloVeiculo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "grupo")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String nome;
    @Column(name = "preco_por_dia", nullable = false)
    private BigDecimal precoPorDia;
    @OneToMany(mappedBy = "grupo")
    private List<ModeloVeiculo> modelosVeiculo;

    public Grupo() {
    }

    public Grupo(Integer id, String nome, BigDecimal precoPorDia) {
        this.id = id;
        this.nome = nome;
        this.precoPorDia = precoPorDia;
    }

    public Integer getId() {
        return id;
    }

    public Grupo setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrecoPorDia() {
        return precoPorDia;
    }

    public void setPrecoPorDia(BigDecimal precoPorDia) {
        this.precoPorDia = precoPorDia;
    }

    public List<ModeloVeiculo> getModelosVeiculo() {
        return modelosVeiculo;
    }

    public Grupo setModelosVeiculo(List<ModeloVeiculo> modelosVeiculo) {
        this.modelosVeiculo = modelosVeiculo;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grupo grupo = (Grupo) o;
        return Objects.equals(id, grupo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
