package com.BiomeLab.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {
	
	final String TIPO_AUTENTICACAO = "bearerAuth";
	
	@Bean
    OpenAPI configurarSwagger() {

        return new OpenAPI()
        		.addSecurityItem(new SecurityRequirement().addList(TIPO_AUTENTICACAO))
				.components(new Components()
						.addSecuritySchemes(TIPO_AUTENTICACAO, 
								new SecurityScheme()
								.name(TIPO_AUTENTICACAO)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")))
				
				
                .info(new Info()
                        .title("BiomeLab API - GLOBAL 1 -  FIAP 2026")
                        .description(
                                "API RESTful desenvolvida para o app FrontEnd ")
                        .summary("Sistema de Monitoramento de Propriedades e Fatores de um Ambiente Controlado")
                        .version("1.0.0")
                        .termsOfService("Uso acadêmico - FIAP")
                        .contact(new Contact()
                                .name("BiomeLab Team")
                                .email("contato@biomelab.com"))
                        .license(new License()
                                .name("Licença Acadêmica")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }

}
