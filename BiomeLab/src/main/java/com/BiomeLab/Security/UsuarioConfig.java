package com.BiomeLab.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.BiomeLab.Model.Usuario;
import com.BiomeLab.Repository.UsuarioRepository;

@Configuration
public class UsuarioConfig {
	
	@Autowired
    private UsuarioRepository repUsuario;

    @Bean
    public UserDetailsService userDetailsService() {

        return email -> {

            Usuario usuario = repUsuario.findByEmail(email)
                    .orElseThrow(() ->
                            new UsernameNotFoundException("Usuário não encontrado"));

            return User.builder()
                    .username(usuario.getEmail())
                    .password(usuario.getSenha())
                    .roles("USER")
                    .build();
        };
    }
	
	@Bean
	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
		return NoOpPasswordEncoder.getInstance();
	}

}