package com.BiomeLab.Record;

import jakarta.validation.constraints.NotBlank;

public record EditarEstudoDTO(
	    @NotBlank
	    String nomeEstudo,
	    String descricaoEstudo
	) {}