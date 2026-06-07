package com.BiomeLab.Record;

import com.BiomeLab.Model.StatusAtivoEnum;
import com.BiomeLab.Model.VisibilidadeEnum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações resumidas de um ambiente exibidas em listagens")
public record AmbienteCardDTO(

        @Schema(
            description = "Identificador do ambiente",
            example = "1"
        )
        Long idAmbiente,

        @Schema(
            description = "Nome do ambiente",
            example = "Floresta Amazônica"
        )
        String nomeAmbiente,

        @Schema(
            description = "Visibilidade do ambiente (P = Público, R = Privado)",
            example = "P"
        )
        VisibilidadeEnum visibilidade,

        @Schema(
            description = "Status do ambiente (ATIVO ou INATIVO)",
            example = "ATIVO"
        )
        StatusAtivoEnum statusAtivo

) {}