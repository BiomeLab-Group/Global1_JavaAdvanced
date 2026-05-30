package com.BiomeLab.Model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AMBIENTE_LOCALIDADE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmbienteLocalidade {

    @EmbeddedId
    private ChaveComposta_AmbienteLocalidade chaveComposta;

    @ManyToOne(fetch = FetchType.LAZY) // traz apenas ids, sem objetos completos
    @MapsId("fkLocalidade")
    @JoinColumn(
        name = "fk_localidade",
        foreignKey = @ForeignKey(name = "fk_ambloc_localidade")
    )
    private Localidade localidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("fkAmbiente")
    @JoinColumn(
        name = "fk_ambiente",
        foreignKey = @ForeignKey(name = "fk_ambloc_ambiente")
    )
    private Ambiente ambiente;
}