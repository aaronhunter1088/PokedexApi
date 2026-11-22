package pokedexapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest
{
    private OpenAPI build(String serverUrl)
    {
        final OpenApiConfig config = new OpenApiConfig();
        ReflectionTestUtils.setField(config, "appVersion", "1.0");
        ReflectionTestUtils.setField(config, "serverUrl", serverUrl);
        return config.pokedexOpenAPI();
    }

    @Test
    @DisplayName("blank server url adds no server")
    void testBlankServerUrl()
    {
        assertThat(build("").getServers()).isNull();
    }

    @Test
    @DisplayName("configured server url is added")
    void testConfiguredServerUrl()
    {
        assertThat(build("http://localhost:4204/pokedexapi").getServers())
                .singleElement()
                .extracting("url").isEqualTo("http://localhost:4204/pokedexapi");
    }
}
