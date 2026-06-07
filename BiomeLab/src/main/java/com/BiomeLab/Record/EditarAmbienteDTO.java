package com.BiomeLab.Record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados utilizados para editar um ambiente")
public record EditarAmbienteDTO(

        @NotBlank
        @Schema(
            description = "Novo nome do ambiente",
            example = "Terrário Tropical Atualizado"
        )
        String nomeAmbiente

) {}