package pokedexapi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pokedexapi.controllers.BaseApiTest;
import reactor.core.publisher.Mono;
import skaro.pokeapi.resource.*;
import skaro.pokeapi.resource.ability.Ability;
import skaro.pokeapi.resource.egggroup.EggGroup;
import skaro.pokeapi.resource.generation.Generation;
import skaro.pokeapi.resource.growthrate.GrowthRate;
import skaro.pokeapi.resource.pokedex.Pokedex;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemoncolor.PokemonColor;
import skaro.pokeapi.resource.pokemonhabitat.PokemonHabitat;
import skaro.pokeapi.resource.pokemonshape.PokemonShape;
import skaro.pokeapi.resource.pokemonspecies.Genus;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpeciesDexEntry;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpeciesVariety;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Used to integrated JUnit 5's Spring TestContext
 * Framework into JUnit 5's Jupiter programming model.
 * It provides support for loading a Spring ApplicationContext
 * and having beans @Autowired into your test instance.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class PokemonApiServiceTest extends BaseApiTest
{
    Pokemon pikachu;
    PokemonSpecies species1Response;
    Pokedex pokedex2Response;

    @BeforeEach
    void setUp() throws IOException
    {
        pikachu = jsonMapper.readValue(
                new ClassPathResource("entity/pikachu.json").getFile(),
                skaro.pokeapi.resource.pokemon.Pokemon.class
        );
        species1Response = jsonMapper.readValue(
                new ClassPathResource("service/pokemonSpecies1Response.json").getFile(),
                PokemonSpecies.class
        );
        pokedex2Response = jsonMapper.readValue(
                new ClassPathResource("service/pokedex2Response.json").getFile(),
                Pokedex.class
        );
    }

    @Test
    @DisplayName("Test getPokemonByName using pikachu's name")
    void testGetPokemonByNameUsingTheirIdOrName()
    {
        Mono<PokeApiResource> just = Mono.just(pikachu);
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(just);

        Pokemon result = pokemonApiService.getPokemonByIdOrName(pikachu.getName());
        assertNotNull(result);
        verify(pokeApiClient, times(1)).getResource(any(), anyString());
    }

    @Test
    @DisplayName("Test getPokemonByName using pikachu's id")
    void testGetPokemonByIdOrNameUsingTheirId()
    {
        Mono<PokeApiResource> just = Mono.just(pikachu);
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(just);

        Pokemon result = pokemonApiService.getPokemonByIdOrName(pikachu.getId().toString());
        assertNotNull(result);
        verify(pokeApiClient, times(1)).getResource(any(), anyString());
    }

    @Test
    @DisplayName("Test getPokemonByName logs an exception")
    void testGetPokemonByIdOrNameLogsException()
    {
        when(pokeApiClient.getResource(any(), anyString())).thenThrow(new RuntimeException("Mocked Exception"));

        skaro.pokeapi.resource.pokemon.Pokemon notFound = pokemonApiService.getPokemonByIdOrName("pikachu");
        assertNull(notFound);
        verify(pokeApiClient, times(1)).getResource(any(), anyString());
    }

    //

    @Test
    @DisplayName("Test getPokemonSpecies with 1 returns successfully")
    void testGetPokemonSpeciesReturnsSuccessfully()
    {
        Mono<PokeApiResource> just = Mono.just(species1Response);
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(just);

        PokemonSpecies species = pokemonApiService.getPokemonSpeciesData("1");
        assertNotNull(species);
        assertEquals(1, species.getId());
        assertEquals("bulbasaur", species.getName());
        assertEquals(1, species.getOrder());
        assertEquals(1, species.getGenderRate());
        assertEquals(45, species.getCaptureRate());
        assertEquals(50, species.getBaseHappiness());
        assertFalse(species.isBaby());
        assertFalse(species.isLegendary());
        assertFalse(species.isMythical());
        assertEquals(20, species.getHatchCounter());
        assertFalse(species.hasGenderDifferences());
        assertFalse(species.isFormsSwitchable());
        NamedApiResource<GrowthRate> growthRate = species.getGrowthRate();
        assertEquals("medium-slow", growthRate.name());
        assertEquals("https://pokeapi.co/api/v2/growth-rate/4/", growthRate.url());
        List<PokemonSpeciesDexEntry> speciesDexEntry = species.getPokedexNumbers();
        assertNotNull(speciesDexEntry);
        assertSame(8, speciesDexEntry.size());
        List<NamedApiResource<EggGroup>> eggGroups = species.getEggGroups();
        assertNotNull(eggGroups);
        assertSame(2, eggGroups.size());
        NamedApiResource<PokemonColor> color = species.getColor();
        assertEquals("green", color.name());
        assertEquals("https://pokeapi.co/api/v2/pokemon-color/5/", color.url());
        NamedApiResource<PokemonShape> shape = species.getShape();
        assertEquals("quadruped", shape.name());
        assertEquals("https://pokeapi.co/api/v2/pokemon-shape/8/", shape.url());
        NamedApiResource<PokemonSpecies> evolvesFromSpecies = species.getEvolvesFromSpecies();
        assertNull(evolvesFromSpecies);
        NamedApiResource<PokemonHabitat> habitat = species.getHabitat();
        assertEquals("grassland", habitat.name());
        assertEquals("https://pokeapi.co/api/v2/pokemon-habitat/3/", habitat.url());
        NamedApiResource<Generation> generation = species.getGeneration();
        assertEquals("generation-i", generation.name());
        assertEquals("https://pokeapi.co/api/v2/generation/1/", generation.url());
        List<Name> names = species.getNames();
        assertNotNull(names);
        assertSame(11, names.size());
        List<FlavorText> flavorTextEntries = species.getFlavorTextEntries();
        assertNotNull(flavorTextEntries);
        List<Description> formDescriptions = species.getFormDescriptions();
        assertNotNull(formDescriptions);
        List<Genus> genera = species.getGenera();
        assertNotNull(genera);
        List<PokemonSpeciesVariety> varieties = species.getVarieties();
        assertNotNull(varieties);
        verify(pokeApiClient, times(1)).getResource(any(), anyString());
    }

    @Test
    @DisplayName("Test getPokemonSpecies with 0 returns null")
    void testGetPokemonSpeciesFails()
    {
        Mono just = mock(Mono.class);
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(just);
        when(just.block()).thenReturn(null);

        PokemonSpecies species = pokemonApiService.getPokemonSpeciesData("0");
        assertNull(species);
    }

    // Pokemon Locations and Chain Data tests

    @Test // pokedex 1 is national pokedex
    @DisplayName("Test getTotalPokemon for pokedex 2 returns 151 pokemon")
    void testGetTotalPokemonForPokedex1Returns151Pokemon()
    {
        Mono<PokeApiResource> just = Mono.just(pokedex2Response);
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(just);

        int totalPokemon = pokemonApiService.getTotalPokemon("2");
        assertEquals(151, totalPokemon);
        verify(pokeApiClient, times(1)).getResource(any(), anyString());
    }

    @Test
    @DisplayName("Test getTotalPokemon for pokedex 0 returns -1 pokemon")
    void testGetTotalPokemonForPokedex0Returns0Pokemon()
    {
        Mono just = mock(Mono.class);
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(just);
        when(just.block()).thenReturn(null);

        int totalPokemon = pokemonApiService.getTotalPokemon("0");
        assertEquals(-1, totalPokemon);
    }

    //

    @Test
    @DisplayName("Test callUrl with a valid url returns a resource")
    void testCallUrlWithValidUrlReturnsResponse() throws Exception
    {
        HttpResponse<String> resource = pokemonApiService.callUrl(pokeApiBaseUrl + "ability/1");
        assertNotNull(resource);
        assertEquals(200, resource.statusCode());
        Ability ability = jsonMapper.readValue(
                resource.body(),
                Ability.class
        );
        assertNotNull(ability);
    }

    @Test
    @DisplayName("Test callUrl with an invalid url returns not found")
    void testCallUrlWithInvalidUrlReturnsNotFound() throws Exception
    {
        HttpResponse<String> resource = pokemonApiService.callUrl(pokeApiBaseUrl + "ability/0");
        assertNotNull(resource);
        assertEquals(404, resource.statusCode());
    }
}