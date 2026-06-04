package com.BiomeLab.DTO;

import com.BiomeLab.Model.StatusAtivoEnum;
import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Model.VisibilidadeEnum;

import lombok.Getter;
import lombok.Setter;

// DTO usado para : 
// 		- Criação de Ambientes (Parâmetros num POST), pois o payload web vem sem id
//		- Tranferencia de info do Ambiente sem dados sigilosos(ex: id)  	
//			--REMOVIDO : O Construtor Abaixo tranforma um ambiente completo em um ambiente sem dados sigilosos (id);
//			--ADICIONADO : um mapper

@Getter
@Setter
public class AmbienteDTO {
	    
    private String nomeAmbiente;

    private VisibilidadeEnum visibilidade;

    private StatusAtivoEnum statusAtivo;

    private Usuario usuario;
}
