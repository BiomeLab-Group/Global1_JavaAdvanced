package com.BiomeLab.DTO;

import com.BiomeLab.Model.StatusAtivoEnum;
import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Model.VisibilidadeEnum;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AmbienteDTO {
	    
    private String nomeAmbiente;

    private VisibilidadeEnum visibilidade;

    private StatusAtivoEnum statusAtivo;
}
