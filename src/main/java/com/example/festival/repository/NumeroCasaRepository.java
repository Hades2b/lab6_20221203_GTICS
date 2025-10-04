package com.example.festival.repository;

import com.example.festival.entity.NumeroCasa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NumeroCasaRepository extends JpaRepository<NumeroCasa, Integer> {

    NumeroCasa findByUsuario_Id(int idUsuario);

    List<NumeroCasa> findByNumeroObjetivoNull();

    List<NumeroCasa> findByAdivinadoTrueOrderByIntentosAsc();

}
