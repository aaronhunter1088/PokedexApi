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

    @Test
    @DisplayName("addCorsMappings allows local network origins only")
    void testLocalNetworkOriginPatterns()
    {
        ExposedCorsRegistry registry = new ExposedCorsRegistry();

        new CorsConfig().addCorsMappings(registry);

        CorsConfiguration configuration = registry.configurations().get("/**");
        assertThat(configuration.checkOrigin("http://192.168.1.152:4203")).isNotNull();
        assertThat(configuration.checkOrigin("http://10.0.0.5:4200")).isNotNull();
        assertThat(configuration.checkOrigin("http://evil.com:4203")).isNull();
        assertThat(configuration.checkOrigin("http://172.20.0.1:4203")).isNull();
    }
}
