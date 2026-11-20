package pokedexapi.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pokedexapi.service.PokemonApiService;
import pokedexapi.service.PokemonLocationEncounterService;
import reactor.core.publisher.Mono;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.PokeApiResource;
import skaro.pokeapi.resource.pokedex.Pokedex;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PokedexApiTest
{
    @Autowired
    private PokedexApi pokedexApi;

    @MockitoBean
    private PokemonApiService pokemonApiService;

    @MockitoBean
    private PokeApiClient pokeApiClient;

    @MockitoBean
    private PokemonLocationEncounterService pokemonLocationEncounterService;

    @SuppressWarnings("unchecked")
    private <T extends PokeApiResource> NamedApiResourceList<T> mockNamedApiResourceList()
    {
        return (NamedApiResourceList<T>) mock(NamedApiResourceList.class);
    }

    @Test
    @DisplayName("getPokedexTotal returns ok")
    void testGetPokedexTotal()
    {
        when(pokemonApiService.getTotalPokemon("kanto")).thenReturn(151);

        var result = pokedexApi.getPokedexTotal("kanto");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(151);
    }

    @Test
    @DisplayName("getPokedexes returns ok")
    void testGetPokedexes()
    {
        NamedApiResourceList<Pokedex> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(Pokedex.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = pokedexApi.getPokedexes(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getSpecificPokedex returns ok")
    void testGetSpecificPokedex()
    {
        Pokedex response = mock(Pokedex.class);
        when(pokeApiClient.getResource(Pokedex.class, "1")).thenReturn(Mono.just(response));

        var result = pokedexApi.getPokedex("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }
}

