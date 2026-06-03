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
	
	
	
	// VALIDAÇÃO DENTRO DA FICHA de Ambiente
	@Query("""
		    FROM Ambiente a
		    WHERE a.usuario.idUsuario = :idUsuario
		    AND a.idAmbiente = :idAmbiente
		    AND a.statusAtivo = 'ATIVO'
		""")
		Optional<Ambiente> buscarAmbienteAtivo(
		    @Param("idUsuario") Long idUsuario,
		    @Param("idAmbiente") Long idAmbiente
		);
	
	
	// USADO NA HOME
	@Query("""
		    FROM Ambiente a
		    WHERE a.usuario.idUsuario = :idUsuario
		    AND a.statusAtivo = 'ATIVO'
		""")
		Optional<Ambiente> buscarAmbienteAtivoHome(
		    @Param("idUsuario") Long idUsuario
		);
	
	//USADO NA FICHA DE AMBIENTE
	@Query("""
		    FROM Ambiente a
		    WHERE a.usuario.idUsuario = :idUsuario
		    AND a.idAmbiente = :idAmbiente
		    AND a.visibilidade = 'R'
		    AND a.statusAtivo = 'INATIVO'
		""")
		Optional<Ambiente> ativarAmbiente(
		    @Param("idUsuario") Long idUsuario,
		    @Param("idAmbiente") Long idAmbiente
		);
	
	
	@Query(
		    nativeQuery = true,
		    value = "SELECT DISTINCT a.* FROM T_BIOMELAB_AMBIENTE a "
		          + "WHERE UPPER(a.nm_ambiente) LIKE UPPER('%'||:substring||'%') "
		          + "AND a.fk_usuario = :idUsuario "
		          + "AND a.st_visibilidade = 'R' "
		          + "ORDER BY a.nm_ambiente ASC"
		)
		List<Ambiente> buscarAmbientesPrivadosPorSubstring(
		    @Param("substring") String substring,
		    @Param("idUsuario") Long idUsuario
		);
	
	@Query("""
		    FROM Ambiente a
		    WHERE a.usuario.idUsuario = :idUsuario
		    AND a.statusAtivo = 'ATIVO'
		""")
		Optional<Ambiente> retornaAmbienteAtivo(
		    @Param("idUsuario") Long idUsuario
		);
}
