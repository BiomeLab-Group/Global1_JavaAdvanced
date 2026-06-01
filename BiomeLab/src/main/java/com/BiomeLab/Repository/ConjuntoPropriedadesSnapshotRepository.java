package com.BiomeLab.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.BiomeLab.Model.ConjuntoPropriedadesSnapshot;

public interface ConjuntoPropriedadesSnapshotRepository extends JpaRepository<ConjuntoPropriedadesSnapshot, Long> {
	

	@Query(value = "FROM ConjuntoPropriedadesSnapshot cp "
	        + "WHERE cp.teste.idTeste = :idTeste "
	        + "AND cp.teste.estudo.idEstudo = :idEstudo "
	        + "AND cp.teste.estudo.ambiente.idAmbiente = :idAmbiente "
	        + "AND cp.teste.estudo.ambiente.usuario.idUsuario = :idUsuario ")
	public Optional<ConjuntoPropriedadesSnapshot> retornaPropsSnapshotPorTesteEEstudoEAmbienteEUsuario(
	        @Param("idTeste") Long idTeste,
	        @Param("idEstudo") Long idEstudo,
	        @Param("idAmbiente") Long idAmbiente,
	        @Param("idUsuario") Long idUsuario
	);
	
}
