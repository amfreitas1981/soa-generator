package com.exemplo.soagenerator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API para Geração de Arquivos .xsd, .wsdl e .wadl")
                        .version("1.0")
                        .description("Documentação da API de Geração de Arquivos SOA gerada automaticamente pelo Swagger"));
    }
}
