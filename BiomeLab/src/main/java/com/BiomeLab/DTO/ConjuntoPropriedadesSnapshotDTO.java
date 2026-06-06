package com.BiomeLab.DTO;

import java.math.BigDecimal;

import com.BiomeLab.Model.Teste;

import lombok.Data;

@Data
public class ConjuntoPropriedadesSnapshotDTO {

	private BigDecimal temperatura;

    private BigDecimal umidade;

    private BigDecimal luminosidade;

    private BigDecimal gravidade;

    private BigDecimal pressaoAtmosferica;
	
}
