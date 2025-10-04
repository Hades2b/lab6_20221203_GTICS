package com.example.festival.repository;

import com.example.festival.entity.AsignacionCancion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsignacionRepository extends JpaRepository<AsignacionCancion, Integer> {

    AsignacionCancion findByUsuario_Id(int userId);

    List<AsignacionCancion> findByCancionNull();

}
