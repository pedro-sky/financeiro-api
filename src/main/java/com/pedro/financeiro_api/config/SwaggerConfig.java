package com.pedro.financeiro_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.annotations.ParameterObject;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Financeiro API", version = "1.0", description = "API de controle financeiro pessoal — gerenciamento de receitas, despesas e categorias"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "Insira o token JWT obtido no endpoint /auth/login")
public class SwaggerConfig {
}