package com.BiomeLab.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.BiomeLab.Model.Ambiente;

public interface AmbienteRepository extends JpaRepository<Ambiente, Long>{
	
	
	@Query(value = "FROM Ambiente a WHERE a.usuario.idUsuario = :idUsuario AND a.visibilidade = 'R'")		
	public List<Ambiente> listarAmbientesPrivadosPorUsuario(@Param("idUsuario") Long idUsuario);
}
