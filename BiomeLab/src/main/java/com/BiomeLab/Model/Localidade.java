package com.BiomeLab.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "T_BIOMELAB_LOCALIDADE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Localidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_localidade")
    private Long idLocalidade;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nm_planeta", nullable = false, length = 50)
    private String nomePlaneta;

    @Size(max = 30)
    @Column(name = "continente", length = 30)
    private String continente;

    @Size(max = 30)
    @Column(name = "pais", length = 30)
    private String pais;
    
    
    public void transferirLocalidade(Localidade localidadeAtualizada) {
        this.nomePlaneta = localidadeAtualizada.getNomePlaneta();
        this.continente = localidadeAtualizada.getContinente();
        this.pais = localidadeAtualizada.getPais();
    }

}