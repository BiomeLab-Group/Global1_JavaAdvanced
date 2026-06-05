package com.BiomeLab.Control;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.Model.Teste;
import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Repository.TesteRepository;
import com.BiomeLab.Security.UsuarioAutenticado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
	    name = "Testes",
	    description = "Operações relacionadas aos testes"
	)
@RestController
@RequestMapping(value = "/teste")
public class TesteController {

    @Autowired
    private TesteRepository repTeste;

//    @GetMapping(value = "/todos")
//    public ResponseEntity<List<Teste>> retornarTodosTestes() {
//
//        List<Teste> testes = repTeste.findAll();
//
//        return ResponseEntity.ok(testes);
//    }

//    @GetMapping(value = "/{idTeste}")
//    public ResponseEntity<Teste> retornarTestePorId(
//            @PathVariable Long idTeste) {
//
//        Optional<Teste> op = repTeste.findById(idTeste);
//
//        if (op.isPresent()) {
//            return ResponseEntity.ok(op.get());
//        }
//
//        return ResponseEntity.notFound().build();
//    }
    
    @Operation(summary = "Retorna os testes de um estudo por ambiente do usuário autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de testes retornada com sucesso")
    })
    @GetMapping("/estudo/{idEstudo}/ambiente/{idAmbiente}")
    public ResponseEntity<List<Teste>> retornarTestesPorEstudoPorAmbientePorUsuario(
            @Parameter(description = "Identificador do estudo", example = "1") @PathVariable Long idEstudo,
            @Parameter(description = "Identificador do ambiente", example = "1") @PathVariable Long idAmbiente) {

        UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        Usuario usuario = auth.getUsuario();

        List<Teste> testes = repTeste.retornarTestesPorEstudoPorAmbientePorUsuario(idEstudo, idAmbiente, usuario.getIdUsuario());
        return ResponseEntity.ok(testes);
    }
    
    

//    @PostMapping(value = "/criar-teste")
//    public ResponseEntity<Void> criarTeste(
//            @RequestBody @Valid Teste teste) {
//
//        repTeste.save(teste);
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @Operation(
    	    summary = "Edita um teste",
    	    description = "O teste deve pertencer ao usuário autenticado"
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "204", description = "Teste atualizado com sucesso"),
    	    @ApiResponse(responseCode = "403", description = "Teste não pertence ao usuário"),
    	    @ApiResponse(responseCode = "404", description = "Teste não encontrado")
    	})
	@PutMapping("/editar-teste/{idTeste}")
	public ResponseEntity<Void> editarTeste(
	        @Parameter(description = "Identificador do teste", example = "1")
	        @PathVariable Long idTeste,
	        @RequestBody @Valid Teste testeAtualizado) {

	    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
	            .getContext().getAuthentication().getPrincipal();
	    Usuario usuario = auth.getUsuario();

	    Optional<Teste> op = repTeste.findById(idTeste);
	    if (op.isEmpty()) return ResponseEntity.notFound().build();

	    Teste teste = op.get();

	    if (!teste.getEstudo().getAmbiente().getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	    }

	    teste.transferirTeste(testeAtualizado);
	    repTeste.save(teste);

	    return ResponseEntity.noContent().build();
	}

    @Operation(
    	    summary = "Remove um teste",
    	    description = "O teste deve pertencer ao usuário autenticado"
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "204", description = "Teste removido com sucesso"),
    	    @ApiResponse(responseCode = "403", description = "Teste não pertence ao usuário"),
    	    @ApiResponse(responseCode = "404", description = "Teste não encontrado")
    	})
	@DeleteMapping("/remover-teste/{idTeste}")
	public ResponseEntity<Void> removerTeste(
	        @Parameter(description = "Identificador do teste", example = "1")
	        @PathVariable Long idTeste) {

	    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
	            .getContext().getAuthentication().getPrincipal();
	    Usuario usuario = auth.getUsuario();

	    Optional<Teste> op = repTeste.findById(idTeste);
	    if (op.isEmpty()) return ResponseEntity.notFound().build();

	    Teste teste = op.get();

	    if (!teste.getEstudo().getAmbiente().getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	    }

	    repTeste.deleteById(idTeste);
	    return ResponseEntity.noContent().build();
	}
}