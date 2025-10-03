package com.example.festival.repository;

import com.example.festival.entity.Intencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntencionRepository extends JpaRepository<Intencion, Integer> {

    Intencion findByUsuario_Id(Integer idUsuario);

    Boolean existsByUsuario_Id(Integer idUsuario);

}
