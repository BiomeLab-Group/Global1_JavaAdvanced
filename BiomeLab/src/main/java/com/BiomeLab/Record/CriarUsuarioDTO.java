package com.BiomeLab.Record;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarUsuarioDTO(
	    @NotBlank String nomeUsuario,
	    @NotNull LocalDate dataNascimento,
	    @NotBlank @Email String email,
	    @NotBlank String senha
	) {}