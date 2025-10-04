package com.example.festival.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "asignaciones_cancion")
public class AsignacionCancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "cancion_id")
    private Cancion cancion;

    @Column
    private Integer intentos = 0;

    @Column
    private Boolean adivinada = false;


    public AsignacionCancion() {
    }

    public AsignacionCancion(Usuario usuario) {
        this.usuario = usuario;
    }

    public AsignacionCancion(Integer id, Usuario usuario, Cancion cancion) {
        this.usuario = usuario;
        this.cancion = cancion;
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

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public Boolean getAdivinada() {
        return adivinada;
    }

    public void setAdivinada(Boolean adivinada) {
        this.adivinada = adivinada;
    }
}
