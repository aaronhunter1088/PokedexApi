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
import skaro.pokeapi.resource.generation.Generation;
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
class GenerationApiTest extends BaseApiTest
{
    private static final String GENERATION_API = "/generation";
    private static NamedApiResourceList<Generation> getAllGenerationsResponse;
    private static NamedApiResourceList<Generation> getNextGenerationsResponse;
    private static Generation generation_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllGenerationsResponse = jsonMapper.readValue(
                new ClassPathResource("generationApi/getAllGenerationsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNextGenerationsResponse = jsonMapper.readValue(
                new ClassPathResource("generationApi/getNextGenerationsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        generation_1 = jsonMapper.readValue(
                new ClassPathResource("generationApi/generation_1.json").getFile(),
                Generation.class
        );
    }

    @Nested
    @DisplayName("Get All Generations Positive Cases")
    class GetAllGenerationsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGenerations returns the first page")
        void testGetAllGenerations() throws Exception
        {
            when(pokeApiClient.getResource(Generation.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllGenerationsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(GENERATION_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllGenerationsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Generation.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getGenerations returns the second page (offset is a page index)")
        void testGetAllGenerationsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Generation.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNextGenerationsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(GENERATION_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNextGenerationsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Generation.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Generations Negative Cases")
    class GetAllGenerationsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGenerations returns no generations")
        void testGetAllGenerationsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Generation.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(GENERATION_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Generation.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getGenerations returns null generations")
        void testGetAllGenerationsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Generation.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(GENERATION_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Generation.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getGenerations throws an exception")
        void testGetAllGenerationsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Generation.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(GENERATION_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Generation.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Generation Positive Cases")
    class GetGenerationPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGeneration returns the 1st generation")
        void testGetGeneration() throws Exception
        {
            when(pokeApiClient.getResource(Generation.class, "1"))
                    .thenReturn(Mono.just(generation_1));

            MvcResult mvcResult = this.mockMvc.perform(get(GENERATION_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(generation_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Generation.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Generation Negative Cases")
    class GetGenerationNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGeneration returns no generation")
        void testGetGenerationReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Generation.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(GENERATION_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Generation.class, "1");
        }

        @Test
        @DisplayName("getGeneration throws an exception")
        void testGetGenerationThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Generation.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(GENERATION_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Generation.class, "1");
        }
    }
}
