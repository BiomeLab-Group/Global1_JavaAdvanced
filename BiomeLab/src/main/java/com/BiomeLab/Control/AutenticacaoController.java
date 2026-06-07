package com.BiomeLab.Control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BiomeLab.Record.AutenticarUsuarioEntradaDTO;
import com.BiomeLab.Record.AutenticarUsuarioSaidaDTO;
import com.BiomeLab.Security.JWTUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
	
	
	@Operation(
		    summary = "Autentica um usuário",
		    description = """
		        Realiza a autenticação utilizando e-mail e senha.

		        Em caso de sucesso, retorna um token JWT
		        que deverá ser utilizado nas demais requisições
		        protegidas da aplicação.
		        """
		)
		@ApiResponses({
		    @ApiResponse(
		        responseCode = "200",
		        description = "Autenticação realizada com sucesso"
		    ),
		    @ApiResponse(
		        responseCode = "401",
		        description = "E-mail ou senha inválidos"
		    )
		})
	@PostMapping("/login")
	public ResponseEntity<AutenticarUsuarioSaidaDTO> login(
	        @RequestBody AutenticarUsuarioEntradaDTO dto) {

	    try {

	        var autenticacao =
	                new UsernamePasswordAuthenticationToken(
	                        dto.email(),
	                        dto.senha()
	                );

	        var auth = manager.authenticate(autenticacao);

	        String token = jwtUtil.gerarToken(dto.email(), 10);

	        return ResponseEntity.ok(
	                new AutenticarUsuarioSaidaDTO(token)
	        );

	    } catch (Exception e) {
	        return ResponseEntity.status(401).build();
	    }
	}

}
