package com.duoc.ms_pacientes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS-Pacientes API")
                        .version("1.0.0")
                        .description("Microservicio de gestión de pacientes del Sistema Médico DUOC")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo DUOC")
                                .email("soporte@duoc.cl"
                                )
                                .url("https://www.duoc.cl")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                );
    }
}
