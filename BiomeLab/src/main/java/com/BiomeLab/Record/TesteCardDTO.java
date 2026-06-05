package com.BiomeLab.Record;

import java.time.LocalDate;

public record TesteCardDTO(

        Long idTeste,
        String nomeTeste,
        LocalDate dataInicioTeste,
        LocalDate dataTerminoTeste

) {}