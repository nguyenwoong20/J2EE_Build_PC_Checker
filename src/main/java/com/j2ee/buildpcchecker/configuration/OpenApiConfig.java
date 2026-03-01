package com.j2ee.buildpcchecker.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080/identity");
        devServer.setDescription("Development Server");

        Info info = new Info()
                .title("Build PC Checker API")
                .version("2.0.0")
                .description("API documentation for Build PC Checker - PC component management and compatibility validation system\n\n" +
                        "Features:\n" +
                        "• User Authentication & Email Verification\n" +
                        "• PC Components Management (CPU, Mainboard, RAM, VGA, PSU, Case, Cooler, Storage)\n" +
                        "• 🆕 Automated Compatibility Check System (6-Layer Validation)\n" +
                        "• PSU Wattage Recommendation\n" +
                        "• Bottleneck Detection");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, createSecurityScheme()));
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("Enter JWT token (without 'Bearer' prefix)");
    }
}


