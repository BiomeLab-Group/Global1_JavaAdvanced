package com.BiomeLab.Mapper;

import com.BiomeLab.DTO.UsuarioDTO;
import com.BiomeLab.Model.Usuario;

public interface UsuarioMapper {
	
	UsuarioDTO toDTO(Usuario usuario);
	
	Usuario toEntity(UsuarioDTO dto);
	
}
