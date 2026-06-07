package com.BiomeLab.Record;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações resumidas de um teste")
public record TesteCardDTO(

        @Schema(
            description = "Identificador do teste",
            example = "1"
        )
        Long idTeste,

        @Schema(
            description = "Nome do teste",
            example = "Teste de Alta Umidade"
        )
        String nomeTeste,

        @Schema(
            description = "Data de início do teste",
            example = "2026-06-07"
        )
        LocalDate dataInicioTeste,

        @Schema(
            description = """
                Data de término do teste.
                Quando nula, indica que o teste ainda está em andamento.
                """,
            example = "2026-06-15"
        )
        LocalDate dataTerminoTeste

) {}