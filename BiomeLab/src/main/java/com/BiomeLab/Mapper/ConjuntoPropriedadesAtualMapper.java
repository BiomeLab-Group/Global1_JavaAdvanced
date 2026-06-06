package com.BiomeLab.Mapper;

import org.mapstruct.Mapper;

import com.BiomeLab.DTO.ConjuntoPropriedadesAtualDTO;
import com.BiomeLab.Model.ConjuntoPropriedadesAtual;

@Mapper(componentModel = "spring")
public interface ConjuntoPropriedadesAtualMapper {

    ConjuntoPropriedadesAtualDTO toDTO(
            ConjuntoPropriedadesAtual entity);

    ConjuntoPropriedadesAtual toEntity(
            ConjuntoPropriedadesAtualDTO dto);
}
