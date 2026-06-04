package com.BiomeLab.Control;

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

import com.BiomeLab.Model.Ambiente;
import com.BiomeLab.Model.ConjuntoPropriedadesAtual;
import com.BiomeLab.Model.StatusAtivoEnum;
import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Repository.AmbienteRepository;
import com.BiomeLab.Repository.ConjuntoPropriedadesAtualRepository;
import com.BiomeLab.Security.UsuarioAutenticado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/conjunto-propriedades-atual")
public class ConjuntoPropriedadesAtualController {

    @Autowired
    private ConjuntoPropriedadesAtualRepository repConjuntoPropriedadesAtual;
    
    @Autowired
    private AmbienteRepository repAmbiente;

//    @GetMapping(value = "/todos")
//    public ResponseEntity<List<ConjuntoPropriedadesAtual>> retornarTodos() {
//
//        List<ConjuntoPropriedadesAtual> conjuntos =
//                repConjuntoPropriedadesAtual.findAll();
//
//        return ResponseEntity.ok(conjuntos);
//    }

    
//    @GetMapping(value = "/{idConjunto}")
//    public ResponseEntity<ConjuntoPropriedadesAtual> retornarPorId(
//            @PathVariable Long idConjunto) {
//
//        Optional<ConjuntoPropriedadesAtual> op =
//                repConjuntoPropriedadesAtual.findById(idConjunto);
//
//        if (op.isPresent()) {
//            return ResponseEntity.ok(op.get());
//        }
//
//        return ResponseEntity.notFound().build();
//    }
    
    
    @Operation(summary = "Retorna o conjunto de propriedades atuais de um ambiente do usuário autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conjunto de propriedades encontrado"),
        @ApiResponse(responseCode = "404", description = "Conjunto de propriedades não encontrado")
    })
    @GetMapping("/ambiente/{idAmbiente}")
    public ResponseEntity<ConjuntoPropriedadesAtual> retornarConjuntoPorAmbienteEUsuario(
            @Parameter(description = "Identificador do ambiente", example = "1")
            @PathVariable Long idAmbiente) {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Usuario usuario = auth.getUsuario();

        Optional<ConjuntoPropriedadesAtual> op = repConjuntoPropriedadesAtual
                .retornaPropsAtuaisPorAmbientePorUsuario(usuario.getIdUsuario(), idAmbiente);

        if (op.isPresent()) return ResponseEntity.ok(op.get());
        return ResponseEntity.notFound().build();
    }
    
    
//    @PostMapping(value = "/criar")
//    public ResponseEntity<Void> criar(
//            @RequestBody @Valid ConjuntoPropriedadesAtual conjunto) {
//
//        repConjuntoPropriedadesAtual.save(conjunto);
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    
    @Operation(
    	    summary = "Edita o conjunto de propriedades atuais de um ambiente",
    	    description = "O ambiente deve existir, pertencer ao usuário autenticado e estar ativo"
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "204", description = "Propriedades atualizadas com sucesso"),
    	    @ApiResponse(responseCode = "400", description = "Ambiente não está ativo"),
    	    @ApiResponse(responseCode = "403", description = "Ambiente não pertence ao usuário"),
    	    @ApiResponse(responseCode = "404", description = "Ambiente ou conjunto de propriedades não encontrado")
    	})
    	@PutMapping("/ambiente/{idAmbiente}/conjunto-props-atual/{idConjuntoPropsAtual}")
    	public ResponseEntity<Void> editarConjuntoPropriedadesAtual(
    	        @Parameter(description = "Identificador do ambiente", example = "1")
    	        @PathVariable Long idAmbiente,
    	        @Parameter(description = "Identificador do conjunto de propriedades", example = "1")
    	        @PathVariable Long idConjuntoPropsAtual,
    	        @RequestBody @Valid ConjuntoPropriedadesAtual conjuntoAtualizado) {

    	    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
    	            .getContext().getAuthentication().getPrincipal();
    	    Usuario usuario = auth.getUsuario();

    	    Optional<Ambiente> op_ambiente = repAmbiente.findById(idAmbiente);
    	    if (op_ambiente.isEmpty()) return ResponseEntity.notFound().build();

    	    Ambiente ambiente = op_ambiente.get();
    	    if (!ambiente.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
    	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    	    }

    	    if (ambiente.getStatusAtivo() != StatusAtivoEnum.ATIVO) {
    	        return ResponseEntity.badRequest().build();
    	    }

    	    Optional<ConjuntoPropriedadesAtual> op_conjunto = repConjuntoPropriedadesAtual.findById(idConjuntoPropsAtual);
    	    if (op_conjunto.isEmpty()) return ResponseEntity.notFound().build();

    	    ConjuntoPropriedadesAtual conjunto = op_conjunto.get();
    	    conjunto.setTemperatura(conjuntoAtualizado.getTemperatura());
    	    conjunto.setUmidade(conjuntoAtualizado.getUmidade());
    	    conjunto.setLuminosidade(conjuntoAtualizado.getLuminosidade());
    	    conjunto.setGravidade(conjuntoAtualizado.getGravidade());
    	    conjunto.setPressaoAtmosferica(conjuntoAtualizado.getPressaoAtmosferica());
    	    repConjuntoPropriedadesAtual.save(conjunto);

    	    return ResponseEntity.noContent().build();
    	}

//    @DeleteMapping(value = "/remover/{idConjunto}")
//    public ResponseEntity<Void> remover(@PathVariable Long idConjunto) {
//
//        Optional<ConjuntoPropriedadesAtual> op =
//                repConjuntoPropriedadesAtual.findById(idConjunto);
//
//        if (op.isPresent()) {
//
//            repConjuntoPropriedadesAtual.deleteById(idConjunto);
//
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.notFound().build();
//    }

}