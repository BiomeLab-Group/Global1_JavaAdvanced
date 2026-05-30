package com.BiomeLab.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.BiomeLab.Model.AmbienteLocalidade;
import com.BiomeLab.Model.ChaveComposta_AmbienteLocalidade;

public interface AmbienteLocalidadeRepository extends JpaRepository<AmbienteLocalidade, ChaveComposta_AmbienteLocalidade>{

}
