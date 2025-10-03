package com.example.festival.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "rango")
public class Rango {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idRango;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombreRango;

    @OneToMany(mappedBy = "rango", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Heroe> heroes;


    public Integer getIdRango() {
        return idRango;
    }

    public void setIdRango(Integer idRango) {
        this.idRango = idRango;
    }

    public String getNombreRango() {
        return nombreRango;
    }

    public void setNombreRango(String nombreRango) {
        this.nombreRango = nombreRango;
    }

    public List<Heroe> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<Heroe> heroes) {
        this.heroes = heroes;
    }
}
