package com.BiomeLab.Record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados utilizados para editar um estudo")
public record EditarEstudoDTO(

        @NotBlank
        @Schema(
            description = "Nome do estudo",
            example = "Estudo da Germinação de Feijões"
        )
        String nomeEstudo,

        @Schema(
            description = "Descrição do estudo",
            example = "Análise da influência da umidade no desenvolvimento inicial da planta."
        )
        String descricaoEstudo

) {}