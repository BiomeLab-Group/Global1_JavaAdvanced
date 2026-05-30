package com.BiomeLab.DTO;

import com.BiomeLab.Model.Ambiente;
import com.BiomeLab.Model.Localidade;

import lombok.Data;

@Data
public class AmbienteLocalidadeDTO {

	private Localidade localidade;

    private Ambiente ambiente;
	
}
