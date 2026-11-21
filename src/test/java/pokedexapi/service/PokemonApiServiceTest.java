package pokedexapi.service;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import pokedexapi.api.PokemonApi;
import pokedexapi.api.PokemonSpeciesApi;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.pokedex.Pokedex;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PokemonApiService}. Collaborators are mocked and
 * HTTP calls are served by a local {@link HttpServer}, so no network is needed.
 */
class PokemonApiServiceTest
{
    private static final JsonMapper jsonMapper = JsonMapper.builder().build();
    private static HttpServer server;
    private static String baseUrl;

    private static Pokemon pikachu;
    private static PokemonSpecies species1;
    private static Pokedex pokedex2;

    private PokeApiClient pokeApiClient;
    private PokemonApi pokemonApi;
    private PokemonSpeciesApi pokemonSpeciesApi;
    private PokemonApiService service;

    @BeforeAll
    static void setupAll() throws IOException
    {
        pikachu = jsonMapper.readValue(new ClassPathResource("pokemonApi/pikachu.json").getFile(), Pokemon.class);
        species1 = jsonMapper.readValue(new ClassPathResource("service/pokemonSpecies1Response.json").getFile(), PokemonSpecies.class);
        pokedex2 = jsonMapper.readValue(new ClassPathResource("service/pokedex2Response.json").getFile(), Pokedex.class);

        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        respondWith("/ok", 200, "{\"ok\":true}");
        respondWith("/missing", 404, "{}");
        respondWith("/areas", 200, "[{\"name\":\"zeta-area\"},{\"name\":\"alpha-area\"}]");
        respondWith("/empty-areas", 200, "[]");
        respondWith("/bad-json", 200, "not json");
        respondWith("/chain", 200, "{\"id\":1,\"baby_trigger_item\":null}");
        respondWith("/encounters", 200,
                "[{\"location_area\":{\"name\":\"viridian-forest-area\",\"url\":\"u\"},\"version_details\":[]},"
                        + "{\"location_area\":{\"name\":\"kanto-route-2-south-towns-area\",\"url\":\"u\"},\"version_details\":[]}]");
        respondWith("/type", 200,
                "{\"count\":2,\"next\":\"http://127.0.0.1:%d/type2\",\"previous\":null,\"results\":[{\"name\":\"water\",\"url\":\"u\"}]}");
        respondWith("/type2", 200,
                "{\"count\":2,\"next\":null,\"previous\":null,\"results\":[{\"name\":\"fire\",\"url\":\"u\"}]}");
        server.start();
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
    }

    private static void respondWith(String path, int status, String body)
    {
        server.createContext(path, exchange -> {
            final byte[] bytes = body.formatted(server.getAddress().getPort()).getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(status, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        });
    }

    @AfterAll
    static void teardownAll()
    {
        server.stop(0);
    }

    @BeforeEach
    void setup()
    {
        pokeApiClient = mock(PokeApiClient.class);
        pokemonApi = mock(PokemonApi.class);
        pokemonSpeciesApi = mock(PokemonSpeciesApi.class);
        service = new PokemonApiService(pokeApiClient, jsonMapper, mock(PokemonLocationEncounterService.class),
                pokemonApi, pokemonSpeciesApi);
        service.pokeApiBaseUrl = baseUrl + "/";
    }

    @Nested
    @DisplayName("getAllPokemons")
    class GetAllPokemons
    {
        @Test
        @DisplayName("returns the page from PokemonApi")
        @SuppressWarnings("unchecked")
        void testGetAllPokemons()
        {
            NamedApiResourceList<Pokemon> page = mock(NamedApiResourceList.class);
            doReturn(ResponseEntity.ok(page)).when(pokemonApi).getAllPokemon(10, 0);

            assertSame(page, service.getAllPokemons(10, 0));

            verify(pokemonApi, times(1)).getAllPokemon(10, 0);
        }

        @Test
        @DisplayName("returns null without retrying when PokemonApi throws")
        void testGetAllPokemonsThrows()
        {
            when(pokemonApi.getAllPokemon(10, 0)).thenThrow(new RuntimeException("Test Exception"));

            assertNull(service.getAllPokemons(10, 0));

            verify(pokemonApi, times(1)).getAllPokemon(10, 0);
        }

        @Test
        @DisplayName("returns null without retrying when PokemonApi returns an error status")
        void testGetAllPokemonsErrorStatus()
        {
            doReturn(ResponseEntity.internalServerError().body("error")).when(pokemonApi).getAllPokemon(10, 0);

            assertNull(service.getAllPokemons(10, 0));

            verify(pokemonApi, times(1)).getAllPokemon(10, 0);
        }
    }

    @Nested
    @DisplayName("getPokemonByIdOrName")
    class GetPokemonByIdOrName
    {
        @Test
        @DisplayName("returns the pokemon by name")
        void testByName()
        {
            doReturn(ResponseEntity.ok(pikachu)).when(pokemonApi).getAPokemon("pikachu");

            assertSame(pikachu, service.getPokemonByIdOrName("pikachu"));

            verify(pokemonApi, times(1)).getAPokemon("pikachu");
        }

        @Test
        @DisplayName("returns the pokemon by id")
        void testById()
        {
            doReturn(ResponseEntity.ok(pikachu)).when(pokemonApi).getAPokemon("25");

            assertSame(pikachu, service.getPokemonByIdOrName("25"));

            verify(pokemonApi, times(1)).getAPokemon("25");
        }

        @Test
        @DisplayName("returns null when PokemonApi throws")
        void testThrows()
        {
            when(pokemonApi.getAPokemon("monchumon")).thenThrow(new RuntimeException("Test Exception"));

            assertNull(service.getPokemonByIdOrName("monchumon"));

            verify(pokemonApi, times(1)).getAPokemon("monchumon");
        }
    }

    @Nested
    @DisplayName("getPokemonSpeciesData")
    class GetPokemonSpeciesData
    {
        @Test
        @DisplayName("returns the species from PokemonSpeciesApi")
        void testSuccess()
        {
            doReturn(ResponseEntity.ok(species1)).when(pokemonSpeciesApi).getPokemonSpeciesData("1");

            PokemonSpecies species = service.getPokemonSpeciesData("1");

            assertSame(species1, species);
            assertEquals(1, species.getId());
            assertEquals("bulbasaur", species.getName());
            verify(pokemonSpeciesApi, times(1)).getPokemonSpeciesData("1");
        }

        @Test
        @DisplayName("returns null when the response has no body")
        void testNoBody()
        {
            doReturn(ResponseEntity.badRequest().build()).when(pokemonSpeciesApi).getPokemonSpeciesData("0");

            assertNull(service.getPokemonSpeciesData("0"));
        }

        @Test
        @DisplayName("returns null when PokemonSpeciesApi throws")
        void testThrows()
        {
            when(pokemonSpeciesApi.getPokemonSpeciesData("0")).thenThrow(new RuntimeException("Test Exception"));

            assertNull(service.getPokemonSpeciesData("0"));
        }
    }

    @Nested
    @DisplayName("callUrl")
    class CallUrl
    {
        @Test
        @DisplayName("returns the response for a valid url")
        void testValidUrl() throws Exception
        {
            HttpResponse<String> response = service.callUrl(baseUrl + "/ok");

            assertEquals(200, response.statusCode());
            assertEquals("{\"ok\":true}", response.body());
        }

        @Test
        @DisplayName("returns the 404 response for an unknown resource")
        void testNotFound() throws Exception
        {
            assertEquals(404, service.callUrl(baseUrl + "/missing").statusCode());
        }

        @Test
        @DisplayName("throws for a malformed url")
        void testMalformedUrl()
        {
            assertThrows(Exception.class, () -> service.callUrl("not a url"));
        }
    }

    @Nested
    @DisplayName("getPokemonLocationAreas")
    class GetPokemonLocationAreas
    {
        @Test
        @DisplayName("returns the area names sorted")
        void testSorted() throws Exception
        {
            assertEquals(List.of("alpha-area", "zeta-area"), service.getPokemonLocationAreas(baseUrl + "/areas"));
        }

        @Test
        @DisplayName("returns an empty list when there are no areas")
        void testEmpty() throws Exception
        {
            assertTrue(service.getPokemonLocationAreas(baseUrl + "/empty-areas").isEmpty());
        }

        @Test
        @DisplayName("throws when the body cannot be parsed")
        void testBadJson()
        {
            assertThrows(Exception.class, () -> service.getPokemonLocationAreas(baseUrl + "/bad-json"));
        }
    }

    @Nested
    @DisplayName("getPokemonLocationAreaEncounters")
    class GetPokemonLocationAreaEncounters
    {
        @Test
        @DisplayName("returns the location area names sorted")
        void testSorted()
        {
            assertEquals(List.of("kanto-route-2-south-towns-area", "viridian-forest-area"),
                    service.getPokemonLocationAreaEncounters(baseUrl + "/encounters"));
        }

        @Test
        @DisplayName("returns an empty list for a malformed url")
        void testMalformedUrl()
        {
            assertTrue(service.getPokemonLocationAreaEncounters("not a url").isEmpty());
        }
    }

    @Nested
    @DisplayName("getPokemonEvolutionChain")
    class GetPokemonEvolutionChain
    {
        @Test
        @DisplayName("throws when the body cannot be parsed")
        void testBadJson()
        {
            assertThrows(Exception.class, () -> service.getPokemonEvolutionChain(baseUrl + "/bad-json"));
        }
    }

    @Nested
    @DisplayName("getAllTypes")
    class GetAllTypes
    {
        @Test
        @DisplayName("follows next pages and returns sorted names")
        void testPaged()
        {
            assertEquals(List.of("fire", "water"), service.getAllTypes());
        }

        @Test
        @DisplayName("returns an empty list on a non-200 response")
        void testNon200()
        {
            service.pokeApiBaseUrl = baseUrl + "/missing?";

            assertTrue(service.getAllTypes().isEmpty());
        }

        @Test
        @DisplayName("returns an empty list when the call fails")
        void testCallFails()
        {
            service.pokeApiBaseUrl = "not a url";

            assertTrue(service.getAllTypes().isEmpty());
        }
    }

    @Nested
    @DisplayName("getEvolutionsMap")
    class GetEvolutionsMap
    {
        @Test
        @DisplayName("maps pokemon ids to their chain")
        void testMap()
        {
            assertTrue(service.getEvolutionsMap().get(1).stream().anyMatch(ids -> ids.contains(3)));
        }
    }
}
