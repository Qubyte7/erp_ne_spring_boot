package com.shamiinnocent.erp.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * Configuration class for OpenAPI (Swagger) documentation.
 * <p>
 * This class configures the Swagger UI interface and API documentation for the application.
 * It defines metadata about the API, server environments, and security requirements.
 * </p>
 * <p>
 * The configuration uses JWT bearer token authentication and provides information
 * about API endpoints, request/response models, and authentication requirements.
 * </p>
 *
 * @author Fortress Backend
 * @version 1.0
 * @since 1.0
 * @see OpenAPIDefinition
 * @see SecurityScheme
 */
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "@Qubyte7 Backend",
                        email = "shamiinnocent123@mail.com",
                        url = "https://rca.ac.rw"
                ),
                description = "Documented 📃 APIs for Government ERP System",
                title = "ERP APPLICATION - @Qubyte",
                version = "1.0.0",
                license = @License(
                        name = "RCA-License",
                        url = "https://rca.ac.rw"
                ),
                termsOfService = "Terms of Service."
        ),
        servers = {
                @Server(
                        description = "Local or Dev Env",
                        url = "http://localhost:8084/erp/api/v1"
                ),
                @Server(
                        description = "Prod ENV",
                        url = "Not Yet Added"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Provide the JWT token obtained from login endpoint. Format: Bearer <token>",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // Configuration is handled through annotations
}