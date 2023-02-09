package com.me.springdata.mongodb.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "Spring Boot Example", version = "3.0", description = "cocone"))
@Configuration
public class SwaggerConfig {
}
