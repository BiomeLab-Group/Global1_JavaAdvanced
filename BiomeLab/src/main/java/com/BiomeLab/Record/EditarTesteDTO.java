package com.BiomeLab.Record;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados utilizados para editar um teste")
public record EditarTesteDTO(

        @NotBlank
        @Size(max = 100)
        @Schema(
            description = "Nome do teste",
            example = "Teste de Alta Temperatura"
        )
        String nomeTeste,

        @Size(max = 4000)
        @Schema(
            description = "Observações registradas durante o teste",
            example = "Foi observado crescimento acelerado durante os primeiros dias."
        )
        String observacoesGerais,

        @Size(max = 4000)
        @Schema(
            description = "Conclusão final do teste",
            example = "A temperatura elevada aumentou a velocidade de germinação."
        )
        String conclusao

) {}