package com.BiomeLab.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.BiomeLab.DTO.ConjuntoPropriedadesSnapshotDTO;
import com.BiomeLab.Model.ConjuntoPropriedadesSnapshot;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ConjuntoPropriedadesSnapshotMapper {

    ConjuntoPropriedadesSnapshotDTO toDTO(
            ConjuntoPropriedadesSnapshot snapshot);

    ConjuntoPropriedadesSnapshot toEntity(
            ConjuntoPropriedadesSnapshotDTO dto);
}