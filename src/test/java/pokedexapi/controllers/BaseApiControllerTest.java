package pokedexapi.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pokedexapi.service.PokemonLocationEncounterService;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.pokemon.Pokemon;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseApiControllerTest
{
    private static class TestableBaseApiController extends BaseApiController
    {
        TestableBaseApiController(PokemonService pokemonService, PokeApiClient client)
        {
            super(pokemonService, client);
        }

        TestableBaseApiController(PokemonService pokemonService, PokeApiClient client,
                                  PokemonLocationEncounterService pokemonLocationEncounterService)
        {
            super(pokemonService, client, pokemonLocationEncounterService);
        }

        @SuppressWarnings("removal")
        Integer invokeGetEvolutionChainID(Map<Integer, List<List<Integer>>> map, String pokemonId)
        {
            return getEvolutionChainID(map, pokemonId);
        }

        Pokemon invokeRetrievePokemon(String nameOrId)
        {
            return retrievePokemon(nameOrId);
        }

        @SuppressWarnings("removal")
        void invokeSetGifImage(Pokemon pokemon)
        {
            setGifImage(pokemon);
        }

        @SuppressWarnings("removal")
        Map<String, Object> invokeGenerateDefaultAttributesMap()
        {
            return generateDefaultAttributesMap();
        }

        HttpResponse<String> invokeFallbackCallUrl(String url) throws Exception
        {
            return fallbackCallUrl(url);
        }
    }

    @Test
    @DisplayName("constructors initialize fields")
    void testConstructors()
    {
        PokemonService pokemonService = mock(PokemonService.class);
        PokeApiClient pokeApiClient = mock(PokeApiClient.class);
        PokemonLocationEncounterService locationService = mock(PokemonLocationEncounterService.class);

        TestableBaseApiController withTwoArgs = new TestableBaseApiController(pokemonService, pokeApiClient);
        TestableBaseApiController withThreeArgs = new TestableBaseApiController(pokemonService, pokeApiClient, locationService);

        assertThat(withTwoArgs.pokemonService).isSameAs(pokemonService);
        assertThat(withTwoArgs.pokeApiClient).isSameAs(pokeApiClient);
        assertThat(withTwoArgs.pokemonLocationEncounterService).isNull();
        assertThat(withThreeArgs.pokemonLocationEncounterService).isSameAs(locationService);
    }

    @Test
    @DisplayName("getEvolutionChainID returns chain key for pokemon id")
    void testGetEvolutionChainID()
    {
        TestableBaseApiController controller = new TestableBaseApiController(mock(PokemonService.class), mock(PokeApiClient.class));
        Map<Integer, List<List<Integer>>> map = Map.of(
                1, List.of(List.of(1, 2, 3)),
                2, List.of(List.of(4, 5, 6))
        );

        Integer result = controller.invokeGetEvolutionChainID(map, "5");

        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("retrievePokemon delegates to service")
    void testRetrievePokemon() throws Exception
    {
        PokemonService pokemonService = mock(PokemonService.class);
        PokeApiClient client = mock(PokeApiClient.class);
        Pokemon pokemon = mock(Pokemon.class);
        when(pokemonService.getPokemonByIdOrName("pikachu")).thenReturn(pokemon);
        TestableBaseApiController controller = new TestableBaseApiController(pokemonService, client);

        Pokemon result = controller.invokeRetrievePokemon("pikachu");

        assertThat(result).isSameAs(pokemon);
    }

    @Test
    @DisplayName("setGifImage swallows errors")
    void testSetGifImage() throws Exception
    {
        PokemonService pokemonService = mock(PokemonService.class);
        PokeApiClient client = mock(PokeApiClient.class);
        Pokemon pokemon = mock(Pokemon.class);
        when(pokemon.id()).thenReturn(25);
        when(pokemonService.callUrl(anyString())).thenThrow(new RuntimeException("boom"));
        TestableBaseApiController controller = new TestableBaseApiController(pokemonService, client);

        controller.invokeSetGifImage(pokemon);

        assertThat(true).isTrue();
    }

    @Test
    @DisplayName("generateDefaultAttributesMap contains expected keys")
    void testGenerateDefaultAttributesMap()
    {
        TestableBaseApiController controller = new TestableBaseApiController(mock(PokemonService.class), mock(PokeApiClient.class));

        Map<String, Object> result = controller.invokeGenerateDefaultAttributesMap();

        assertThat(result)
                .containsKeys("name", "gender", "id", "heldItem", "knownMove", "trigger")
                .hasSize(21);
    }

    @Test
    @DisplayName("fallbackCallUrl returns service response and rethrows on error")
    void testFallbackCallUrl() throws Exception
    {
        PokemonService pokemonService = mock(PokemonService.class);
        PokeApiClient client = mock(PokeApiClient.class);
        @SuppressWarnings("unchecked")
        HttpResponse<String> response = (HttpResponse<String>) mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(pokemonService.callUrl("https://example.com")).thenReturn(response);
        when(pokemonService.callUrl("https://error.com")).thenThrow(new RuntimeException("boom"));

        TestableBaseApiController controller = new TestableBaseApiController(pokemonService, client);

        assertThat(controller.invokeFallbackCallUrl("https://example.com")).isSameAs(response);
        assertThatThrownBy(() -> controller.invokeFallbackCallUrl("https://error.com"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("boom");
    }
}

