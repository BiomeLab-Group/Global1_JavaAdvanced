package com.BiomeLab.Record;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditarTesteDTO(

        @NotBlank
        @Size(max = 100)
        String nomeTeste,

        @Size(max = 4000)
        String observacoesGerais,

        @Size(max = 4000)
        String conclusao

) {}