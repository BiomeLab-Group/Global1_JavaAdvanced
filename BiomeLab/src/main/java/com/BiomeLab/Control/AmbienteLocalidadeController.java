package com.BiomeLab.Control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.Mapper.AmbienteMapper;
import com.BiomeLab.Model.Ambiente;
import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Record.AmbienteCardDTO;
import com.BiomeLab.Repository.AmbienteLocalidadeRepository;
import com.BiomeLab.Security.UsuarioAutenticado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value = "/ambiente")
public class AmbienteLocalidadeController {
	
	@Autowired
	private AmbienteLocalidadeRepository repAmbLoc;
	
	@Autowired
	private AmbienteMapper mapper; 
	
//	//Desligado
//	@GetMapping(value="/{idUsuario}/localidades")
//	public ResponseEntity<List<Ambiente>> buscarTodosAmbientesPorLocalidadeMapeadaPorUsuario (
//			@RequestParam(name = "planeta",required = false) Optional<String> planeta,
//			@RequestParam(name = "continente",required = false) Optional<String> continente,
//			@RequestParam(name = "pais",required = false) Optional<String> pais,
//			@PathVariable Long idUsuario
//			)
//	{	
//		
//		List<Ambiente> listaAmbientes = repAmbLoc.listarTodosAmbientesPorLocalidadeMapeadaPorUsuario(planeta, continente, pais,idUsuario);
//		
//		
//		return ResponseEntity.ok(listaAmbientes);
//	};
//	
	
	@Operation(
		    summary = "Busca todos os ambientes por substring",
		    description = "Retorna ambientes privados do usuário autenticado e públicos, filtrando por nome do ambiente ou campos de localidade (planeta, continente, país)"
		)
		@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Lista de ambientes retornada com sucesso")
		})
		@GetMapping("/todos/pesquisa")
		public ResponseEntity<List<AmbienteCardDTO>> buscarTodosAmbientesPorLocalidadePorSubstringPorUsuario(
		        @RequestParam(name = "substring", required = false, defaultValue = "") String substring) {

		    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
		            .getContext().getAuthentication().getPrincipal();

		    Usuario usuario = auth.getUsuario();

		    List<Ambiente> listaAmbientes =
		            repAmbLoc.buscarTodosAmbientesPorSubstring(
		                    substring,
		                    usuario.getIdUsuario()
		            );

		    List<AmbienteCardDTO> listaDTO = listaAmbientes.stream()
		            .map(mapper::toCardDTO)
		            .toList();

		    return ResponseEntity.ok(listaDTO);
		}
	
//	//Desligado
//	@GetMapping(value="/localidades")
//	public ResponseEntity<List<Ambiente>> buscarAmbientesPublicosPorLocalidadeMapeada (
//	        @RequestParam(name = "planeta", required = false) Optional<String> planeta,
//	        @RequestParam(name = "continente", required = false) Optional<String> continente,
//	        @RequestParam(name = "pais", required = false) Optional<String> pais
//	        )
//	{
//	    List<Ambiente> listaAmbientes = repAmbLoc.listarAmbientesPublicosPorLocalidadeMapeada(planeta, continente, pais);
//	    return ResponseEntity.ok(listaAmbientes);
//	}

	@Operation(
		    summary = "Busca ambientes públicos por substring",
		    description = "Retorna apenas ambientes públicos, filtrando por nome do ambiente ou campos de localidade (planeta, continente, país)"
		)
		@ApiResponses(value = {
		    @ApiResponse(responseCode = "200", description = "Lista de ambientes públicos retornada com sucesso")
		})
	@GetMapping("/publicos/pesquisa")
	public ResponseEntity<List<AmbienteCardDTO>> buscarAmbientesPublicosPorLocalidadePorSubstring(
	        @RequestParam(name = "substring", required = false, defaultValue = "") String substring) {

	    List<Ambiente> listaAmbientes =
	            repAmbLoc.buscarAmbientesPublicosPorSubstring(substring);

	    List<AmbienteCardDTO> listaDTO = listaAmbientes.stream()
	            .map(mapper::toCardDTO)
	            .toList();

	    return ResponseEntity.ok(listaDTO);
	}
	

}
