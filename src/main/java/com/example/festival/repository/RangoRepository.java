package com.example.festival.repository;

import com.example.festival.entity.Rango;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RangoRepository extends JpaRepository<Rango, Integer> {
}
