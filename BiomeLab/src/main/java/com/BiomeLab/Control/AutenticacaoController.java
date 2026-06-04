package com.BiomeLab.Control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Record.AutenticarUsuarioEntradaDTO;
import com.BiomeLab.Record.AutenticarUsuarioSaidaDTO;
import com.BiomeLab.Repository.UsuarioRepository;
import com.BiomeLab.Security.JWTUtil;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
	    name = "Autenticação",
	    description = "Login e geração de token JWT"
	)
@RestController
@RequestMapping("/autenticacao")
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private UsuarioRepository repUsuario;
	
	@PostMapping("/login")
	public ResponseEntity<AutenticarUsuarioSaidaDTO> login(
	        @RequestBody AutenticarUsuarioEntradaDTO dto) {

	    try {

	        var autenticacao =
	                new UsernamePasswordAuthenticationToken(
	                        dto.email(),
	                        dto.senha());

	        manager.authenticate(autenticacao); //------------------------------- Se passar daqui, o usuario existe
	        
	        Usuario usuario = repUsuario.findByEmail(dto.email()).orElseThrow();

	        String token =
	                jwtUtil.gerarToken(dto.email(), 10);

	        return ResponseEntity.ok(
	                new AutenticarUsuarioSaidaDTO(
	                        token,
	                        usuario.getIdUsuario()
	                ));

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	}

}
