package com.BiomeLab.Control;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.Model.Ambiente;
import com.BiomeLab.Repository.AmbienteLocalidadeRepository;

@RestController
@RequestMapping(value = "/ambientes")
public class AmbienteLocalidadeController {
	
	@Autowired
	AmbienteLocalidadeRepository repAmbLoc;
	
	
	@GetMapping(value="/{idUsuario}/localidades")
	public ResponseEntity<List<Ambiente>> buscarTodosAmbientesPorLocalidadeMapeadaPorUsuario (
			@RequestParam(name = "planeta",required = false) Optional<String> planeta,
			@RequestParam(name = "continente",required = false) Optional<String> continente,
			@RequestParam(name = "pais",required = false) Optional<String> pais,
			@PathVariable Long idUsuario
			)
	{	
		
		List<Ambiente> listaAmbientes = repAmbLoc.listarTodosAmbientesPorLocalidadeMapeadaPorUsuario(planeta, continente, pais,idUsuario);
		
		
		return ResponseEntity.ok(listaAmbientes);
	};
	
	
	@GetMapping(value="/{idUsuario}/localidades/pesquisa")
	public ResponseEntity<List<Ambiente>> buscarTodosAmbientesPorLocalidadePorSubstringPorUsuario (
			@RequestParam(name = "substring",required = false,defaultValue = "") String substring,

			@PathVariable Long idUsuario
			)
	{
		

		
		
		List<Ambiente> listaAmbientes = repAmbLoc.listarTodosAmbientesPorLocalidadePorSubstringPorUsuario(substring,idUsuario);
		
		
		return ResponseEntity.ok(listaAmbientes);
	};
	
	
	@GetMapping(value="/localidades")
	public ResponseEntity<List<Ambiente>> buscarAmbientesPublicosPorLocalidadeMapeada (
	        @RequestParam(name = "planeta", required = false) Optional<String> planeta,
	        @RequestParam(name = "continente", required = false) Optional<String> continente,
	        @RequestParam(name = "pais", required = false) Optional<String> pais
	        )
	{
	    List<Ambiente> listaAmbientes = repAmbLoc.listarAmbientesPublicosPorLocalidadeMapeada(planeta, continente, pais);
	    return ResponseEntity.ok(listaAmbientes);
	}

	
	@GetMapping(value="/localidades/pesquisa")
	public ResponseEntity<List<Ambiente>> buscarAmbientesPublicosPorLocalidadePorSubstring (
	        @RequestParam(name = "substring", required = false, defaultValue = "") String substring
	        )
	{
	    List<Ambiente> listaAmbientes = repAmbLoc.listarAmbientesPublicosPorLocalidadePorSubstring(substring);
	    return ResponseEntity.ok(listaAmbientes);
	}
	

}
