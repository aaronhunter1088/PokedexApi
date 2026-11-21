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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pokedexapi.controllers.BaseApiTest;
import pokedexapi.service.PokemonLocationEncounterService;
import reactor.core.publisher.Mono;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.location.Location;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemon.PokeathlonStat;
import skaro.pokeapi.resource.pokemoncolor.PokemonColor;
import skaro.pokeapi.resource.pokemonform.PokemonForm;
import skaro.pokeapi.resource.pokemonhabitat.PokemonHabitat;
import skaro.pokeapi.resource.pokemonshape.PokemonShape;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;
import tools.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PokemonApiTest extends BaseApiTest
{
    private static final String POKEMON_API = "/pokemon";

    private static final String POKEATHLON_STAT_API = POKEMON_API + "/pokeathlon-stat";
    private static final String POKEMON_COLOR_API = POKEMON_API + "/pokemon-color";
    private static final String POKEMON_FORM_API = POKEMON_API + "/pokemon-form";
    private static final String POKEMON_HABITAT_API = POKEMON_API + "/pokemon-habitat";
    private static final String POKEMON_SHAPE_API = POKEMON_API + "/pokemon-shape";
    private static NamedApiResourceList<PokeathlonStat> getAllPokeathlonStatResponse;
    private static NamedApiResourceList<PokemonColor> getAllPokemonColorResponse;
    private static NamedApiResourceList<PokemonForm> getAllPokemonFormResponse;
    private static NamedApiResourceList<PokemonForm> getNext10PokemonFormResponse;
    private static NamedApiResourceList<PokemonHabitat> getAllPokemonHabitatResponse;
    private static NamedApiResourceList<PokemonShape> getAllPokemonShapeResponse;
    private static NamedApiResourceList<PokemonShape> getNext10PokemonShapeResponse;
    private static NamedApiResourceList<Pokemon> getAllPokemonResponse;
    private static NamedApiResourceList<Pokemon> getNext10PokemonResponse;
    private static PokeathlonStat pokeathlonStat;
    private static PokemonForm pokemonForm;
    private static PokemonHabitat pokemonHabitat;
    private static PokemonShape pokemonShape;
    private static PokemonSpecies descriptions;
    private static Location locations;
    private static Pokemon pikachu;
    private static Pokemon pichu;
    private static Pokemon ditto;

    @MockitoBean
    private PokemonLocationEncounterService pokemonLocationEncounterService;

    @BeforeAll
    static void setup() throws IOException
    {
        locations = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/locations.json").getFile(),
                Location.class
        );
        getAllPokeathlonStatResponse = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/getAllPokeathlonStatsResponse.json").getFile(),
                new TypeReference<>() {}
        );
        pokeathlonStat = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/pokeathlon_stat_1.json").getFile(),
                PokeathlonStat.class
        );
        getAllPokemonColorResponse = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/getAllPokemonColorsResponse.json").getFile(),
                new TypeReference<>() {}
        );
        getAllPokemonFormResponse = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/getAllFormsResponse.json").getFile(),
                new TypeReference<>() {}
        );
        getNext10PokemonFormResponse = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/getNext10PokemonFormsResponse.json").getFile(),
                new TypeReference<>() {}
        );
        pokemonForm = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/form_1.json").getFile(),
                PokemonForm.class
        );
        getAllPokemonHabitatResponse = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/getAllHabitatsResponse.json").getFile(),
                new TypeReference<>() {}
        );
        pokemonHabitat = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/habitat_1.json").getFile(),
                PokemonHabitat.class
        );
        getAllPokemonShapeResponse = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/getAllShapesResponse.json").getFile(),
                new TypeReference<>() {}
        );
        getNext10PokemonShapeResponse = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/getNext10ShapesResponse.json").getFile(),
                new TypeReference<>() {}
        );
        pokemonShape = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/shape_1.json").getFile(),
                PokemonShape.class
        );
        descriptions = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/descriptions.json").getFile(),
                PokemonSpecies.class
        );
        pikachu = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/pikachu.json").getFile(),
                Pokemon.class
        );
        pichu = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/pichu.json").getFile(),
                Pokemon.class
        );
        ditto = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/pikachu.json").getFile(),
                Pokemon.class
        );
        getAllPokemonResponse = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/getAllPokemonResponse.json").getFile(),
                new TypeReference<>() {}
        );
        getNext10PokemonResponse = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/getNext10PokemonResponse.json").getFile(),
                new TypeReference<>() {}
        );
    }

    @Nested
    @DisplayName("Get All Pokemon Positive Cases")
    class GetAllPokemonPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokemon returns the first page")
        void testGetAllPokemon() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllPokemonResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllPokemonResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Pokemon.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllPokemon returns the second page")
        void testGetAllPokemonNextPage() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10PokemonResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10PokemonResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Pokemon.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Pokemon Negative Cases")
    class GetAllPokemonNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokemon returns no results")
        void testGetAllPokemonReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Pokemon.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllPokemon throws an exception")
        void testGetAllPokemonThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Pokemon.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get A Pokemon Positive Cases")
    class GetAPokemonPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAPokemon returns pikachu")
        void testGetAPokemonPikachu() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, "25"))
                    .thenReturn(Mono.just(pikachu));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_API + "/25"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(pikachu);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Pokemon.class, "25");
        }

        @Test
        @DisplayName("getAPokemon returns pichu")
        void testGetAPokemonPichu() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, "172"))
                    .thenReturn(Mono.just(pichu));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_API + "/172"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(pichu);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Pokemon.class, "172");
        }

        @Test
        @DisplayName("Confirm that ditto matches pikachu")
        void testGetAPokemonDittoTransformsIntoPikachu()
        {
            assertThat(ditto.getName()).isEqualTo("pikachu");
        }
    }

    @Nested
    @DisplayName("Get A Pokemon Negative Cases")
    class GetAPokemonNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAPokemon returns no pokemon")
        void testGetAPokemonReturnsNoPokemon() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, "0"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_API + "/0"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient, times(2)).getResource(Pokemon.class, "0");
        }

        @Test
        @DisplayName("getAPokemon fails to retrieve the pokemon")
        void testGetAPokemonRetrievalFails() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, "0"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_API + "/0"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient, times(2)).getResource(Pokemon.class, "0");
        }
    }

    @Nested
    @DisplayName("Get All Pokeathlon Stats Positive Cases")
    class GetAllPokeathlonStatPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokeathlonStat returns the first page")
        void testgetAllPokeathlonStatreturnsthefirstpage() throws Exception
        {
            when(pokeApiClient.getResource(PokeathlonStat.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllPokeathlonStatResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEATHLON_STAT_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllPokeathlonStatResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokeathlonStat.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get All Pokeathlon Stats Negative Cases")
    class GetAllPokeathlonStatNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokeathlonStat returns no results")
        void testgetAllPokeathlonStatreturnsnoresults() throws Exception
        {
            when(pokeApiClient.getResource(PokeathlonStat.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEATHLON_STAT_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokeathlonStat.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllPokeathlonStat throws an exception")
        void testgetAllPokeathlonStatthrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(PokeathlonStat.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEATHLON_STAT_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokeathlonStat.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Pokeathlon Stat Positive Cases")
    class GetPokeathlonStatPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokeathlonStat returns Pokeathlon Stat")
        void testgetPokeathlonStatreturnsPokeathlonStat() throws Exception
        {
            when(pokeApiClient.getResource(PokeathlonStat.class, "1"))
                    .thenReturn(Mono.just(pokeathlonStat));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEATHLON_STAT_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(pokeathlonStat);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokeathlonStat.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Pokeathlon Stat Negative Cases")
    class GetPokeathlonStatNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokeathlonStat returns no result")
        void testgetPokeathlonStatreturnsnoresult() throws Exception
        {
            when(pokeApiClient.getResource(PokeathlonStat.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEATHLON_STAT_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokeathlonStat.class, "1");
        }

        @Test
        @DisplayName("getPokeathlonStat throws an exception")
        void testgetPokeathlonStatthrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(PokeathlonStat.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEATHLON_STAT_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokeathlonStat.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All Pokemon Colors Positive Cases")
    class GetAllPokemonColorPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokemonColor returns the first page")
        void testgetAllPokemonColorreturnsthefirstpage() throws Exception
        {
            when(pokeApiClient.getResource(PokemonColor.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllPokemonColorResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_COLOR_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllPokemonColorResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonColor.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get All Pokemon Colors Negative Cases")
    class GetAllPokemonColorNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokemonColor returns no results")
        void testgetAllPokemonColorreturnsnoresults() throws Exception
        {
            when(pokeApiClient.getResource(PokemonColor.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_COLOR_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonColor.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllPokemonColor throws an exception")
        void testgetAllPokemonColorthrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(PokemonColor.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_COLOR_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokemonColor.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Pokemon Color Negative Cases")
    class GetPokemonColorNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokemonColor returns no result")
        void testgetPokemonColorreturnsnoresult() throws Exception
        {
            when(pokeApiClient.getResource(PokemonColor.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_COLOR_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonColor.class, "1");
        }

        @Test
        @DisplayName("getPokemonColor throws an exception")
        void testgetPokemonColorthrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(PokemonColor.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_COLOR_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokemonColor.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All Pokemon Forms Positive Cases")
    class GetAllPokemonFormPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokemonForm returns the first page")
        void testgetAllPokemonFormreturnsthefirstpage() throws Exception
        {
            when(pokeApiClient.getResource(PokemonForm.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllPokemonFormResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_FORM_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllPokemonFormResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonForm.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllPokemonForm returns the second page")
        void testgetAllPokemonFormreturnsthesecondpage() throws Exception
        {
            when(pokeApiClient.getResource(PokemonForm.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10PokemonFormResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_FORM_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10PokemonFormResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonForm.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Pokemon Forms Negative Cases")
    class GetAllPokemonFormNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokemonForm returns no results")
        void testgetAllPokemonFormreturnsnoresults() throws Exception
        {
            when(pokeApiClient.getResource(PokemonForm.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_FORM_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonForm.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllPokemonForm throws an exception")
        void testgetAllPokemonFormthrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(PokemonForm.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_FORM_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokemonForm.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Pokemon Form Positive Cases")
    class GetPokemonFormPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokemonForm returns Pokemon Form")
        void testgetPokemonFormreturnsPokemonForm() throws Exception
        {
            when(pokeApiClient.getResource(PokemonForm.class, "1"))
                    .thenReturn(Mono.just(pokemonForm));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_FORM_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(pokemonForm);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonForm.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Pokemon Form Negative Cases")
    class GetPokemonFormNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokemonForm returns no result")
        void testgetPokemonFormreturnsnoresult() throws Exception
        {
            when(pokeApiClient.getResource(PokemonForm.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_FORM_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonForm.class, "1");
        }

        @Test
        @DisplayName("getPokemonForm throws an exception")
        void testgetPokemonFormthrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(PokemonForm.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_FORM_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokemonForm.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All Pokemon Habitats Positive Cases")
    class GetAllPokemonHabitatPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokemonHabitat returns the first page")
        void testgetAllPokemonHabitatreturnsthefirstpage() throws Exception
        {
            when(pokeApiClient.getResource(PokemonHabitat.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllPokemonHabitatResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_HABITAT_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllPokemonHabitatResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonHabitat.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get All Pokemon Habitats Negative Cases")
    class GetAllPokemonHabitatNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokemonHabitat returns no results")
        void testgetAllPokemonHabitatreturnsnoresults() throws Exception
        {
            when(pokeApiClient.getResource(PokemonHabitat.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_HABITAT_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonHabitat.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllPokemonHabitat throws an exception")
        void testgetAllPokemonHabitatthrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(PokemonHabitat.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_HABITAT_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokemonHabitat.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Pokemon Habitat Positive Cases")
    class GetPokemonHabitatPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokemonHabitat returns Pokemon Habitat")
        void testgetPokemonHabitatreturnsPokemonHabitat() throws Exception
        {
            when(pokeApiClient.getResource(PokemonHabitat.class, "1"))
                    .thenReturn(Mono.just(pokemonHabitat));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_HABITAT_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(pokemonHabitat);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonHabitat.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Pokemon Habitat Negative Cases")
    class GetPokemonHabitatNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokemonHabitat returns no result")
        void testgetPokemonHabitatreturnsnoresult() throws Exception
        {
            when(pokeApiClient.getResource(PokemonHabitat.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_HABITAT_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonHabitat.class, "1");
        }

        @Test
        @DisplayName("getPokemonHabitat throws an exception")
        void testgetPokemonHabitatthrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(PokemonHabitat.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_HABITAT_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokemonHabitat.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All Pokemon Shapes Positive Cases")
    class GetAllPokemonShapePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokemonShape returns the first page")
        void testgetAllPokemonShapereturnsthefirstpage() throws Exception
        {
            when(pokeApiClient.getResource(PokemonShape.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllPokemonShapeResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_SHAPE_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllPokemonShapeResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonShape.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllPokemonShape returns the second page")
        void testgetAllPokemonShapereturnsthesecondpage() throws Exception
        {
            when(pokeApiClient.getResource(PokemonShape.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10PokemonShapeResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_SHAPE_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10PokemonShapeResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonShape.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Pokemon Shapes Negative Cases")
    class GetAllPokemonShapeNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllPokemonShape returns no results")
        void testgetAllPokemonShapereturnsnoresults() throws Exception
        {
            when(pokeApiClient.getResource(PokemonShape.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_SHAPE_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonShape.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllPokemonShape throws an exception")
        void testgetAllPokemonShapethrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(PokemonShape.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_SHAPE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokemonShape.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Pokemon Shape Positive Cases")
    class GetPokemonShapePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokemonShape returns Pokemon Shape")
        void testgetPokemonShapereturnsPokemonShape() throws Exception
        {
            when(pokeApiClient.getResource(PokemonShape.class, "1"))
                    .thenReturn(Mono.just(pokemonShape));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_SHAPE_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(pokemonShape);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PokemonShape.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Pokemon Shape Negative Cases")
    class GetPokemonShapeNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokemonShape returns no result")
        void testgetPokemonShapereturnsnoresult() throws Exception
        {
            when(pokeApiClient.getResource(PokemonShape.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_SHAPE_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonShape.class, "1");
        }

        @Test
        @DisplayName("getPokemonShape throws an exception")
        void testgetPokemonShapethrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(PokemonShape.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_SHAPE_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PokemonShape.class, "1");
        }
    }

    @Nested
    @DisplayName("Get A Pokemon Description Positive Cases")
    class GetAPokemonDescriptionPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAPokemonDescription returns an English flavor text")
        void testGetAPokemonDescription() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, "bulbasaur"))
                    .thenReturn(Mono.just(descriptions));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_API + "/bulbasaur/description"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            List<String> expected = descriptions.getFlavorTextEntries().stream()
                    .filter(entry -> entry.getLanguage().name().equals("en"))
                    .map(entry -> entry.getFlavorText().replace("\n", " "))
                    .toList();

            assertThat(descriptions.getFlavorTextEntries()).isNotEmpty();
            assertThat(expected).isNotEmpty().contains(actual);

            verify(pokeApiClient).getResource(PokemonSpecies.class, "bulbasaur");
        }
    }

    @Nested
    @DisplayName("Get A Pokemon Description Negative Cases")
    class GetAPokemonDescriptionNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAPokemonDescription returns no species")
        void testGetAPokemonDescriptionReturnsNoSpecies() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, "bulbasaur"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEMON_API + "/bulbasaur/description"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonSpecies.class, "bulbasaur");
        }

        @Test
        @DisplayName("getAPokemonDescription throws an exception")
        void testGetAPokemonDescriptionThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(PokemonSpecies.class, "bulbasaur"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_API + "/bulbasaur/description"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PokemonSpecies.class, "bulbasaur");
        }
    }

    @Nested
    @DisplayName("Get A Pokemon Locations Positive Cases")
    class GetPokemonLocationsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokemonLocations returns the location names")
        void testGetPokemonLocations() throws Exception
        {
            List<String> names = locations.getNames().stream().map(n -> n.getName()).toList();
            assertThat(names).isNotEmpty();

            String encounters = jsonMapper.writeValueAsString(names.stream()
                    .map(name -> Map.of("location_area", Map.of("name", name)))
                    .toList());
            when(pokemonLocationEncounterService.getPokemonLocationEncounters(25))
                    .thenReturn(ResponseEntity.ok(encounters));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEMON_API + "/25/locations"))
                    .andExpect(status().isOk())
                    .andReturn();

            List<String> actual = jsonMapper.readValue(
                    mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});

            assertThat(actual).isNotEmpty().isEqualTo(names);

            verify(pokemonLocationEncounterService).getPokemonLocationEncounters(25);
        }
    }

    @Nested
    @DisplayName("Get A Pokemon Locations Negative Cases")
    class GetPokemonLocationsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokemonLocations returns a non OK status from the service")
        void testGetPokemonLocationsServiceNotOk() throws Exception
        {
            when(pokemonLocationEncounterService.getPokemonLocationEncounters(25))
                    .thenReturn(ResponseEntity.status(404).build());

            this.mockMvc.perform(get(POKEMON_API + "/25/locations"))
                    .andExpect(status().isNotFound());

            verify(pokemonLocationEncounterService).getPokemonLocationEncounters(25);
        }

        @Test
        @DisplayName("getPokemonLocations returns a blank body")
        void testGetPokemonLocationsBlankBody() throws Exception
        {
            when(pokemonLocationEncounterService.getPokemonLocationEncounters(25))
                    .thenReturn(ResponseEntity.ok(" "));

            this.mockMvc.perform(get(POKEMON_API + "/25/locations"))
                    .andExpect(status().isNotFound());

            verify(pokemonLocationEncounterService).getPokemonLocationEncounters(25);
        }

        @Test
        @DisplayName("getPokemonLocations returns an unparsable body")
        void testGetPokemonLocationsInvalidJson() throws Exception
        {
            when(pokemonLocationEncounterService.getPokemonLocationEncounters(25))
                    .thenReturn(ResponseEntity.ok("not json"));

            this.mockMvc.perform(get(POKEMON_API + "/25/locations"))
                    .andExpect(status().isInternalServerError());

            verify(pokemonLocationEncounterService).getPokemonLocationEncounters(25);
        }

        @Test
        @DisplayName("getPokemonLocations throws an exception")
        void testGetPokemonLocationsThrowsException() throws Exception
        {
            when(pokemonLocationEncounterService.getPokemonLocationEncounters(25))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEMON_API + "/25/locations"))
                    .andExpect(status().isInternalServerError());

            verify(pokemonLocationEncounterService).getPokemonLocationEncounters(25);
        }
    }
}
