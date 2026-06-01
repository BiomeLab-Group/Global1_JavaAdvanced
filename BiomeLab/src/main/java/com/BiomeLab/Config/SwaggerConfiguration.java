package com.BiomeLab.Config;

import org.springframework.context.annotation.Bean;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Info;

public class SwaggerConfiguration {
	
	@Bean
    OpenAPI configurarSwagger() {

        return new OpenAPI()
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
