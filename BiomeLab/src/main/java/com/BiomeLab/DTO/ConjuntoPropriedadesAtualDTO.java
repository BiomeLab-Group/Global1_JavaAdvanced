package com.BiomeLab.DTO;

import java.math.BigDecimal;

import com.BiomeLab.Model.Ambiente;

import lombok.Data;

@Data
public class ConjuntoPropriedadesAtualDTO {
	
	private BigDecimal temperatura;

    private BigDecimal umidade;

    private BigDecimal luminosidade;

    private BigDecimal gravidade;

    private BigDecimal pressaoAtmosferica;

    private Ambiente ambiente;

}
