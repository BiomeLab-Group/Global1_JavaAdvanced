package com.BiomeLab.Record;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlterarCondicoesDTO(
		
		@NotBlank
		String nomeTeste,

        @NotNull
        BigDecimal temperatura,

        @NotNull
        BigDecimal umidade,

        @NotNull
        BigDecimal luminosidade,

        BigDecimal gravidade,

        BigDecimal pressaoAtmosferica

) {}