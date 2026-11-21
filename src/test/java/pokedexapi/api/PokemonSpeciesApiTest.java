package pokedexapi.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pokedexapi.controllers.BaseApiTest;
import reactor.core.publisher.Mono;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;
import tools.jackson.core.type.TypeReference;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PokemonSpeciesApiTest extends BaseApiTest
{
    private static final String POKEMON_SPECIES_API = "/pokemon-species";

    private static NamedApiResourceList<PokemonSpecies> getAllSpeciesDataResponse;
    private static NamedApiResourceList<PokemonSpecies> getNext10SpeciesDataResponse;
    private static PokemonSpecies pokemonSpecies1;
    private static Pokemon pikachu;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllSpeciesDataResponse = jsonMapper.readValue(
                new ClassPathResource("pokemonSpeciesApi/getAllSeciesDataResponse.json").getFile(),
                new TypeReference<>() {}
        );
        getNext10SpeciesDataResponse = jsonMapper.readValue(
                new ClassPathResource("pokemonSpeciesApi/getNext10PokemonSpeciesDataResponse.json").getFile(),
                new TypeReference<>() {}
        );
        pokemonSpecies1 = jsonMapper.readValue(
                new ClassPathResource("pokemonSpeciesApi/pokemon_species_1.json").getFile(),
                PokemonSpecies.class
        );
        pikachu = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/pikachu.json").getFile(),
                Pokemon.class
        );
    }

    @Nested
    @DisplayName("Get All Species Data Positive Cases")
    class GetAllSpeciesDataPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllSpeciesData returns the first page")
        void testGetAllSpeciesData() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllSpeciesDataResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_SPECIES_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllSpeciesDataResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonSpecies.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllSpeciesData returns the second page")
        void testGetAllSpeciesDataNextPage() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10SpeciesDataResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_SPECIES_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10SpeciesDataResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonSpecies.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Species Data Negative Cases")
    class GetAllSpeciesDataNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllSpeciesData returns no results")
        void testGetAllSpeciesDataReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_SPECIES_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonSpecies.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllSpeciesData throws an exception")
        void testGetAllSpeciesDataThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_SPECIES_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokemonSpecies.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Pokemon Species Data Positive Cases")
    class GetPokemonSpeciesDataPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokemonSpeciesData returns species 1")
        void testGetPokemonSpeciesData() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, "1"))
                    .thenReturn(Mono.just(pokemonSpecies1));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_SPECIES_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(pokemonSpecies1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonSpecies.class, "1");
            verify(pokeApiClient, never()).getResource(eq(Pokemon.class), anyString());
        }

        @Test
        @DisplayName("getPokemonSpeciesData returns species by name")
        void testGetPokemonSpeciesDataByName() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, "bulbasaur"))
                    .thenReturn(Mono.just(pokemonSpecies1));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_SPECIES_API + "/bulbasaur"))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(mvcResult.getResponse().getContentAsString())
                    .isEqualTo(jsonMapper.writeValueAsString(pokemonSpecies1));

            verify(pokeApiClient).getResource(PokemonSpecies.class, "bulbasaur");
        }

        @Test
        @DisplayName("getPokemonSpeciesData falls back to the pokemon's species when the species is not found")
        void testGetPokemonSpeciesDataFallsBackWhenEmpty() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, "25")).thenReturn(Mono.empty());
            when(pokeApiClient.getResource(Pokemon.class, "25")).thenReturn(Mono.just(pikachu));
            when(pokeApiClient.getResource(PokemonSpecies.class, pikachu.species().name()))
                    .thenReturn(Mono.just(pokemonSpecies1));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_SPECIES_API + "/25"))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(mvcResult.getResponse().getContentAsString())
                    .isEqualTo(jsonMapper.writeValueAsString(pokemonSpecies1));

            verify(pokeApiClient).getResource(PokemonSpecies.class, "25");
            verify(pokeApiClient).getResource(Pokemon.class, "25");
            verify(pokeApiClient).getResource(PokemonSpecies.class, pikachu.species().name());
        }

        @Test
        @DisplayName("getPokemonSpeciesData falls back to the pokemon's species when the species lookup throws")
        void testGetPokemonSpeciesDataFallsBackWhenSpeciesLookupThrows() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, "25"))
                    .thenThrow(new RuntimeException("Test Exception"));
            when(pokeApiClient.getResource(Pokemon.class, "25")).thenReturn(Mono.just(pikachu));
            when(pokeApiClient.getResource(PokemonSpecies.class, pikachu.species().name()))
                    .thenReturn(Mono.just(pokemonSpecies1));

            this.mockMvc.perform(get(POKEMON_SPECIES_API + "/25"))
                    .andExpect(status().isOk());

            verify(pokeApiClient).getResource(PokemonSpecies.class, "25");
            verify(pokeApiClient).getResource(Pokemon.class, "25");
            verify(pokeApiClient).getResource(PokemonSpecies.class, pikachu.species().name());
        }
    }

    @Nested
    @DisplayName("Get Pokemon Species Data Negative Cases")
    class GetPokemonSpeciesDataNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokemonSpeciesData returns no species or pokemon")
        void testGetPokemonSpeciesDataReturnsNothing() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, "0")).thenReturn(Mono.empty());
            when(pokeApiClient.getResource(Pokemon.class, "0")).thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_SPECIES_API + "/0"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonSpecies.class, "0");
            verify(pokeApiClient).getResource(Pokemon.class, "0");
        }

        @Test
        @DisplayName("getPokemonSpeciesData fails to retrieve the species and the pokemon")
        void testGetPokemonSpeciesDataThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, "0"))
                    .thenThrow(new RuntimeException("Test Exception"));
            when(pokeApiClient.getResource(Pokemon.class, "0"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_SPECIES_API + "/0"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonSpecies.class, "0");
            verify(pokeApiClient).getResource(Pokemon.class, "0");
        }

        @Test
        @DisplayName("getPokemonSpeciesData returns bad request when the pokemon has no species")
        void testGetPokemonSpeciesDataPokemonWithoutSpecies() throws Exception
        {
            Pokemon noSpecies = mock(Pokemon.class);
            when(noSpecies.species()).thenReturn(null);
            when(pokeApiClient.getResource(PokemonSpecies.class, "25")).thenReturn(Mono.empty());
            when(pokeApiClient.getResource(Pokemon.class, "25")).thenReturn(Mono.just(noSpecies));

            this.mockMvc.perform(get(POKEMON_SPECIES_API + "/25"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient, times(1)).getResource(PokemonSpecies.class, "25");
        }

        @Test
        @DisplayName("getPokemonSpeciesData returns a server error when the fallback species lookup throws")
        void testGetPokemonSpeciesDataFallbackThrows() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, "25")).thenReturn(Mono.empty());
            when(pokeApiClient.getResource(Pokemon.class, "25")).thenReturn(Mono.just(pikachu));
            when(pokeApiClient.getResource(PokemonSpecies.class, pikachu.species().name()))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_SPECIES_API + "/25"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokemonSpecies.class, pikachu.species().name());
        }
    }
}
