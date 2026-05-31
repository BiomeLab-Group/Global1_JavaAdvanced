package com.BiomeLab.Control;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository repUsuario;
	
//	@Autowired
//	private UsuarioMapper mapper;
	
	
	@GetMapping(value = "/todos")
	public ResponseEntity<List<Usuario>> retornarTodosUsuarios (){
		
		List<Usuario> usuarios = repUsuario.findAll();
		
		
		return ResponseEntity.ok(usuarios);
	}
	
	@GetMapping(value = "/{idUsuario}")
	public ResponseEntity<Usuario> retornarUsuarioPorId(@PathVariable Long idUsuario){
		
		Optional<Usuario> op = repUsuario.findById(idUsuario);
		
		if (op.isPresent()) {
			return ResponseEntity.ok(op.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	
	@PostMapping(value = "/criar-usuario")
	public ResponseEntity<Void> criarUsuario(@RequestBody @Valid Usuario usuario){
		
		
		repUsuario.save(usuario);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	@PutMapping(value = "/editar-usuario/{idUsuario}")
	public ResponseEntity<Void> editarUsuario(@PathVariable Long idUsuario,@RequestBody @Valid Usuario usuarioAtualizado){
		
		Optional<Usuario> op = repUsuario.findById(idUsuario);
		
		if (op.isPresent()) {
			Usuario usuario = op.get();
			
			usuario.transferirUsuario(usuarioAtualizado);
			repUsuario.save(usuario);
			
			return ResponseEntity.noContent().build();
			
		}else {
		
			return ResponseEntity.notFound().build();
		}
	}
	
	
	@DeleteMapping(value = "/remover-usuario/{idUsuario}")
	public ResponseEntity<Void> removerUsuario(@PathVariable Long idUsuario){
		
		Optional<Usuario> op = repUsuario.findById(idUsuario);
		
		if (op.isPresent()) {
			repUsuario.deleteById(idUsuario);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

}
