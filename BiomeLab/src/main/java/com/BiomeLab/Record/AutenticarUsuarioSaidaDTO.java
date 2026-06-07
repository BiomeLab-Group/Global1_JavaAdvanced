package com.BiomeLab.Record;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token JWT gerado após autenticação")
public record AutenticarUsuarioSaidaDTO(

        @Schema(
            description = "Token JWT utilizado para autenticação nas requisições protegidas",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c3VhcmlvQGVtYWlsLmNvbSJ9.signature"
        )
        String token

) {}