package com.BiomeLab.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.BiomeLab.Model.Teste;

public interface TesteRepository extends JpaRepository<Teste, Long>{
	
	
	@Query(value = "FROM Teste t "
	        + "WHERE t.estudo.idEstudo = :idEstudo "
	        + "AND t.estudo.ambiente.idAmbiente = :idAmbiente "
	        + "AND t.estudo.ambiente.usuario.idUsuario = :idUsuario ")
	public List<Teste> retornarTestesPorEstudoPorAmbientePorUsuario(
	        @Param("idEstudo") Long idEstudo,
	        @Param("idAmbiente") Long idAmbiente,
	        @Param("idUsuario") Long idUsuario
	);
	

}
