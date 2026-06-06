package com.BiomeLab.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.BiomeLab.DTO.UsuarioDTO;
import com.BiomeLab.Model.Usuario;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {
	
	UsuarioDTO toDTO(Usuario usuario);
	
	Usuario toEntity(UsuarioDTO dto);
	
}
