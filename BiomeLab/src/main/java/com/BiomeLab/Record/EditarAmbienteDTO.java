package com.BiomeLab.Record;

import jakarta.validation.constraints.NotBlank;

public record EditarAmbienteDTO(
		@NotBlank
	    String nomeAmbiente
	) {}