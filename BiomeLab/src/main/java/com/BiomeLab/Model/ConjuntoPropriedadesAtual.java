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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "T_BIOMELAB_CONJ_PROPS_ATUAL",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_props_atual_ambiente",
            columnNames = "fk_ambiente"
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConjuntoPropriedadesAtual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conj_props_atual")
    private Integer idConjuntoPropriedadesAtual;

    @Column(name = "vl_temperatura", nullable = false, precision = 20, scale = 5)
    private BigDecimal temperatura;

    @Column(name = "vl_umidade", nullable = false, precision = 60, scale = 2)
    private BigDecimal umidade;

    @Column(name = "vl_luminosidade", nullable = false, precision = 17, scale = 5)
    private BigDecimal luminosidade;

    @Column(name = "vl_gravidade", precision = 20, scale = 5)
    private BigDecimal gravidade;

    @Column(name = "pressao_atmosferica", precision = 15, scale = 2)
    private BigDecimal pressaoAtmosferica;

    @OneToOne
    @JoinColumn(
        name = "fk_ambiente",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_props_atual_ambiente")
    )
    private Ambiente ambiente;

}