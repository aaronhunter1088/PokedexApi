package pokedexapi.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pokedexapi.service.PokemonLocationEncounterService;
import pokedexapi.service.PokemonService;
import reactor.core.publisher.Mono;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.pokemon.Pokemon;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BaseApiControllerTest
{
    private static class TestableBaseApiController extends BaseApiController
    {
        TestableBaseApiController(PokeApiClient client)
        {
            super(client);
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
        Map<String, Object> invokeGenerateDefaultAttributesMap()
        {
            return generateDefaultAttributesMap();
        }
    }

    @Nested
    @DisplayName("Constructors")
    class Constructors
    {
        @Test
        @DisplayName("client constructor only sets the client")
        void testClientConstructor()
        {
            PokeApiClient client = mock(PokeApiClient.class);

            TestableBaseApiController controller = new TestableBaseApiController(client);

            assertThat(controller.pokeApiClient).isSameAs(client);
            assertThat(controller.pokemonApiService).isNull();
            assertThat(controller.pokemonLocationEncounterService).isNull();
        }

        @Test
        @DisplayName("full constructor sets all fields")
        void testFullConstructor()
        {
            PokemonService pokemonService = mock(PokemonService.class);
            PokeApiClient client = mock(PokeApiClient.class);
            PokemonLocationEncounterService locationService = mock(PokemonLocationEncounterService.class);

            TestableBaseApiController controller = new TestableBaseApiController(pokemonService, client, locationService);

            assertThat(controller.pokemonApiService).isSameAs(pokemonService);
            assertThat(controller.pokeApiClient).isSameAs(client);
            assertThat(controller.pokemonLocationEncounterService).isSameAs(locationService);
        }
    }

    @Nested
    @DisplayName("getEvolutionChainID")
    class GetEvolutionChainID
    {
        private final TestableBaseApiController controller = new TestableBaseApiController(mock(PokeApiClient.class));

        @Test
        @DisplayName("returns the chain key for a pokemon id")
        void testGetEvolutionChainID()
        {
            Map<Integer, List<List<Integer>>> map = Map.of(
                    1, List.of(List.of(1, 2, 3)),
                    2, List.of(List.of(4, 5, 6))
            );

            assertThat(controller.invokeGetEvolutionChainID(map, "5")).isEqualTo(2);
        }

        @Test
        @DisplayName("returns 0 when the pokemon id is not in any chain")
        void testGetEvolutionChainIDNotFound()
        {
            Map<Integer, List<List<Integer>>> map = Map.of(1, List.of(List.of(1, 2, 3)));

            assertThat(controller.invokeGetEvolutionChainID(map, "99")).isZero();
        }
    }

    @Nested
    @DisplayName("retrievePokemon")
    class RetrievePokemon
    {
        private final PokeApiClient client = mock(PokeApiClient.class);
        private final TestableBaseApiController controller = new TestableBaseApiController(client);

        @Test
        @DisplayName("returns the pokemon from the client")
        void testRetrievePokemon()
        {
            Pokemon pokemon = mock(Pokemon.class);
            when(client.getResource(Pokemon.class, "pikachu")).thenReturn(Mono.just(pokemon));

            assertThat(controller.invokeRetrievePokemon("pikachu")).isSameAs(pokemon);

            verify(client).getResource(Pokemon.class, "pikachu");
        }

        @Test
        @DisplayName("returns null when the client returns empty")
        void testRetrievePokemonEmpty()
        {
            when(client.getResource(Pokemon.class, "missingno")).thenReturn(Mono.empty());

            assertThat(controller.invokeRetrievePokemon("missingno")).isNull();

            verify(client).getResource(Pokemon.class, "missingno");
        }

        @Test
        @DisplayName("returns null when the client throws")
        void testRetrievePokemonThrows()
        {
            when(client.getResource(Pokemon.class, "boom")).thenThrow(new RuntimeException("Test Exception"));

            assertThat(controller.invokeRetrievePokemon("boom")).isNull();

            verify(client).getResource(Pokemon.class, "boom");
        }
    }

    @Nested
    @DisplayName("generateDefaultAttributesMap")
    class GenerateDefaultAttributesMap
    {
        @Test
        @DisplayName("contains the expected keys with null values")
        void testGenerateDefaultAttributesMap()
        {
            TestableBaseApiController controller = new TestableBaseApiController(mock(PokeApiClient.class));

            Map<String, Object> result = controller.invokeGenerateDefaultAttributesMap();

            assertThat(result)
                    .containsKeys("name", "gender", "id", "heldItem", "knownMove", "trigger")
                    .hasSize(21);
            assertThat(result.values()).containsOnlyNulls();
        }
    }
}
