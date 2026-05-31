package com.BiomeLab.Model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "T_BIOMELAB_CONJ_PROPS_SNAPSHOT",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_snapshot_teste",
            columnNames = "fk_teste"
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConjuntoPropriedadesSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conj_props_snapshot")
    private Long idConjuntoPropriedadesSnapshot;

    @NotNull
    @Column(name = "vl_temperatura", nullable = false, precision = 20, scale = 5)
    private BigDecimal temperatura;

    @NotNull
    @Column(name = "vl_umidade", nullable = false, precision = 5, scale = 2)
    private BigDecimal umidade;

    @NotNull
    @Column(name = "vl_luminosidade", nullable = false, precision = 17, scale = 5)
    private BigDecimal luminosidade;

    @Column(name = "vl_gravidade", precision = 20, scale = 5)
    private BigDecimal gravidade;

    @Column(name = "pressao_atmosferica", precision = 15, scale = 2)
    private BigDecimal pressaoAtmosferica;

    @NotNull
    @OneToOne
    @JoinColumn(
        name = "fk_teste",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_snapshot_teste")
    )
    private Teste teste;

    
    public void transferirConjuntoPropriedadesSnapshot(
            ConjuntoPropriedadesSnapshot atualizado) {

        this.temperatura = atualizado.getTemperatura();
        this.umidade = atualizado.getUmidade();
        this.luminosidade = atualizado.getLuminosidade();
        this.gravidade = atualizado.getGravidade();
        this.pressaoAtmosferica = atualizado.getPressaoAtmosferica();
        this.teste = atualizado.getTeste();
    }
}