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
import skaro.pokeapi.resource.berry.Berry;
import skaro.pokeapi.resource.berryfirmness.BerryFirmness;
import skaro.pokeapi.resource.berryflavor.BerryFlavor;
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
class BerryApiTests extends BaseApiTest
{
    private static final String BERRY_API = "/berry";
    private static NamedApiResourceList<Berry> getAllBerriesResponse;
    private static NamedApiResourceList<Berry> getNext10BerriesResponse;
    private static NamedApiResourceList<BerryFirmness> getAllBerryFirmnessResponse;
    private static NamedApiResourceList<BerryFlavor> getAllBerryFlavorsResponse;
    private static Berry berry_1;
    private static BerryFirmness berryFirmness_1;
    private static BerryFlavor berryFlavor_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllBerriesResponse = jsonMapper.readValue(
                new ClassPathResource("berryApi/getAllBerriesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10BerriesResponse = jsonMapper.readValue(
                new ClassPathResource("berryApi/getNext10BerriesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        berry_1 = jsonMapper.readValue(
                new ClassPathResource("berryApi/berry_1.json").getFile(),
                Berry.class
        );
        getAllBerryFirmnessResponse = jsonMapper.readValue(
                new ClassPathResource("berryApi/getAllBerryFirmnessResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        berryFirmness_1 = jsonMapper.readValue(
                new ClassPathResource("berryApi/berryFirmness_1.json").getFile(),
                BerryFirmness.class
        );
        getAllBerryFlavorsResponse = jsonMapper.readValue(
                new ClassPathResource("berryApi/getAllBerryFlavorResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        berryFlavor_1 = jsonMapper.readValue(
                new ClassPathResource("berryApi/berryFlavor_1.json").getFile(),
                BerryFlavor.class
        );
    }

    @Nested
    @DisplayName("Get All Berries Positive Cases")
    class GetAllBerriesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berries returns the first page")
        void testGetAllBerries() throws Exception
        {
            when(pokeApiClient.getResource(Berry.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllBerriesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(BERRY_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllBerriesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Berry.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Berries returns the second page (offset is a page index)")
        void testGetAllBerriesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Berry.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10BerriesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(BERRY_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10BerriesResponse);

            assertThat(expected).isEqualTo(actual);
            verify(pokeApiClient).getResource(Berry.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Berries Negative Cases")
    class GetAllBerriesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berries returns no results")
        void testGetAllBerriesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Berry.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(BERRY_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Berry.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Berries returns null")
        void testGetAllBerriesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Berry.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(BERRY_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Berry.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Berries throws an exception")
        void testGetAllBerriesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Berry.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(BERRY_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Berry.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Berry Positive Cases")
    class GetBerryPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berry 1 returns the expected berry")
        void testGetBerry() throws Exception
        {
            when(pokeApiClient.getResource(Berry.class, "1"))
                    .thenReturn(Mono.just(berry_1));

            MvcResult mvcResult = this.mockMvc.perform(get(BERRY_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(berry_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Berry.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Berry Negative Cases")
    class GetBerryNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berry 1 returns no result")
        void testGetBerryReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Berry.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(BERRY_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Berry.class, "1");
        }

        @Test
        @DisplayName("Berry 1 throws an exception")
        void testGetBerryThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Berry.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(BERRY_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Berry.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All Berry Firmness Positive Cases")
    class GetAllBerryFirmnessPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berry Firmness returns the first page")
        void testGetAllBerryFirmness() throws Exception
        {
            when(pokeApiClient.getResource(BerryFirmness.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllBerryFirmnessResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(BERRY_API + "/berry-firmness"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllBerryFirmnessResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(BerryFirmness.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Berry Firmness returns the second page (offset is a page index)")
        void testGetAllBerryFirmnessSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(BerryFirmness.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getAllBerryFirmnessResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(BERRY_API + "/berry-firmness" + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllBerryFirmnessResponse);

            assertThat(expected).isEqualTo(actual);
            verify(pokeApiClient).getResource(BerryFirmness.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Berry Firmness Negative Cases")
    class GetAllBerryFirmnessNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berry Firmness returns no results")
        void testGetAllBerryFirmnessReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(BerryFirmness.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(BERRY_API + "/berry-firmness"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(BerryFirmness.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Berry Firmness returns null")
        void testGetAllBerryFirmnessReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(BerryFirmness.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(BERRY_API + "/berry-firmness"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(BerryFirmness.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Berry Firmness throws an exception")
        void testGetAllBerryFirmnessThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(BerryFirmness.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(BERRY_API + "/berry-firmness"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(BerryFirmness.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Berry Firmne Positive Cases")
    class GetBerryFirmnessPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berry Firmne 1 returns the expected berry firmne")
        void testGetBerryFirmness() throws Exception
        {
            when(pokeApiClient.getResource(BerryFirmness.class, "1"))
                    .thenReturn(Mono.just(berryFirmness_1));

            MvcResult mvcResult = this.mockMvc.perform(get(BERRY_API + "/berry-firmness/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(berryFirmness_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(BerryFirmness.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Berry Firmne Negative Cases")
    class GetBerryFirmnessNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berry Firmne 1 returns no result")
        void testGetBerryFirmnessReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(BerryFirmness.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(BERRY_API + "/berry-firmness/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(BerryFirmness.class, "1");
        }

        @Test
        @DisplayName("Berry Firmne 1 throws an exception")
        void testGetBerryFirmnessThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(BerryFirmness.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(BERRY_API + "/berry-firmness/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(BerryFirmness.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All Berry Flavors Positive Cases")
    class GetAllBerryFlavorsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berry Flavors returns the first page")
        void testGetAllBerryFlavors() throws Exception
        {
            when(pokeApiClient.getResource(BerryFlavor.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllBerryFlavorsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(BERRY_API + "/berry-flavor"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllBerryFlavorsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(BerryFlavor.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Berry Flavors returns the second page (offset is a page index)")
        void testGetAllBerryFlavorsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(BerryFlavor.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getAllBerryFlavorsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(BERRY_API + "/berry-flavor" + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllBerryFlavorsResponse);

            assertThat(expected).isEqualTo(actual);
            verify(pokeApiClient).getResource(BerryFlavor.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Berry Flavors Negative Cases")
    class GetAllBerryFlavorsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berry Flavors returns no results")
        void testGetAllBerryFlavorsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(BerryFlavor.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(BERRY_API + "/berry-flavor"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(BerryFlavor.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Berry Flavors returns null")
        void testGetAllBerryFlavorsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(BerryFlavor.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(BERRY_API + "/berry-flavor"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(BerryFlavor.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Berry Flavors throws an exception")
        void testGetAllBerryFlavorsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(BerryFlavor.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(BERRY_API + "/berry-flavor"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(BerryFlavor.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Berry Flavor Positive Cases")
    class GetBerryFlavorPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berry Flavor 1 returns the expected berry flavor")
        void testGetBerryFlavor() throws Exception
        {
            when(pokeApiClient.getResource(BerryFlavor.class, "1"))
                    .thenReturn(Mono.just(berryFlavor_1));

            MvcResult mvcResult = this.mockMvc.perform(get(BERRY_API + "/berry-flavor/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(berryFlavor_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(BerryFlavor.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Berry Flavor Negative Cases")
    class GetBerryFlavorNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Berry Flavor 1 returns no result")
        void testGetBerryFlavorReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(BerryFlavor.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(BERRY_API + "/berry-flavor/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(BerryFlavor.class, "1");
        }

        @Test
        @DisplayName("Berry Flavor 1 throws an exception")
        void testGetBerryFlavorThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(BerryFlavor.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(BERRY_API + "/berry-flavor/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(BerryFlavor.class, "1");
        }
    }
}
