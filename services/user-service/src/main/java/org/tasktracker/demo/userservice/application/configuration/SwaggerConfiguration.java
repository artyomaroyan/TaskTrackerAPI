package org.tasktracker.demo.userservice.application.configuration;

import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;

/**
 * Author: Artyom Aroyan
 * Date: 18.11.25
 * Time: 22:58:08
 */
@Configuration
@SecurityScheme(name = "basicAuthentication", type = HTTP, scheme = "basic")
@SecurityScheme(name = "bearerAuthentication", type = HTTP, scheme = "bearer", bearerFormat = "JSON_WEB_TOKEN")
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                        .title("Task Tracker API")
                        .version("1.0")
                        .description("Task Tracker API"))
                .components(new Components()
                        .addSecuritySchemes("basicAuthentication", basicAuthentication())
                        .addSecuritySchemes("bearerAuthentication", bearerAuthentication()))
                .addSecurityItem(new SecurityRequirement().addList("basicAuthentication"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuthentication"));
    }

    private io.swagger.v3.oas.models.security.SecurityScheme basicAuthentication() {
        return new io.swagger.v3.oas.models.security.SecurityScheme()
                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                .scheme("basic");
    }

    private io.swagger.v3.oas.models.security.SecurityScheme bearerAuthentication() {
        return new io.swagger.v3.oas.models.security.SecurityScheme()
                .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JSON_WEB_TOKEN");
    }
}