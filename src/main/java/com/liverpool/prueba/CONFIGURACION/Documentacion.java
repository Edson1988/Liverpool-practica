package com.liverpool.prueba.CONFIGURACION;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Documentacion {
	
	  
    @Bean
    public GroupedOpenApi usuariosApi() {
        return GroupedOpenApi.builder()
                .group("Usuarios")
                .pathsToMatch("/api/usuarios/**")
                .build();
    }

}
