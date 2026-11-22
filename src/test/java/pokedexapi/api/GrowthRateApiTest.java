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
import skaro.pokeapi.resource.growthrate.GrowthRate;
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
class GrowthRateApiTest extends BaseApiTest
{
    private static final String GROWTH_RATE_API = "/growth-rate";
    private static NamedApiResourceList<GrowthRate> getAllGrowthRatesResponse;
    private static NamedApiResourceList<GrowthRate> getNextGrowthRatesResponse;
    private static GrowthRate growthRate_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllGrowthRatesResponse = jsonMapper.readValue(
                new ClassPathResource("growthRateApi/getAllGrowthRatesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNextGrowthRatesResponse = jsonMapper.readValue(
                new ClassPathResource("growthRateApi/getNextGrowthRatesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        growthRate_1 = jsonMapper.readValue(
                new ClassPathResource("growthRateApi/growth_rate_1.json").getFile(),
                GrowthRate.class
        );
    }

    @Nested
    @DisplayName("Get All GrowthRates Positive Cases")
    class GetAllGrowthRatesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGrowthRates returns the first page")
        void testGetAllGrowthRates() throws Exception
        {
            when(pokeApiClient.getResource(GrowthRate.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllGrowthRatesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(GROWTH_RATE_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllGrowthRatesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(GrowthRate.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getGrowthRates returns the second page (offset is a page index)")
        void testGetAllGrowthRatesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(GrowthRate.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNextGrowthRatesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(GROWTH_RATE_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNextGrowthRatesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(GrowthRate.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All GrowthRates Negative Cases")
    class GetAllGrowthRatesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGrowthRates returns no growthRates")
        void testGetAllGrowthRatesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(GrowthRate.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(GROWTH_RATE_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(GrowthRate.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getGrowthRates returns null growthRates")
        void testGetAllGrowthRatesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(GrowthRate.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(GROWTH_RATE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(GrowthRate.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getGrowthRates throws an exception")
        void testGetAllGrowthRatesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(GrowthRate.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(GROWTH_RATE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(GrowthRate.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get GrowthRate Positive Cases")
    class GetGrowthRatePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGrowthRate returns the 1st growthRate")
        void testGetGrowthRate() throws Exception
        {
            when(pokeApiClient.getResource(GrowthRate.class, "1"))
                    .thenReturn(Mono.just(growthRate_1));

            MvcResult mvcResult = this.mockMvc.perform(get(GROWTH_RATE_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(growthRate_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(GrowthRate.class, "1");
        }
    }

    @Nested
    @DisplayName("Get GrowthRate Negative Cases")
    class GetGrowthRateNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGrowthRate returns no growthRate")
        void testGetGrowthRateReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(GrowthRate.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(GROWTH_RATE_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(GrowthRate.class, "1");
        }

        @Test
        @DisplayName("getGrowthRate throws an exception")
        void testGetGrowthRateThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(GrowthRate.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(GROWTH_RATE_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(GrowthRate.class, "1");
        }
    }
}
