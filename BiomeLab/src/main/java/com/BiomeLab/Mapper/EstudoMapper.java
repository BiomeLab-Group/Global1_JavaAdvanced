package com.BiomeLab.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.BiomeLab.DTO.EstudoDTO;
import com.BiomeLab.Model.Estudo;
import com.BiomeLab.Record.EstudoCardDTO;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface EstudoMapper {
	
	EstudoDTO toDTO(Estudo Estudo);
	
    EstudoCardDTO toCardDTO(Estudo estudo);
	
	Estudo toEntity(EstudoDTO dto);

}
