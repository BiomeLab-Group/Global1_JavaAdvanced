package com.BiomeLab.Record;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados utilizados para iniciar um novo teste e alterar as condições do ambiente")
public record AlterarCondicoesDTO(

        @Schema(
            description = "Nome do novo teste",
            example = "Teste de Alta Umidade"
        )
        @NotBlank
        String nomeTeste,

        @Schema(
            description = "Temperatura do ambiente",
            example = "25.5"
        )
        @NotNull
        BigDecimal temperatura,

        @Schema(
            description = "Umidade do ambiente",
            example = "80.0"
        )
        @NotNull
        BigDecimal umidade,

        @Schema(
            description = "Luminosidade do ambiente",
            example = "650.0"
        )
        @NotNull
        BigDecimal luminosidade,

        @Schema(
            description = "Gravidade do ambiente",
            example = "9.81"
        )
        BigDecimal gravidade,

        @Schema(
            description = "Pressão atmosférica do ambiente",
            example = "1013.25"
        )
        BigDecimal pressaoAtmosferica

) {}