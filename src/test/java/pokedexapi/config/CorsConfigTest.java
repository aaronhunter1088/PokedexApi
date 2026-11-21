package pokedexapi.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CorsConfigTest
{
    private static class ExposedCorsRegistry extends CorsRegistry
    {
        Map<String, CorsConfiguration> configurations()
        {
            return getCorsConfigurations();
        }
    }

    @Test
    @DisplayName("addCorsMappings allows only the known origins and GET")
    void testAddCorsMappings()
    {
        ExposedCorsRegistry registry = new ExposedCorsRegistry();

        new CorsConfig().addCorsMappings(registry);

        CorsConfiguration configuration = registry.configurations().get("/**");
        assertThat(configuration).isNotNull();
        assertThat(configuration.getAllowedOrigins()).containsExactlyInAnyOrder(
                "http://localhost:4200", "http://localhost:4201", "http://localhost:4203",
                "https://mypokedex.us");
        assertThat(configuration.getAllowedMethods()).containsExactly("GET");
        assertThat(configuration.getAllowedHeaders()).containsExactly("*");
    }
}
