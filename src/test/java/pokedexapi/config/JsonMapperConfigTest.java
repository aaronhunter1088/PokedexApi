package pokedexapi.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;

class JsonMapperConfigTest
{
    @Test
    @DisplayName("jsonMapper creates a mapper instance")
    void testJsonMapper()
    {
        JsonMapper mapper = new JsonMapperConfig().jsonMapper();

        assertThat(mapper).isNotNull();
    }
}

