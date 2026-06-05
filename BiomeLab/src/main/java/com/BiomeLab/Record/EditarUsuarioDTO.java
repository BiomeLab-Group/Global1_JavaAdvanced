package com.BiomeLab.Record;

import jakarta.validation.constraints.NotBlank;

public record EditarUsuarioDTO(
	    @NotBlank
	    String nomeUsuario
	    
	) {}