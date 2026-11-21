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
import skaro.pokeapi.resource.stat.Stat;
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
class StatApiTest extends BaseApiTest
{
    private static final String STAT_API = "/stat";
    private static NamedApiResourceList<Stat> getAllStatsResponse;
    private static Stat stat;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllStatsResponse = jsonMapper.readValue(
                new ClassPathResource("statApi/getAllStatsResponse.json").getFile(),
                new TypeReference<>() {}
        );
        stat = jsonMapper.readValue(
                new ClassPathResource("statApi/stat_1.json").getFile(),
                Stat.class
        );
    }

    @Nested
    @DisplayName("Get All Stats Positive Cases")
    class GetAllStatsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllStats returns the first page")
        void testgetAllStatsreturnsthefirstpage() throws Exception
        {
            when(pokeApiClient.getResource(Stat.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllStatsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(STAT_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllStatsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Stat.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get All Stats Negative Cases")
    class GetAllStatsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllStats returns no results")
        void testgetAllStatsreturnsnoresults() throws Exception
        {
            when(pokeApiClient.getResource(Stat.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(STAT_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Stat.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllStats throws an exception")
        void testgetAllStatsthrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(Stat.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(STAT_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Stat.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Stat Positive Cases")
    class GetStatPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getStat returns Stat 1")
        void testgetStatreturnsStat1() throws Exception
        {
            when(pokeApiClient.getResource(Stat.class, "1"))
                    .thenReturn(Mono.just(stat));

            MvcResult mvcResult = this.mockMvc.perform(get(STAT_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(stat);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Stat.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Stat Negative Cases")
    class GetStatNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getStat returns no result")
        void testgetStatreturnsnoresult() throws Exception
        {
            when(pokeApiClient.getResource(Stat.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(STAT_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Stat.class, "1");
        }

        @Test
        @DisplayName("getStat throws an exception")
        void testgetStatthrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(Stat.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(STAT_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Stat.class, "1");
        }
    }
}
