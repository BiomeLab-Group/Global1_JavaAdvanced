package com.BiomeLab.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.BiomeLab.Model.Ambiente;
import com.BiomeLab.Model.AmbienteLocalidade;
import com.BiomeLab.Model.ChaveComposta_AmbienteLocalidade;

public interface AmbienteLocalidadeRepository extends JpaRepository<AmbienteLocalidade, ChaveComposta_AmbienteLocalidade>{

	
	//----------------------------------------------------------------------------------
	//---------------------- PESQUISA DE TODOS AMBIENTES (PUBLICOS E PRIVADOS(de acordo com o id do usuario))
	
	//------ POR SUBSTRING // adicionar campo de nome do ambiente
	@Query(
		    nativeQuery = true,
		    value = "SELECT DISTINCT a.* FROM T_BIOMELAB_AMBIENTE a "
		          + "WHERE UPPER(a.nm_ambiente) LIKE UPPER('%'||:substring||'%') "
		          + "AND (a.fk_usuario = :idUsuario OR a.fk_usuario IS NULL) "
		          + "UNION "
		          + "SELECT DISTINCT a.* FROM AMBIENTE_LOCALIDADE al "
		          + "INNER JOIN T_BIOMELAB_AMBIENTE a ON al.fk_ambiente = a.id_ambiente "
		          + "INNER JOIN T_BIOMELAB_LOCALIDADE l ON al.fk_localidade = l.id_localidade "
		          + "WHERE ( "
		          + "    UPPER(l.nm_planeta) LIKE UPPER('%'||:substring||'%') "
		          + " OR UPPER(l.continente) LIKE UPPER('%'||:substring||'%') "
		          + " OR UPPER(l.pais) LIKE UPPER('%'||:substring||'%') "
		          + ") "
		          + "AND (a.fk_usuario = :idUsuario OR a.fk_usuario IS NULL) "
		          + "ORDER BY 1 ASC"
		)
		List<Ambiente> buscarTodosAmbientesPorSubstring(
		    @Param("substring") String substring,
		    @Param("idUsuario") Long idUsuario
		);
	
	

	//------ MAPEADO POR CAMPOS DE LOCALIDADE
	@Query(
		    nativeQuery = true,
		    value = "SELECT DISTINCT a.* "
		          + "FROM AMBIENTE_LOCALIDADE al "
		          + "INNER JOIN T_BIOMELAB_AMBIENTE a ON al.fk_ambiente = a.id_ambiente "
		          + "INNER JOIN T_BIOMELAB_LOCALIDADE l ON al.fk_localidade = l.id_localidade "
		          + "WHERE ("
		          + "	 (UPPER(l.nm_planeta) = UPPER(:planeta) OR :planeta IS NULL) "
		          + "AND (UPPER(l.continente) = UPPER(:continente) OR :continente IS NULL) "
		          + "AND (UPPER(l.pais) = UPPER(:pais) OR :pais IS NULL) "
		          + ") AND ("
		          + "    a.fk_usuario = :idUsuario "
		          + " OR a.fk_usuario IS NULL "
		          + ") "
		          + "ORDER BY a.nm_ambiente ASC"
		)
		public List<Ambiente> listarTodosAmbientesPorLocalidadeMapeadaPorUsuario(
		    @Param("planeta") Optional<String> planeta,
		    @Param("continente") Optional<String> continente,
		    @Param("pais") Optional<String> pais,
		    @Param("idUsuario") Long idUsuario
		);
	
	
	//----------------------------------------- PESQUISA DE TODOS AMBIENTES PÚBLICOS
	
	//----- POR SUBSTRING
	@Query(
		    nativeQuery = true,
		    value = "SELECT DISTINCT a.* FROM T_BIOMELAB_AMBIENTE a "
		          + "WHERE UPPER(a.nm_ambiente) LIKE UPPER('%'||:substring||'%') "
		          + "AND a.fk_usuario IS NULL "
		          + "UNION "
		          + "SELECT DISTINCT a.* FROM AMBIENTE_LOCALIDADE al "
		          + "INNER JOIN T_BIOMELAB_AMBIENTE a ON al.fk_ambiente = a.id_ambiente "
		          + "INNER JOIN T_BIOMELAB_LOCALIDADE l ON al.fk_localidade = l.id_localidade "
		          + "WHERE ( "
		          + "    UPPER(l.nm_planeta) LIKE UPPER('%'||:substring||'%') "
		          + " OR UPPER(l.continente) LIKE UPPER('%'||:substring||'%') "
		          + " OR UPPER(l.pais) LIKE UPPER('%'||:substring||'%') "
		          + ") "
		          + "AND a.fk_usuario IS NULL "
		          + "ORDER BY 1 ASC"
		)
		List<Ambiente> buscarAmbientesPublicosPorSubstring(
		    @Param("substring") String substring
		);
	
	//Desligado
	//------ MAPEADO POR CAMPOS DE LOCALIDADE
		@Query(
			    nativeQuery = true,
			    value = "SELECT DISTINCT a.* "
			          + "FROM AMBIENTE_LOCALIDADE al "
			          + "INNER JOIN T_BIOMELAB_AMBIENTE a ON al.fk_ambiente = a.id_ambiente "
			          + "INNER JOIN T_BIOMELAB_LOCALIDADE l ON al.fk_localidade = l.id_localidade "
			          + "WHERE "
			          + "	 (UPPER(l.nm_planeta) = UPPER(:planeta) OR :planeta IS NULL) "
			          + "AND (UPPER(l.continente) = UPPER(:continente) OR :continente IS NULL) "
			          + "AND (UPPER(l.pais) = UPPER(:pais) OR :pais IS NULL) "
			          + "AND "
			          + "a.fk_usuario IS NULL "/*Ambientes publicos*/
			          + "ORDER BY a.nm_ambiente ASC"
			)
			public List<Ambiente> listarAmbientesPublicosPorLocalidadeMapeada(
				    @Param("planeta") Optional<String> planeta,
				    @Param("continente") Optional<String> continente,
				    @Param("pais") Optional<String> pais
			);
	
	
	
	
	
	//---------------------- PESQUISA DE TODOS AMBIENTES PRIVADOS
	
		// ambientes privados não tem localidade 
	
	
	
}









