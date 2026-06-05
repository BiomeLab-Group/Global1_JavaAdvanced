package com.BiomeLab.Record;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EditarUsuarioDTO(
	    @NotBlank
	    String nomeUsuario,
	    
	    @NotBlank
	    @Email
	    String email
	) {}