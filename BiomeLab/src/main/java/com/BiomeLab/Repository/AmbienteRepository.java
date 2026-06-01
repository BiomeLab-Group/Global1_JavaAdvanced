package com.BiomeLab.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.BiomeLab.Model.Ambiente;

public interface AmbienteRepository extends JpaRepository<Ambiente, Long>{
	
	
	@Query(value = "FROM Ambiente a WHERE a.usuario.idUsuario = :idUsuario AND a.visibilidade = 'R'")		
	public List<Ambiente> listarAmbientesPrivadosPorUsuario(@Param("idUsuario") Long idUsuario);
	
	
	@Query("""
		    FROM Ambiente a
		    WHERE a.usuario.idUsuario = :idUsuario
		    AND a.idAmbiente = :idAmbiente
		    AND a.statusAtivo = com.BiomeLab.Enum.StatusAtivoEnum.ATIVO
		""")
		Optional<Ambiente> buscarAmbienteAtivo(
		    @Param("idUsuario") Long idUsuario,
		    @Param("idAmbiente") Long idAmbiente
		);
	
	
	@Query("""
		    FROM Ambiente a
		    WHERE a.usuario.idUsuario = :idUsuario
		    AND a.idAmbiente = :idAmbiente
		    AND a.visibilidade = 'R'
		    AND a.statusAtivo = com.BiomeLab.Enum.StatusAtivoEnum.INATIVO
		""")
		Optional<Ambiente> ativarAmbiente(
		    @Param("idUsuario") Long idUsuario,
		    @Param("idAmbiente") Long idAmbiente
		);
	
	
	@Query("""
		    FROM Ambiente a
		    WHERE a.usuario.idUsuario = :idUsuario
		    AND a.statusAtivo = com.BiomeLab.Enum.StatusAtivoEnum.ATIVO
		""")
		Optional<Ambiente> retornaAmbienteAtivo(
		    @Param("idUsuario") Long idUsuario
		);
}
