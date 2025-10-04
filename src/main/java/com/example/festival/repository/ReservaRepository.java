package com.example.festival.repository;

import com.example.festival.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    Reserva findByUsuario_IdAndId(int id, int reserva_id);

    Reserva findByMesa_IdAndUsuario_Id(int idMesa, int idUsuario);

}
