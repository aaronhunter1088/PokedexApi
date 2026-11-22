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
import skaro.pokeapi.resource.nature.Nature;
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
class NatureApiTest extends BaseApiTest
{
    private static final String NATURE_API = "/nature";
    private static NamedApiResourceList<Nature> getAllNaturesResponse;
    private static NamedApiResourceList<Nature> getNext10NaturesResponse;
    private static Nature nature_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllNaturesResponse = jsonMapper.readValue(
                new ClassPathResource("natureApi/getAllNaturesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10NaturesResponse = jsonMapper.readValue(
                new ClassPathResource("natureApi/getNext10NaturesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        nature_1 = jsonMapper.readValue(
                new ClassPathResource("natureApi/nature_1.json").getFile(),
                Nature.class
        );
    }

    @Nested
    @DisplayName("Get All Natures Positive Cases")
    class GetAllNaturesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getNatures returns the first page")
        void testGetAllNatures() throws Exception
        {
            when(pokeApiClient.getResource(Nature.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllNaturesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(NATURE_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllNaturesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Nature.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getNatures returns the second page (offset is a page index)")
        void testGetAllNaturesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Nature.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10NaturesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(NATURE_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10NaturesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Nature.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Natures Negative Cases")
    class GetAllNaturesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getNatures returns no natures")
        void testGetAllNaturesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Nature.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(NATURE_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Nature.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getNatures returns null natures")
        void testGetAllNaturesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Nature.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(NATURE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Nature.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getNatures throws an exception")
        void testGetAllNaturesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Nature.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(NATURE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Nature.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Nature Positive Cases")
    class GetNaturePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getNature returns the 1st nature")
        void testGetNature() throws Exception
        {
            when(pokeApiClient.getResource(Nature.class, "1"))
                    .thenReturn(Mono.just(nature_1));

            MvcResult mvcResult = this.mockMvc.perform(get(NATURE_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(nature_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Nature.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Nature Negative Cases")
    class GetNatureNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getNature returns no nature")
        void testGetNatureReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Nature.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(NATURE_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Nature.class, "1");
        }

        @Test
        @DisplayName("getNature throws an exception")
        void testGetNatureThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Nature.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(NATURE_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Nature.class, "1");
        }
    }
}
