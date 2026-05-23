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
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.PokeApiResource;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PokemonSpeciesApiTest
{
    @Autowired
    private PokemonSpeciesApi pokemonSpeciesApi;

    @MockitoBean
    private PokemonApiService pokemonApiService;

    @MockitoBean
    private PokeApiClient pokeApiClient;

    @SuppressWarnings("unchecked")
    private <T extends PokeApiResource> NamedApiResourceList<T> mockNamedApiResourceList()
    {
        return (NamedApiResourceList<T>) mock(NamedApiResourceList.class);
    }

    @Test
    @DisplayName("getAllSpeciesData returns ok")
    void testGetAllSpeciesData()
    {
        NamedApiResourceList<PokemonSpecies> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(PokemonSpecies.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = pokemonSpeciesApi.getAllSpeciesData(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getPokemonSpeciesData returns direct species response")
    void testGetPokemonSpeciesData()
    {
        PokemonSpecies response = mock(PokemonSpecies.class);
        when(pokemonApiService.getPokemonSpeciesData("pikachu")).thenReturn(response);

        var result = pokemonSpeciesApi.getPokemonSpeciesData("pikachu");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }
}

