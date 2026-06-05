package com.BiomeLab.Control;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.DTO.EstudoDTO;
import com.BiomeLab.Mapper.EstudoMapper;
import com.BiomeLab.Model.Ambiente;
import com.BiomeLab.Model.Estudo;
import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Record.EditarEstudoDTO;
import com.BiomeLab.Repository.AmbienteRepository;
import com.BiomeLab.Repository.EstudoRepository;
import com.BiomeLab.Security.UsuarioAutenticado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(
	    name = "Estudos",
	    description = "Operações relacionadas aos estudos"
	)
@RestController
@RequestMapping(value = "/estudo")
public class EstudoController {

    @Autowired
    private EstudoRepository repEstudo;
    
    @Autowired
    private AmbienteRepository repAmbiente;
    
    @Autowired
    private EstudoMapper mapper;

//    @GetMapping(value = "/todos")
//    public ResponseEntity<List<Estudo>> retornarTodosEstudos() {
//
//        List<Estudo> estudos = repEstudo.findAll();
//
//        return ResponseEntity.ok(estudos);
//    }

//    @GetMapping(value = "/{idEstudo}")
//    public ResponseEntity<Estudo> retornarEstudoPorId(
//            @PathVariable Long idEstudo) {
//
//        Optional<Estudo> op = repEstudo.findById(idEstudo);
//
//        if (op.isPresent()) {
//            return ResponseEntity.ok(op.get());
//        }
//
//        return ResponseEntity.notFound().build();
//    }

    @Operation(
    	    summary = "Retorna o estudo de um ambiente do usuário autenticado",
    	    description = "Utilizado na ficha de estudo"
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Estudo encontrado"),
    	    @ApiResponse(responseCode = "404", description = "Estudo não encontrado")
    	})
	@GetMapping("/ambiente/{idAmbiente}")
	public ResponseEntity<EstudoDTO> retornarEstudoPorAmbienteEUsuario(
	        @Parameter(description = "Identificador do ambiente", example = "1")
	        @PathVariable Long idAmbiente) {

	    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
	            .getContext().getAuthentication().getPrincipal();
	    Usuario usuario = auth.getUsuario();

	    Optional<Estudo> op = repEstudo.retornaEstudoPorAmbientePorUsuario(usuario.getIdUsuario(), idAmbiente);

	    if (op.isPresent()) return ResponseEntity.ok(mapper.toDTO(op.get()));
	    return ResponseEntity.notFound().build();
	}
    
    
    @Operation(
    	    summary = "Retorna o estudo do ambiente ativo do usuário autenticado",
    	    description = "Utilizado na tela Home"
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Estudo encontrado"),
    	    @ApiResponse(responseCode = "404", description = "Nenhum ambiente ativo encontrado")
    	})
    	@GetMapping("/ativo")
    	public ResponseEntity<Estudo> retornarEstudoDoAmbienteAtivoPorUsuario() {

    	    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
    	            .getContext().getAuthentication().getPrincipal();
    	    Usuario usuario = auth.getUsuario();

    	    Optional<Estudo> op = repEstudo.buscarEstudoDoAmbienteAtivo(usuario.getIdUsuario());

    	    if (op.isPresent()) return ResponseEntity.ok(op.get());
    	    return ResponseEntity.notFound().build();
    	}
    
    //
    @GetMapping("/estudo/{idEstudo}/ambiente")
    public ResponseEntity<Long> retornarIdAmbientePorEstudo(Long idEstudo){
    	
    	Optional<Estudo> op_estudo = repEstudo.findById(idEstudo);
    	if (op_estudo.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
    	Estudo estudo = op_estudo.get();
    	return ResponseEntity.ok(estudo.getAmbiente().getIdAmbiente());
    }
    

//    @PostMapping(value = "/criar-estudo")
//    public ResponseEntity<Void> criarEstudo(
//            @RequestBody @Valid Estudo estudo) {
//
//        repEstudo.save(estudo);
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
    
    @Operation(summary = "Retorna todos os estudos do usuário autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de estudos retornada com sucesso")
    })
    @GetMapping("/meus-estudos")
    public ResponseEntity<List<Estudo>> retornarEstudosPorUsuario() {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Usuario usuario = auth.getUsuario();

        List<Estudo> estudos = repEstudo.buscarEstudosPorUsuario(usuario.getIdUsuario());
        return ResponseEntity.ok(estudos);
    }

    
    @Operation(
    	    summary = "Edita um estudo",
    	    description = "O ambiente deve existir e pertencer ao usuário autenticado"
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "204", description = "Estudo atualizado com sucesso"),
    	    @ApiResponse(responseCode = "403", description = "Ambiente não pertence ao usuário"),
    	    @ApiResponse(responseCode = "404", description = "Ambiente ou estudo não encontrado")
    	})
    	@PutMapping("/ambiente/{idAmbiente}/estudo/{idEstudo}")
    	public ResponseEntity<Void> editarEstudo(
    	        @Parameter(description = "Identificador do ambiente", example = "1") @PathVariable Long idAmbiente,
    	        @Parameter(description = "Identificador do estudo", example = "1") @PathVariable Long idEstudo,
    	        @RequestBody @Valid EditarEstudoDTO estudoDTO) {

    	    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
    	            .getContext().getAuthentication().getPrincipal();
    	    Usuario usuario = auth.getUsuario();

    	    Optional<Ambiente> op_ambiente = repAmbiente.findById(idAmbiente);
    	    if (op_ambiente.isEmpty()) return ResponseEntity.notFound().build();

    	    Ambiente ambiente = op_ambiente.get();
    	    if (!ambiente.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
    	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    	    }

    	    Optional<Estudo> op_estudo = repEstudo.findById(idEstudo);
    	    if (op_estudo.isEmpty()) return ResponseEntity.notFound().build();

    	    Estudo estudo = op_estudo.get();
    	    estudo.setNomeEstudo(estudoDTO.nomeEstudo());
    	    estudo.setDescricaoEstudo(estudoDTO.descricaoEstudo());

    	    repEstudo.save(estudo);

    	    return ResponseEntity.noContent().build();
    	}

    
//    @DeleteMapping(value = "/remover-estudo/{idEstudo}")
//    public ResponseEntity<Void> removerEstudo(
//            @PathVariable Long idEstudo) {
//
//        Optional<Estudo> op = repEstudo.findById(idEstudo);
//
//        if (op.isPresent()) {
//
//            repEstudo.deleteById(idEstudo);
//
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.notFound().build();
//    }

}