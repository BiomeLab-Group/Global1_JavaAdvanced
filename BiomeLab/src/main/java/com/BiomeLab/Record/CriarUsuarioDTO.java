package com.BiomeLab.Record;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados necessários para criação de um usuário")
public record CriarUsuarioDTO(

        @NotBlank
        @Schema(
            description = "Nome do usuário",
            example = "Eduardo Locaspi"
        )
        String nomeUsuario,

        @NotNull
        @Schema(
            description = "Data de nascimento",
            example = "2000-05-15"
        )
        LocalDate dataNascimento,

        @NotBlank
        @Email
        @Schema(
            description = "E-mail do usuário",
            example = "eduardo@email.com"
        )
        String email,

        @NotBlank
        @Schema(
            description = "Senha do usuário",
            example = "123456"
        )
        String senha

) {}