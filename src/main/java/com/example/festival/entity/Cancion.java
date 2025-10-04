package com.example.festival.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "canciones_criollas")
public class Cancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(name = "letra", columnDefinition = "TEXT")
    private String letra;

    @OneToMany(mappedBy = "cancion")
    private List<AsignacionCancion> asignaciones;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public List<AsignacionCancion> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<AsignacionCancion> asignaciones) {
        this.asignaciones = asignaciones;
    }
}
