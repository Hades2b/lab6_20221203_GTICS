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

    @Column(name = "numero_objetivo", nullable = false)
    private Integer numeroObjetivo;

    @Column(name = "intentos")
    private Integer intentos;

    @Column(name = "adivinado")
    private Boolean adivinado;
}
