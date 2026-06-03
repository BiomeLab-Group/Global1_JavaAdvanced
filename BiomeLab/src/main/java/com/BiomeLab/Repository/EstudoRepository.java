package com.BiomeLab.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.BiomeLab.Model.Estudo;

public interface EstudoRepository extends JpaRepository<Estudo, Long> {
	
	@Query(value = "FROM Estudo e "
			+ "Where e.ambiente.idAmbiente = :idAmbiente AND "
			+ "e.ambiente.usuario.idUsuario = :idUsuario ")
	public Optional<Estudo> retornaEstudoPorAmbientePorUsuario(
			@Param("idUsuario") Long idUsuario,
			@Param("idAmbiente") Long idAmbiente
			) ;

	@Query("""
		    FROM Estudo e
		    WHERE e.ambiente.usuario.idUsuario = :idUsuario
		    AND e.ambiente.statusAtivo = 'ATIVO'
		""")
	public Optional<Estudo> buscarEstudoDoAmbienteAtivo(
		    @Param("idUsuario") Long idUsuario
		);
}
