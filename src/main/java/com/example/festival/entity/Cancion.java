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


}
