package com.cefet.vocealuga.usuario;

import com.cefet.vocealuga.filial.Filial;
import jakarta.persistence.*;

@Entity
@Table(name = "operador")
public class Operador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private CargoOperador cargo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "filial_id")
    private Filial filial;

    public Operador(Integer id, CargoOperador cargo, Usuario usuario, Filial filial) {
        this.id = id;
        this.cargo = cargo;
        this.usuario = usuario;
        this.filial = filial;
    }

    public Operador() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CargoOperador getCargo() {
        return cargo;
    }

    public void setCargo(CargoOperador cargo) {
        this.cargo = cargo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }
}
