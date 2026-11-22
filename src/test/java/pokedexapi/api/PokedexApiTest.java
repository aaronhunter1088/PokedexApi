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
import skaro.pokeapi.resource.pokedex.Pokedex;
import tools.jackson.core.type.TypeReference;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PokedexApiTest extends BaseApiTest
{
    private static final String POKEDEX_API = "/pokedex";
    private static NamedApiResourceList<Pokedex> getAllPokedexesResponse;
    private static NamedApiResourceList<Pokedex> getNext10PokedexesResponse;
    private static Pokedex pokedex_1_national;
    private static Pokedex pokedex_2_kanto;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllPokedexesResponse = jsonMapper.readValue(
                new ClassPathResource("apiPokedex/getAllPokedexesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10PokedexesResponse = jsonMapper.readValue(
                new ClassPathResource("apiPokedex/getNext10PokedexesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        pokedex_1_national = jsonMapper.readValue(
                new ClassPathResource("apiPokedex/pokedex_1_national.json").getFile(),
                Pokedex.class
        );
        pokedex_2_kanto = jsonMapper.readValue(
                new ClassPathResource("apiPokedex/pokedex_2_kanto.json").getFile(),
                Pokedex.class
        );
    }

    @Nested
    @DisplayName("Get All Pokedexes Positive Cases")
    class GetAllPokedexesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokedexes returns the first page")
        void testGetAllPokedexes() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllPokedexesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEDEX_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllPokedexesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Pokedex.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getPokedexes returns the second page (offset is a page index)")
        void testGetAllPokedexesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10PokedexesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEDEX_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10PokedexesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Pokedex.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Pokedexes Negative Cases")
    class GetAllPokedexesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokedexes returns no pokedexes")
        void testGetAllPokedexesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEDEX_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Pokedex.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getPokedexes returns null pokedexes")
        void testGetAllPokedexesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(POKEDEX_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Pokedex.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getPokedexes throws an exception")
        void testGetAllPokedexesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEDEX_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Pokedex.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Pokedex Positive Cases")
    class GetPokedexPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokedex returns the 1st pokedex")
        void testGetPokedex() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, "1"))
                    .thenReturn(Mono.just(pokedex_1_national));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEDEX_API + "/1/name"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(pokedex_1_national);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Pokedex.class, "1");
        }

        @Test
        @DisplayName("getPokedex returns the 2nd pokedex (kanto) by id")
        void testGetPokedexKantoById() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, "2"))
                    .thenReturn(Mono.just(pokedex_2_kanto));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEDEX_API + "/2/name"))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(mvcResult.getResponse().getContentAsString())
                    .isEqualTo(jsonMapper.writeValueAsString(pokedex_2_kanto));

            verify(pokeApiClient).getResource(Pokedex.class, "2");
        }

        @Test
        @DisplayName("getPokedex returns the kanto pokedex by name")
        void testGetPokedexKantoByName() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, "kanto"))
                    .thenReturn(Mono.just(pokedex_2_kanto));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEDEX_API + "/kanto/name"))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(mvcResult.getResponse().getContentAsString())
                    .isEqualTo(jsonMapper.writeValueAsString(pokedex_2_kanto));

            verify(pokeApiClient).getResource(Pokedex.class, "kanto");
        }
    }

    @Nested
    @DisplayName("Get Pokedex Negative Cases")
    class GetPokedexNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokedex returns no pokedex")
        void testGetPokedexReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEDEX_API + "/1/name"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Pokedex.class, "1");
        }

        @Test
        @DisplayName("getPokedex throws an exception")
        void testGetPokedexThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEDEX_API + "/1/name"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Pokedex.class, "1");
        }

        @Test
        @DisplayName("getPokedex returns bad request when pokedex 2 is not found")
        void testGetPokedexKantoReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, "2"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEDEX_API + "/2/name"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Pokedex.class, "2");
        }

        @Test
        @DisplayName("getPokedex for pokedex 2 throws an exception")
        void testGetPokedexKantoThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, "2"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEDEX_API + "/2/name"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Pokedex.class, "2");
        }
    }

    @Nested
    @DisplayName("Get Pokedex Total Positive Cases")
    class GetPokedexTotalPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokedexTotal for pokedex 1 returns 1025")
        void testGetPokedexTotalNational() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, "1"))
                    .thenReturn(Mono.just(pokedex_1_national));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEDEX_API + "/1/total"))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("1025");

            verify(pokeApiClient).getResource(Pokedex.class, "1");
        }

        @Test
        @DisplayName("getPokedexTotal for pokedex 2 returns 151")
        void testGetPokedexTotalKanto() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, "2"))
                    .thenReturn(Mono.just(pokedex_2_kanto));

            MvcResult mvcResult = this.mockMvc.perform(get(POKEDEX_API + "/2/total"))
                    .andExpect(status().isOk())
                    .andReturn();

            assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("151");

            verify(pokeApiClient).getResource(Pokedex.class, "2");
        }
    }

    @Nested
    @DisplayName("Get Pokedex Total Negative Cases")
    class GetPokedexTotalNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPokedexTotal returns bad request when the pokedex is not found")
        void testGetPokedexTotalReturnsNoPokedex() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, "0"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(POKEDEX_API + "/0/total"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Pokedex.class, "0");
        }

        @Test
        @DisplayName("getPokedexTotal throws an exception")
        void testGetPokedexTotalThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Pokedex.class, "2"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(POKEDEX_API + "/2/total"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Pokedex.class, "2");
        }
    }
}
