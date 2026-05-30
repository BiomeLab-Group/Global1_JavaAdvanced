package com.BiomeLab.DTO;

import java.time.LocalDate;

import com.BiomeLab.Model.Estudo;

import lombok.Data;

@Data
public class TesteDTO {
	
	private String nomeTeste;

    private LocalDate dataInicioTeste;

    private LocalDate dataTerminoTeste;

    private String observacoesGerais;

    private String conclusao;

    private Estudo estudo;

}
