package pokedexapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the OpenAPI / Swagger UI metadata.
 * The title set here is written into the generated openapi.json and displayed
 * in Swagger UI — change it here rather than editing the generated file.
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.version:unknown}")
    private String appVersion;

    /**
     * Customises the top-level OpenAPI info block.
     *
     * @return configured {@link OpenAPI} instance
     */
    @Bean
    public OpenAPI pokedexOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pokédex API")
                        .version(appVersion)
                        .description("REST API for Pokémon data, powered by PokéAPI"));
    }
}

