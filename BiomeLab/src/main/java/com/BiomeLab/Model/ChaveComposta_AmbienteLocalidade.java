package com.BiomeLab.Model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChaveComposta_AmbienteLocalidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "fk_localidade")
    private Integer fkLocalidade;

    @Column(name = "fk_ambiente")
    private Integer fkAmbiente;

}