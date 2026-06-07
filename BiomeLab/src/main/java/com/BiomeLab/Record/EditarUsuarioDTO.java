package com.BiomeLab.Record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados utilizados para editar o perfil do usuário")
public record EditarUsuarioDTO(

        @NotBlank
        @Schema(
            description = "Nome de exibição do usuário",
            example = "Eduardo Locaspi"
        )
        String nomeUsuario

) {}