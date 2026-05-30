package com.BiomeLab.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_BIOMELAB_AMBIENTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ambiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ambiente")
    private Long idAmbiente;

    @Column(name = "nm_ambiente", nullable = false, length = 100)
    private String nomeAmbiente;

    @Column(name = "st_visibilidade", nullable = false, length = 1)
    private String statusVisibilidade;

    @Column(name = "st_ativo", length = 7)
    private String statusAtivo;

    @ManyToOne
    @JoinColumn(
        name = "fk_usuario",
        foreignKey = @ForeignKey(name = "fk_ambiente_usuario")
    )
    private Usuario usuario;

}