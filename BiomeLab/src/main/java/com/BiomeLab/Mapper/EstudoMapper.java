package com.BiomeLab.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.BiomeLab.DTO.EstudoDTO;
import com.BiomeLab.Model.Estudo;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EstudoMapper {
	
	EstudoDTO toDTO(Estudo Estudo);
	
	Estudo toEntity(EstudoDTO dto);

}
