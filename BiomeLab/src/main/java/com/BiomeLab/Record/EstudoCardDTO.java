package com.BiomeLab.Record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações resumidas de um estudo")
public record EstudoCardDTO(

        @Schema(
            description = "Identificador do estudo",
            example = "1"
        )
        Long idEstudo,

        @Schema(
            description = "Nome do estudo",
            example = "Estudo da Germinação"
        )
        String nomeEstudo,

        @Schema(
            description = "Descrição resumida do estudo",
            example = "Análise do crescimento de plantas em diferentes condições ambientais."
        )
        String descricaoEstudo

) {}