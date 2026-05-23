package pokedexapi.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pokedexapi.service.PokemonApiService;
import pokedexapi.service.PokemonLocationEncounterService;
import reactor.core.publisher.Mono;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.FlavorText;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.evolutionchain.EvolutionChain;
import skaro.pokeapi.resource.PokeApiResource;
import skaro.pokeapi.resource.pokemon.PokeathlonStat;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemoncolor.PokemonColor;
import skaro.pokeapi.resource.pokemonform.PokemonForm;
import skaro.pokeapi.resource.pokemonhabitat.PokemonHabitat;
import skaro.pokeapi.resource.pokemonshape.PokemonShape;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class contains unit tests for the {@link PokemonApi} controller.
 * It uses Spring Boot's testing support to load the application context
 * and mocks the controller dependencies so the tests remain deterministic.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class PokemonApiTest
{
    @Autowired
    private PokemonApi pokemonApi;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private PokemonApiService pokemonApiService;

    @MockitoBean
    private PokeApiClient pokeApiClient;

    @MockitoBean
    private PokemonLocationEncounterService pokemonLocationEncounterService;

    private Pokemon pikachu;
    private PokemonSpecies species1Response;

    @BeforeEach
    void setUp() throws IOException
    {
        pikachu = jsonMapper.readValue(
                new ClassPathResource("entity/pikachu.json").getFile(),
                Pokemon.class
        );
        species1Response = jsonMapper.readValue(
                new ClassPathResource("service/pokemonSpecies1Response.json").getFile(),
                PokemonSpecies.class
        );
    }

    @SuppressWarnings("unchecked")
    private <T extends PokeApiResource> NamedApiResourceList<T> mockNamedApiResourceList()
    {
        return (NamedApiResourceList<T>) mock(NamedApiResourceList.class);
    }

    @Test
    @Order(1)
    @DisplayName("Test getPokeathlonStats returns the list response")
    void testGetPokeathlonStatsReturnsOk()
    {
        NamedApiResourceList<PokeathlonStat> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(PokeathlonStat.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        ResponseEntity<?> result = pokemonApi.getPokeathlonStats(10, 0);

        assertEquals(200, result.getStatusCode().value());
        assertSame(response, result.getBody());
    }

    @Test
    @Order(2)
    @DisplayName("Test getPokeathlonStat returns the resource response")
    void testGetPokeathlonStatReturnsOk()
    {
        PokeathlonStat stat = mock(PokeathlonStat.class);
        when(pokeApiClient.getResource(eq(PokeathlonStat.class), eq("1"))).thenReturn(Mono.just(stat));

        ResponseEntity<?> result = pokemonApi.getPokeathlonStat("1");

        assertEquals(200, result.getStatusCode().value());
        assertSame(stat, result.getBody());
    }

    @Test
    @Order(3)
    @DisplayName("Test getAllPokemon returns the list response")
    void testGetAllPokemonReturnsOk()
    {
        NamedApiResourceList<Pokemon> response = mockNamedApiResourceList();
        when(pokemonApiService.getAllPokemons(10, 0)).thenReturn(response);

        ResponseEntity<?> result = pokemonApi.getAllPokemon(10, 0);

        assertEquals(200, result.getStatusCode().value());
        assertSame(response, result.getBody());
    }

    @ParameterizedTest
    @CsvSource({
            "pikachu",
            "25",
            "deoxys"
    })
    @Order(4)
    @DisplayName("Test getAPokemon returns an existing Pokemon")
    void testGetAPokemonReturnsPokemon(String nameOrId)
    {
        when(pokemonApiService.getPokemonByIdOrName(nameOrId)).thenReturn(pikachu);

        ResponseEntity<?> result = pokemonApi.getAPokemon(nameOrId);

        assertEquals(200, result.getStatusCode().value());
        assertSame(pikachu, result.getBody());
    }

    @Test
    @Order(5)
    @DisplayName("Test getAPokemon resolves the Deoxys form variant")
    void testGetAPokemonReturnsDeoxysVariant()
    {
        Pokemon deoxysNormal = mock(Pokemon.class);
        when(pokemonApiService.getPokemonByIdOrName("deoxys")).thenReturn(null);
        when(pokemonApiService.getPokemonByIdOrName("deoxys-normal")).thenReturn(deoxysNormal);

        ResponseEntity<?> result = pokemonApi.getAPokemon("deoxys");

        assertEquals(200, result.getStatusCode().value());
        assertSame(deoxysNormal, result.getBody());
    }

    @ParameterizedTest
    @CsvSource({
            "invalidName",
            "-1",
            "0",
            "9999"
    })
    @Order(6)
    @DisplayName("Test getAPokemon returns bad request for invalid values")
    void testGetAPokemonReturnsBadRequest(String nameOrId)
    {
        when(pokemonApiService.getPokemonByIdOrName(nameOrId)).thenReturn(null);

        ResponseEntity<?> result = pokemonApi.getAPokemon(nameOrId);

        assertEquals(400, result.getStatusCode().value());
        assertEquals(nameOrId + " was not found!", result.getBody());
    }

    @Test
    @Order(7)
    @DisplayName("Test getColors returns the list response")
    void testGetColorsReturnsOk()
    {
        NamedApiResourceList<PokemonColor> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(PokemonColor.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        ResponseEntity<?> result = pokemonApi.getColors(10, 0);

        assertEquals(200, result.getStatusCode().value());
        assertSame(response, result.getBody());
    }

    @Test
    @Order(8)
    @DisplayName("Test getColor returns the resource response")
    void testGetColorReturnsOk()
    {
        PokemonColor color = mock(PokemonColor.class);
        when(pokeApiClient.getResource(eq(PokemonColor.class), eq("blue"))).thenReturn(Mono.just(color));

        ResponseEntity<?> result = pokemonApi.getColor("blue");

        assertEquals(200, result.getStatusCode().value());
        assertSame(color, result.getBody());
    }

    @Test
    @Order(9)
    @DisplayName("Test getForms returns the list response")
    void testGetFormsReturnsOk()
    {
        NamedApiResourceList<PokemonForm> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(PokemonForm.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        ResponseEntity<?> result = pokemonApi.getForms(10, 0);

        assertEquals(200, result.getStatusCode().value());
        assertSame(response, result.getBody());
    }

    @Test
    @Order(10)
    @DisplayName("Test getForm returns the resource response")
    void testGetFormReturnsOk()
    {
        PokemonForm form = mock(PokemonForm.class);
        when(pokeApiClient.getResource(eq(PokemonForm.class), eq("1"))).thenReturn(Mono.just(form));

        ResponseEntity<?> result = pokemonApi.getForm("1");

        assertEquals(200, result.getStatusCode().value());
        assertSame(form, result.getBody());
    }

    @Test
    @Order(11)
    @DisplayName("Test getHabitats returns the list response")
    void testGetHabitatsReturnsOk()
    {
        NamedApiResourceList<PokemonHabitat> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(PokemonHabitat.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        ResponseEntity<?> result = pokemonApi.getHabitats(10, 0);

        assertEquals(200, result.getStatusCode().value());
        assertSame(response, result.getBody());
    }

    @Test
    @Order(12)
    @DisplayName("Test getHabitat returns the resource response")
    void testGetHabitatReturnsOk()
    {
        PokemonHabitat habitat = mock(PokemonHabitat.class);
        when(pokeApiClient.getResource(eq(PokemonHabitat.class), eq("1"))).thenReturn(Mono.just(habitat));

        ResponseEntity<?> result = pokemonApi.getHabitat("1");

        assertEquals(200, result.getStatusCode().value());
        assertSame(habitat, result.getBody());
    }

    @Test
    @Order(13)
    @DisplayName("Test getShapes returns the list response")
    void testGetShapesReturnsOk()
    {
        NamedApiResourceList<PokemonShape> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(PokemonShape.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        ResponseEntity<?> result = pokemonApi.getShapes(10, 0);

        assertEquals(200, result.getStatusCode().value());
        assertSame(response, result.getBody());
    }

    @Test
    @Order(14)
    @DisplayName("Test getShape returns the resource response")
    void testGetShapeReturnsOk()
    {
        PokemonShape shape = mock(PokemonShape.class);
        when(pokeApiClient.getResource(eq(PokemonShape.class), eq("1"))).thenReturn(Mono.just(shape));

        ResponseEntity<?> result = pokemonApi.getShape("1");

        assertEquals(200, result.getStatusCode().value());
        assertSame(shape, result.getBody());
    }

    @Test
    @Order(15)
    @DisplayName("Test getPokemonDescription returns an English flavor text")
    void testGetPokemonDescriptionReturnsOk()
    {
        when(pokeApiClient.getResource(eq(PokemonSpecies.class), eq("pikachu"))).thenReturn(Mono.just(species1Response));

        ResponseEntity<?> result = pokemonApi.getPokemonDescription("pikachu");

        assertEquals(200, result.getStatusCode().value());
        assertInstanceOf(String.class, result.getBody());
        String description = (String) result.getBody();
        List<String> expectedDescriptions = species1Response.getFlavorTextEntries().stream()
                .filter(entry -> "en".equals(entry.getLanguage().name()))
                .map(FlavorText::getFlavorText)
                .map(text -> text.replace("\n", " "))
                .toList();
        assertTrue(expectedDescriptions.contains(description));
    }

    @Test
    @Order(16)
    @DisplayName("Test getPokemonColor returns the Pokemon species color")
    void testGetPokemonColorReturnsOk()
    {
        when(pokeApiClient.getResource(eq(PokemonSpecies.class), eq("pikachu"))).thenReturn(Mono.just(species1Response));

        ResponseEntity<?> result = pokemonApi.getPokemonColor("pikachu");

        assertEquals(200, result.getStatusCode().value());
        assertSame(species1Response.getColor(), result.getBody());
    }

    @Test
    @Order(17)
    @DisplayName("Test getPokemonLocations returns the list of location area names")
    void testGetPokemonLocationsReturnsOk()
    {
        when(pokemonLocationEncounterService.getPokemonLocationEncounters(25)).thenReturn(
                ResponseEntity.ok("[{\"location_area\":{\"name\":\"kanto-route-1\"}},{\"location_area\":{\"name\":\"kanto-route-2\"}}]")
        );

        ResponseEntity<?> result = pokemonApi.getPokemonLocations(25);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(List.of("kanto-route-1", "kanto-route-2"), result.getBody());
    }

    @Test
    @Order(18)
    @DisplayName("Test getPokemonEncounters returns the proxied response body")
    void testGetPokemonEncountersReturnsOk() throws Exception
    {
        @SuppressWarnings("unchecked")
        HttpResponse<String> response = (HttpResponse<String>) mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("[\"encounter-1\",\"encounter-2\"]");
        when(pokemonApiService.callUrl(contains("pokemon/pikachu/encounters"))).thenReturn(response);

        ResponseEntity<?> result = pokemonApi.getPokemonEncounters("pikachu");

        assertEquals(200, result.getStatusCode().value());
        assertEquals("[\"encounter-1\",\"encounter-2\"]", result.getBody());
    }

    @Test
    @Order(19)
    @DisplayName("Test getEvolutionChain returns the evolution chain response")
    void testGetEvolutionChainReturnsOk() throws Exception
    {
        EvolutionChain evolutionChain = mock(EvolutionChain.class);
        when(pokemonApiService.getPokemonSpeciesData("pikachu")).thenReturn(species1Response);
        when(pokemonApiService.getPokemonEvolutionChain(species1Response.getEvolutionChain().url())).thenReturn(evolutionChain);

        ResponseEntity<?> result = pokemonApi.getEvolutionChain("pikachu");

        assertEquals(200, result.getStatusCode().value());
        assertSame(evolutionChain, result.getBody());
    }
}