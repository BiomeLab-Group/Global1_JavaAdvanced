package com.BiomeLab.Control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.BiomeLab.Model.Ambiente;
import com.BiomeLab.Repository.AmbienteLocalidadeRepository;

public class AmbienteLocalidadeController {
	
	@Autowired
	AmbienteLocalidadeRepository repAmbLoc;
	
	
	@GetMapping(value="ambientes/localidade")
	public ResponseEntity<List<Ambiente>> buscarPorLocalidadeMapeada (
			@RequestParam(name = "planeta",required = false) String planeta,
			@RequestParam(name = "continente",required = false) String continente,
			@RequestParam(name = "pais",required = false) String pais
			)
	{

		List<Ambiente> listaAmbientesPorLocalidade = repAmbLoc.listarAmbientesPorLocalidadeMapeada(planeta, continente, pais);
		
		
		return ResponseEntity.ok(listaAmbientesPorLocalidade);
	};
	
	
	
	
	

}
