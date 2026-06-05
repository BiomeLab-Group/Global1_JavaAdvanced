package com.BiomeLab.Control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.DTO.UsuarioDTO;
import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Record.CriarUsuarioDTO;
import com.BiomeLab.Record.EditarUsuarioDTO;
import com.BiomeLab.Repository.UsuarioRepository;
import com.BiomeLab.Security.UsuarioAutenticado;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
	    name = "Usuários",
	    description = "Operações relacionadas aos usuários"
	)
@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository repUsuario;
	
//	@Autowired
//	private UsuarioMapper mapper;
	
	
	@Operation(summary = "Retorna todos os usuários", description = "Apenas para testes")
	@ApiResponses(@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"))
	@GetMapping("/todos")
	public ResponseEntity<List<Usuario>> retornarTodosUsuarios() {
	    return ResponseEntity.ok(repUsuario.findAll());
	}
	
//	@GetMapping(value = "/{idUsuario}")
//	public ResponseEntity<Usuario> retornarUsuarioPorId(@PathVariable Long idUsuario){
//		
//		Optional<Usuario> op = repUsuario.findById(idUsuario);
//		
//		if (op.isPresent()) {
//			return ResponseEntity.ok(op.get());
//		}
//		
//		return ResponseEntity.notFound().build();
//	}
//	
	@Operation(summary = "Retorna dados pessoais do usuário autenticado")
	@ApiResponses({
	    @ApiResponse(responseCode = "200", description = "Dados retornados com sucesso"),
	    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	@Tag(name = "Teste em Cloud")
	@GetMapping("/dados-pessoais")
	public ResponseEntity<UsuarioDTO> retornarDadosUsuario() {

	    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
	            .getContext().getAuthentication().getPrincipal();
	    Usuario usuario = auth.getUsuario();

	    UsuarioDTO dto = new UsuarioDTO(
	            usuario.getNomeUsuario(),
	            usuario.getDataNascimento(),
	            usuario.getEmail()
	    );

	    return ResponseEntity.ok(dto);
	}
	
	
	
	
	@Operation(summary = "Cria um novo usuário")
	@ApiResponses({
	    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
	})
	@Tag(name = "Teste em Cloud")
	@PostMapping("/criar-usuario")
	public ResponseEntity<Void> criarUsuario(@RequestBody @Valid CriarUsuarioDTO dto) {

	    Usuario usuario = Usuario.builder()
	            .nomeUsuario(dto.nomeUsuario())
	            .dataNascimento(dto.dataNascimento())
	            .email(dto.email())
	            .senha(dto.senha())
	            .build();

	    repUsuario.save(usuario);
	    return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// O usuario Pode apenas modificar nome e email 
	@Operation(summary = "Edita nome e email do usuário autenticado")
	@ApiResponses({
	    @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso"),
	    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	@Tag(name = "Teste em Cloud")
	@PutMapping("/editar-usuario")
	public ResponseEntity<Void> editarUsuario(@RequestBody @Valid EditarUsuarioDTO dto) {

	    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
	            .getContext().getAuthentication().getPrincipal();
	    Usuario usuario = auth.getUsuario();
	   
	    usuario.setNomeUsuario(dto.nomeUsuario());
	    repUsuario.save(usuario);

	    return ResponseEntity.noContent().build();
	}
	
	
	
//	// Apenas para testes
//	@PutMapping(value = "/editar-usuario-completo/{idUsuario}")
//	public ResponseEntity<Void> editarUsuarioCompleto(
//	        @PathVariable Long idUsuario,
//	        @RequestBody @Valid Usuario usuarioAtualizado){
//
//	    Optional<Usuario> op = repUsuario.findById(idUsuario);
//
//	    if (op.isPresent()) {
//	        Usuario usuario = op.get();
//	        usuario.transferirUsuario(usuarioAtualizado);
//	        repUsuario.save(usuario);
//	        return ResponseEntity.noContent().build();
//	    }
//	    return ResponseEntity.notFound().build();
//	}
	
	
	// Apenas para cloud
	@Operation(summary = "Remove o usuário autenticado")
	@ApiResponses({
	    @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
	    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	@Tag(name = "Teste em Cloud")
	@DeleteMapping("/remover-usuario")
	public ResponseEntity<Void> removerUsuario() {
	
	    UsuarioAutenticado auth = (UsuarioAutenticado) SecurityContextHolder
	            .getContext().getAuthentication().getPrincipal();
	    Usuario usuario = auth.getUsuario();
	
	    repUsuario.deleteById(usuario.getIdUsuario());
	    return ResponseEntity.noContent().build();
	}

}
