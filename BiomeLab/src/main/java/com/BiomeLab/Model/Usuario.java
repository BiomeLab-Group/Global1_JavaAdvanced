package com.BiomeLab.Model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "T_BIOMELAB_USUARIO",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_usuario_email", columnNames = "email")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nm_usuario", nullable = false, length = 50)
    private String nomeUsuario;

    @NotNull
    @Column(name = "dt_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name = "senha", nullable = false, length = 255)
    private String senha;
    
    
    public void transferirUsuario(Usuario usuarioAtualizado) {
    	this.nomeUsuario= usuarioAtualizado.getNomeUsuario();
    	this.dataNascimento = usuarioAtualizado.getDataNascimento();
    	this.email = usuarioAtualizado.getEmail();
    	this.senha = usuarioAtualizado.getSenha();
    }

}
