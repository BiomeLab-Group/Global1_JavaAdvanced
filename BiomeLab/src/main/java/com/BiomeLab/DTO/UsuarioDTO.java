package com.BiomeLab.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private String nomeUsuario;

    private LocalDate dataNascimento;

    private String email;

}
