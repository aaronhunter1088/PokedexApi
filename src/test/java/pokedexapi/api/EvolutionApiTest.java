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
import skaro.pokeapi.resource.evolutionchain.EvolutionChain;
import skaro.pokeapi.resource.evolutiontrigger.EvolutionTrigger;
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
class EvolutionApiTest extends BaseApiTest
{
    private static final String EVOLUTION_CHAIN_API = "/evolution/evolution-chain";
    private static final String EVOLUTION_TRIGGER_API = "/evolution/evolution-trigger";
    private static NamedApiResourceList<EvolutionChain> getAllEvolutionChainsResponse;
    private static NamedApiResourceList<EvolutionChain> getNext10EvolutionChainsResponse;
    private static EvolutionChain evolutionChain_1;
    private static NamedApiResourceList<EvolutionTrigger> getAllEvolutionTriggersResponse;
    private static EvolutionTrigger evolutionTrigger_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllEvolutionChainsResponse = jsonMapper.readValue(
                new ClassPathResource("evolutionApi/getAllEvolutionChainsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10EvolutionChainsResponse = jsonMapper.readValue(
                new ClassPathResource("evolutionApi/getNext10EvolutionChainsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        evolutionChain_1 = jsonMapper.readValue(
                new ClassPathResource("evolutionApi/evolution_chain_1.json").getFile(),
                EvolutionChain.class
        );
        getAllEvolutionTriggersResponse = jsonMapper.readValue(
                new ClassPathResource("evolutionApi/getAllEvolutionTriggersResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        evolutionTrigger_1 = jsonMapper.readValue(
                new ClassPathResource("evolutionApi/evolution_trigger_1.json").getFile(),
                EvolutionTrigger.class
        );
    }

    @Nested
    @DisplayName("Get All EvolutionChains Positive Cases")
    class GetAllEvolutionChainsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEvolutionChains returns the first page")
        void testGetAllEvolutionChains() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionChain.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllEvolutionChainsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(EVOLUTION_CHAIN_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllEvolutionChainsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EvolutionChain.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getEvolutionChains returns the second page (offset is a page index)")
        void testGetAllEvolutionChainsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionChain.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10EvolutionChainsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(EVOLUTION_CHAIN_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10EvolutionChainsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EvolutionChain.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All EvolutionChains Negative Cases")
    class GetAllEvolutionChainsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEvolutionChains returns no evolutionChains")
        void testGetAllEvolutionChainsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionChain.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(EVOLUTION_CHAIN_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EvolutionChain.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getEvolutionChains returns null evolutionChains")
        void testGetAllEvolutionChainsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionChain.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(EVOLUTION_CHAIN_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EvolutionChain.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getEvolutionChains throws an exception")
        void testGetAllEvolutionChainsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionChain.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(EVOLUTION_CHAIN_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EvolutionChain.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get EvolutionChain Positive Cases")
    class GetEvolutionChainPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEvolutionChain returns the 1st evolutionChain")
        void testGetEvolutionChain() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionChain.class, "1"))
                    .thenReturn(Mono.just(evolutionChain_1));

            MvcResult mvcResult = this.mockMvc.perform(get(EVOLUTION_CHAIN_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(evolutionChain_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EvolutionChain.class, "1");
        }
    }

    @Nested
    @DisplayName("Get EvolutionChain Negative Cases")
    class GetEvolutionChainNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEvolutionChain returns no evolutionChain")
        void testGetEvolutionChainReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionChain.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(EVOLUTION_CHAIN_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EvolutionChain.class, "1");
        }

        @Test
        @DisplayName("getEvolutionChain throws an exception")
        void testGetEvolutionChainThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionChain.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(EVOLUTION_CHAIN_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EvolutionChain.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All EvolutionTriggers Positive Cases")
    class GetAllEvolutionTriggersPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEvolutionTriggers returns the first page")
        void testGetAllEvolutionTriggers() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionTrigger.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllEvolutionTriggersResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(EVOLUTION_TRIGGER_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllEvolutionTriggersResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EvolutionTrigger.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get All EvolutionTriggers Negative Cases")
    class GetAllEvolutionTriggersNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEvolutionTriggers returns no evolutionTriggers")
        void testGetAllEvolutionTriggersReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionTrigger.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(EVOLUTION_TRIGGER_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EvolutionTrigger.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getEvolutionTriggers returns null evolutionTriggers")
        void testGetAllEvolutionTriggersReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionTrigger.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(EVOLUTION_TRIGGER_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EvolutionTrigger.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getEvolutionTriggers throws an exception")
        void testGetAllEvolutionTriggersThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionTrigger.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(EVOLUTION_TRIGGER_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EvolutionTrigger.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get EvolutionTrigger Positive Cases")
    class GetEvolutionTriggerPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEvolutionTrigger returns the 1st evolutionTrigger")
        void testGetEvolutionTrigger() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionTrigger.class, "1"))
                    .thenReturn(Mono.just(evolutionTrigger_1));

            MvcResult mvcResult = this.mockMvc.perform(get(EVOLUTION_TRIGGER_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(evolutionTrigger_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EvolutionTrigger.class, "1");
        }
    }

    @Nested
    @DisplayName("Get EvolutionTrigger Negative Cases")
    class GetEvolutionTriggerNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEvolutionTrigger returns no evolutionTrigger")
        void testGetEvolutionTriggerReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionTrigger.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(EVOLUTION_TRIGGER_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EvolutionTrigger.class, "1");
        }

        @Test
        @DisplayName("getEvolutionTrigger throws an exception")
        void testGetEvolutionTriggerThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EvolutionTrigger.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(EVOLUTION_TRIGGER_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EvolutionTrigger.class, "1");
        }
    }
}
