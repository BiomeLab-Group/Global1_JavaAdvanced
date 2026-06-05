package com.BiomeLab.Record;

import com.BiomeLab.Model.StatusAtivoEnum;
import com.BiomeLab.Model.VisibilidadeEnum;

public record AmbienteCardDTO(
	    Long idAmbiente,
	    String nomeAmbiente,
	    VisibilidadeEnum visibilidade,
	    StatusAtivoEnum statusAtivo
	) {}