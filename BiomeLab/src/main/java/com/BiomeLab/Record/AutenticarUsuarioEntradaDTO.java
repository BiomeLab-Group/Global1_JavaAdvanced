package com.BiomeLab.Record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais utilizadas para autenticação do usuário")
public record AutenticarUsuarioEntradaDTO(

        @Schema(
            description = "E-mail cadastrado do usuário",
            example = "eduardo@email.com"
        )
        String email,

        @Schema(
            description = "Senha do usuário",
            example = "123456"
        )
        String senha

) {}