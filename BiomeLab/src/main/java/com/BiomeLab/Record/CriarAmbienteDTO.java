package com.BiomeLab.Record;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados necessários para criação de um novo ambiente")
public record CriarAmbienteDTO(

        @NotBlank
        @Schema(
            description = "Nome do ambiente",
            example = "Terrário Tropical"
        )
        String nomeAmbiente,

        @NotNull
        @Schema(
            description = "Temperatura inicial do ambiente",
            example = "25.5"
        )
        BigDecimal temperatura,

        @NotNull
        @Schema(
            description = "Umidade inicial do ambiente",
            example = "80.0"
        )
        BigDecimal umidade,

        @NotNull
        @Schema(
            description = "Luminosidade inicial do ambiente",
            example = "700.0"
        )
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