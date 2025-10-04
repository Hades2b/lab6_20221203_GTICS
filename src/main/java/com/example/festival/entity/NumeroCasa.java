package com.example.festival.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "numeros_casa")
public class NumeroCasa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "numero_objetivo")
    private Integer numeroObjetivo;

    @Column(name = "intentos")
    private Integer intentos = 0;

    @Column(name = "adivinado")
    private Boolean adivinado = false;


    public NumeroCasa() {
    }

    public NumeroCasa(Usuario usuario) {
        this.usuario = usuario;
    }

    public NumeroCasa(Integer id, Usuario usuario, Integer numObjetivo) {
        this.usuario = usuario;
        this.numeroObjetivo = numObjetivo;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getNumeroObjetivo() {
        return numeroObjetivo;
    }

    public void setNumeroObjetivo(Integer numeroObjetivo) {
        this.numeroObjetivo = numeroObjetivo;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public Boolean getAdivinado() {
        return adivinado;
    }

    public void setAdivinado(Boolean adivinado) {
        this.adivinado = adivinado;
    }
}
