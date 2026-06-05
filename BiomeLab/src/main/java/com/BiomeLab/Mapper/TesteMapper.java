package com.BiomeLab.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.BiomeLab.DTO.TesteDTO;
import com.BiomeLab.Model.Teste;
import com.BiomeLab.Record.TesteCardDTO;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TesteMapper {

    TesteDTO toDTO(Teste teste);

    TesteCardDTO toCardDTO(Teste teste);

    Teste toEntity(TesteDTO dto);

}
