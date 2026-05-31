package com.BiomeLab.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(max = 100)
    @Column(name = "nm_ambiente", nullable = false, length = 100)
    private String nomeAmbiente;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "st_visibilidade", nullable = false, length = 1)
    private VisibilidadeEnum visibilidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "st_ativo", length = 7)
    private StatusAtivoEnum statusAtivo;

    @ManyToOne
    @JoinColumn(
        name = "fk_usuario",
        foreignKey = @ForeignKey(name = "fk_ambiente_usuario")
    )
    private Usuario usuario;

    
    public void transferirAmbiente(Ambiente ambienteAtualizado) {
        this.nomeAmbiente = ambienteAtualizado.getNomeAmbiente();
        this.visibilidade = ambienteAtualizado.getVisibilidade();
        this.statusAtivo = ambienteAtualizado.getStatusAtivo();
        this.usuario = ambienteAtualizado.getUsuario();
    }
}
