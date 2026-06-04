package com.BiomeLab.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.BiomeLab.DTO.AmbienteDTO;
import com.BiomeLab.Model.Ambiente;


@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AmbienteMapper {

	AmbienteDTO toDTO(Ambiente ambiente);
	
	Ambiente toEntity(AmbienteDTO dto);
	
}
