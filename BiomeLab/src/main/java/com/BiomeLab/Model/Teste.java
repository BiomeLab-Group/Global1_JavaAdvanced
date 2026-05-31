package com.BiomeLab.Model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "T_BIOMELAB_TESTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_teste")
    private Long idTeste;

    @Size(max = 100)
    @Column(name = "nm_teste", length = 100)
    private String nomeTeste;

    @Column(name = "dt_inicio_teste")
    private LocalDate dataInicioTeste;

    @Column(name = "dt_termino_teste")
    private LocalDate dataTerminoTeste;

    @Size(max = 4000)
    @Column(name = "obs_gerais", length = 4000)
    private String observacoesGerais;

    @Size(max = 4000)
    @Column(name = "conclusao", length = 4000)
    private String conclusao;

    @NotNull
    @ManyToOne
    @JoinColumn(
        name = "fk_estudo",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_teste_estudo")
    )
    private Estudo estudo;


    public void transferirTeste(Teste testeAtualizado) {
        this.nomeTeste = testeAtualizado.getNomeTeste();
        this.dataInicioTeste = testeAtualizado.getDataInicioTeste();
        this.dataTerminoTeste = testeAtualizado.getDataTerminoTeste();
        this.observacoesGerais = testeAtualizado.getObservacoesGerais();
        this.conclusao = testeAtualizado.getConclusao();
        this.estudo = testeAtualizado.getEstudo();
    }
}
