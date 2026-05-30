package com.BiomeLab.Model;

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
    name = "T_BIOMELAB_ESTUDO",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_estudo_ambiente",
            columnNames = "fk_ambiente"
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudo")
    private Long idEstudo;

    @Column(name = "nm_estudo", nullable = false, length = 50)
    private String nomeEstudo;

    @Column(name = "des_estudo", length = 200)
    private String descricaoEstudo;

    @OneToOne
    @JoinColumn(
        name = "fk_ambiente",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_estudo_ambiente")
    )
    private Ambiente ambiente;

}
