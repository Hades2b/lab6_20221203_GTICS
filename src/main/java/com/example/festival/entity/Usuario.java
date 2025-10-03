package com.example.festival.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(nullable = false, length = 255)
    private String password;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @OneToMany(mappedBy = "usuario")
    private List<Intencion> intenciones;

    @OneToMany(mappedBy = "usuario")
    private List<AsignacionCancion> asignaciones;

    @OneToMany(mappedBy = "usuario")
    private List<NumeroCasa> numerosCasa;

    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Intencion> getIntenciones() {
        return intenciones;
    }

    public void setIntenciones(List<Intencion> intenciones) {
        this.intenciones = intenciones;
    }

    public List<AsignacionCancion> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<AsignacionCancion> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public List<NumeroCasa> getNumerosCasa() {
        return numerosCasa;
    }

    public void setNumerosCasa(List<NumeroCasa> numerosCasa) {
        this.numerosCasa = numerosCasa;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}