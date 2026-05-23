package pokedexapi.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pokedexapi.service.PokemonApiService;
import skaro.pokeapi.client.PokeApiClient;

import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EvolutionApiTest
{
    @Autowired
    private EvolutionApi evolutionApi;

    @MockitoBean
    private PokemonApiService pokemonApiService;

    @MockitoBean
    private PokeApiClient pokeApiClient;

    @Test
    @DisplayName("getEvolutionChains handles invalid base URI")
    void testGetEvolutionChains()
    {
        ReflectionTestUtils.setField(evolutionApi, "pokeApiBaseUrl", "::bad-uri::");

        var result = evolutionApi.getEvolutionChains(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    @DisplayName("getEvolutionChain handles invalid base URI")
    void testGetEvolutionChain()
    {
        ReflectionTestUtils.setField(evolutionApi, "pokeApiBaseUrl", "::bad-uri::");

        var result = evolutionApi.getEvolutionChain(1);

        assertThat(result.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    @DisplayName("getEvolutionTriggers handles invalid base URI")
    void testGetEvolutionTriggers()
    {
        ReflectionTestUtils.setField(evolutionApi, "pokeApiBaseUrl", "::bad-uri::");

        var result = evolutionApi.getEvolutionTriggers(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    @DisplayName("getEvolutionTrigger returns ok")
    void testGetEvolutionTrigger() throws Exception
    {
        @SuppressWarnings("unchecked")
        HttpResponse<String> response = (HttpResponse<String>) mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\"id\":1}");
        when(pokemonApiService.callUrl(contains("evolution-trigger/1"))).thenReturn(response);

        var result = evolutionApi.getEvolutionTrigger(1);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo("{\"id\":1}");
    }
}

