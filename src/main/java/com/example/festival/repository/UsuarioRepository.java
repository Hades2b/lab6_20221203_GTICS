package com.example.festival.repository;

import com.example.festival.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

        Usuario findByCorreo(String correo);

}
