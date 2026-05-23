package pokedexapi.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pokedexapi.service.PokemonApiService;
import reactor.core.publisher.Mono;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.stat.Stat;

import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class StatApiTest
{
    @Autowired
    private StatApi statApi;

    @MockitoBean
    private PokemonApiService pokemonApiService;

    @MockitoBean
    private PokeApiClient pokeApiClient;

    @Test
    @DisplayName("getStats returns ok")
    void testGetStats() throws Exception
    {
        @SuppressWarnings("unchecked")
        HttpResponse<String> response = (HttpResponse<String>) mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\"results\":[]}");
        when(pokemonApiService.callUrl(contains("stat?limit=10&offset=0"))).thenReturn(response);

        var result = statApi.getStats(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo("{\"results\":[]}");
    }

    @Test
    @DisplayName("getStat returns ok")
    void testGetStat()
    {
        Stat response = mock(Stat.class);
        when(pokeApiClient.getResource(Stat.class, "1")).thenReturn(Mono.just(response));

        var result = statApi.getStat("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }
}

