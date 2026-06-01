package com.BiomeLab.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.BiomeLab.Model.ConjuntoPropriedadesAtual;

public interface ConjuntoPropriedadesAtualRepository extends JpaRepository<ConjuntoPropriedadesAtual, Long>{
	
	
	@Query(value = "FROM ConjuntoPropriedadesAtual cp "
			+ "Where cp.ambiente.idAmbiente = :idAmbiente AND "
			+ "cp.ambiente.usuario.idUsuario = :idUsuario ")
	public Optional<ConjuntoPropriedadesAtual> retornaPropsAtuaisPorAmbientePorUsuario(@Param("idUsuario") Long idUsuario,@Param("idAmbiente") Long idAmbiente) ;

}
