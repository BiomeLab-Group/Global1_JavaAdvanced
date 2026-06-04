package com.BiomeLab.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.BiomeLab.Model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("FROM Usuario u WHERE u.email = :email")
	Optional<Usuario> findByEmail(@Param("email") String email);
}
