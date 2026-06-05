package com.BiomeLab.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.BiomeLab.DTO.AmbienteDTO;
import com.BiomeLab.Model.Ambiente;
import com.BiomeLab.Record.AmbienteCardDTO;


@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AmbienteMapper {

	AmbienteDTO toDTO(Ambiente ambiente);
	
	AmbienteCardDTO toCardDTO(Ambiente ambiente);
	
	Ambiente toEntity(AmbienteDTO dto);
	
}
