package com.BiomeLab.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BiomeLab.Model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
