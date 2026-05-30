package com.BiomeLab.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BiomeLab.Model.Estudo;

public interface EstudoRepository extends JpaRepository<Estudo, Long> {

}
