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

import com.BiomeLab.Model.ConjuntoPropriedadesSnapshot;
import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Repository.ConjuntoPropriedadesSnapshotRepository;
import com.BiomeLab.Security.UsuarioAutenticado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/conjunto-propriedades-snapshot")
public class ConjuntoPropriedadesSnapshotController {

    @Autowired
    private ConjuntoPropriedadesSnapshotRepository repConjuntoPropriedadesSnapshot;

//    @GetMapping(value = "/todos")
//    public ResponseEntity<List<ConjuntoPropriedadesSnapshot>> retornarTodos() {
//
//        List<ConjuntoPropriedadesSnapshot> conjuntos =
//                repConjuntoPropriedadesSnapshot.findAll();
//
//        return ResponseEntity.ok(conjuntos);
//    }

//    @GetMapping(value = "/{idConjunto}")
//    public ResponseEntity<ConjuntoPropriedadesSnapshot> retornarPorId(
//            @PathVariable Long idConjunto) {
//
//        Optional<ConjuntoPropriedadesSnapshot> op =
//                repConjuntoPropriedadesSnapshot.findById(idConjunto);
//
//        if (op.isPresent()) {
//            return ResponseEntity.ok(op.get());
//        }
//
//        return ResponseEntity.notFound().build();
//    }

    @Operation(summary = "Retorna o snapshot de propriedades de um teste")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Snapshot encontrado"),
        @ApiResponse(responseCode = "404", description = "Snapshot não encontrado")
    })
    @GetMapping("/teste/{idTeste}/estudo/{idEstudo}/ambiente/{idAmbiente}")
    public ResponseEntity<ConjuntoPropriedadesSnapshot> retornaPropsSnapshotPorTesteEEstudoEAmbienteEUsuario(
            @Parameter(description = "Identificador do teste", example = "1") @PathVariable Long idTeste,
            @Parameter(description = "Identificador do estudo", example = "1") @PathVariable Long idEstudo,
            @Parameter(description = "Identificador do ambiente", example = "1") @PathVariable Long idAmbiente) {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Usuario usuario = auth.getUsuario();

        Optional<ConjuntoPropriedadesSnapshot> op = repConjuntoPropriedadesSnapshot
                .retornaPropsSnapshotPorTesteEEstudoEAmbienteEUsuario(idTeste, idEstudo, idAmbiente, usuario.getIdUsuario());

        if (op.isPresent()) return ResponseEntity.ok(op.get());
        return ResponseEntity.notFound().build();
    }
    
    
//    @PostMapping(value = "/criar")
//    public ResponseEntity<Void> criar(
//            @RequestBody @Valid ConjuntoPropriedadesSnapshot conjunto) {
//
//        repConjuntoPropriedadesSnapshot.save(conjunto);
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @Operation(
    	    summary = "Edita o conjunto de propriedades snapshot de um teste",
    	    description = "O snapshot deve pertencer ao usuário autenticado"
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "204", description = "Snapshot atualizado com sucesso"),
    	    @ApiResponse(responseCode = "403", description = "Snapshot não pertence ao usuário"),
    	    @ApiResponse(responseCode = "404", description = "Snapshot não encontrado")
    	})
    	@PutMapping("/editar/{idConjunto}")
    	public ResponseEntity<Void> editar(
    	        @Parameter(description = "Identificador do snapshot", example = "1")
    	        @PathVariable Long idConjunto,
    	        @RequestBody @Valid ConjuntoPropriedadesSnapshot conjuntoAtualizado) {

    	    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
    	            .getContext().getAuthentication().getPrincipal();
    	    Usuario usuario = auth.getUsuario();

    	    Optional<ConjuntoPropriedadesSnapshot> op = repConjuntoPropriedadesSnapshot.findById(idConjunto);
    	    if (op.isEmpty()) return ResponseEntity.notFound().build();

    	    ConjuntoPropriedadesSnapshot conjunto = op.get();

    	    if (!conjunto.getTeste().getEstudo().getAmbiente().getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
    	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    	    }

    	    conjunto.transferirConjuntoPropriedadesSnapshot(conjuntoAtualizado);
    	    repConjuntoPropriedadesSnapshot.save(conjunto);

    	    return ResponseEntity.noContent().build();
    	}

//    @DeleteMapping(value = "/remover/{idConjunto}")
//    public ResponseEntity<Void> remover(@PathVariable Long idConjunto) {
//
//        Optional<ConjuntoPropriedadesSnapshot> op =
//                repConjuntoPropriedadesSnapshot.findById(idConjunto);
//
//        if (op.isPresent()) {
//
//            repConjuntoPropriedadesSnapshot.deleteById(idConjunto);
//
//            return ResponseEntity.noContent().build();
//        }
//
//        return ResponseEntity.notFound().build();
//    }

}