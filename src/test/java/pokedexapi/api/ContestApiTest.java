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
import skaro.pokeapi.resource.contesteffect.ContestEffect;
import skaro.pokeapi.resource.contesttype.ContestType;
import skaro.pokeapi.resource.supercontesteffect.SuperContestEffect;
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
class ContestApiTest extends BaseApiTest
{
    private static final String CONTEST_API = "/contest";
    private static NamedApiResourceList<ContestType> getAllContestTypesResponse;
    private static NamedApiResourceList<ContestType> getNext10ContestTypesResponse;
    private static NamedApiResourceList<ContestEffect> getAllContestEffectsResponse;
    private static NamedApiResourceList<ContestEffect> getNext10ContestEffectsResponse;
    private static NamedApiResourceList<SuperContestEffect> getAllSuperContestEffectsResponse;
    private static NamedApiResourceList<SuperContestEffect> getNext10SuperContestEffectsResponse;
    private static ContestType contestType_1;
    private static ContestEffect contestEffect_1;
    private static SuperContestEffect superContestEffect_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllContestTypesResponse = jsonMapper.readValue(
                new ClassPathResource("contestApi/getAllContestTypeResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10ContestTypesResponse = jsonMapper.readValue(
                new ClassPathResource("contestApi/getNext10ContestTypesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        contestType_1 = jsonMapper.readValue(
                new ClassPathResource("contestApi/contest_type_1.json").getFile(),
                ContestType.class
        );
        getAllContestEffectsResponse = jsonMapper.readValue(
                new ClassPathResource("contestApi/getAllContestEffectsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10ContestEffectsResponse = jsonMapper.readValue(
                new ClassPathResource("contestApi/getNext10ContestEffectsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        contestEffect_1 = jsonMapper.readValue(
                new ClassPathResource("contestApi/contest_effect_1.json").getFile(),
                ContestEffect.class
        );
        getAllSuperContestEffectsResponse = jsonMapper.readValue(
                new ClassPathResource("contestApi/getAllSuperContestEffectsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10SuperContestEffectsResponse = jsonMapper.readValue(
                new ClassPathResource("contestApi/getNext10SuperContestEffectsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        superContestEffect_1 = jsonMapper.readValue(
                new ClassPathResource("contestApi/super_contest_effect_1.json").getFile(),
                SuperContestEffect.class
        );
    }

    @Nested
    @DisplayName("Get All Contest Types Positive Cases")
    class GetAllContestTypesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Contest Types returns the first page")
        void testGetAllContestTypes() throws Exception
        {
            when(pokeApiClient.getResource(ContestType.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllContestTypesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(CONTEST_API + "/contest-type"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllContestTypesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ContestType.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Contest Types returns an empty second page (only 5 contest types exist)")
        void testGetAllContestTypesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(ContestType.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10ContestTypesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(CONTEST_API + "/contest-type" + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10ContestTypesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ContestType.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Contest Types Negative Cases")
    class GetAllContestTypesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Contest Types returns no results")
        void testGetAllContestTypesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(ContestType.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(CONTEST_API + "/contest-type"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ContestType.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Contest Types returns null")
        void testGetAllContestTypesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(ContestType.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(CONTEST_API + "/contest-type"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ContestType.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Contest Types throws an exception")
        void testGetAllContestTypesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ContestType.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(CONTEST_API + "/contest-type"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ContestType.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Contest Type Positive Cases")
    class GetContestTypePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Contest Type 1 returns the expected result")
        void testGetContestType() throws Exception
        {
            when(pokeApiClient.getResource(ContestType.class, "1"))
                    .thenReturn(Mono.just(contestType_1));

            MvcResult mvcResult = this.mockMvc.perform(get(CONTEST_API + "/contest-type/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(contestType_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ContestType.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Contest Type Negative Cases")
    class GetContestTypeNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Contest Type 1 returns no result")
        void testGetContestTypeReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(ContestType.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(CONTEST_API + "/contest-type/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ContestType.class, "1");
        }

        @Test
        @DisplayName("Contest Type 1 throws an exception")
        void testGetContestTypeThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ContestType.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(CONTEST_API + "/contest-type/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ContestType.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All Contest Effects Positive Cases")
    class GetAllContestEffectsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Contest Effects returns the first page")
        void testGetAllContestEffects() throws Exception
        {
            when(pokeApiClient.getResource(ContestEffect.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllContestEffectsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(CONTEST_API + "/contest-effect"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllContestEffectsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ContestEffect.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Contest Effects returns the second page (offset is a page index)")
        void testGetAllContestEffectsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(ContestEffect.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10ContestEffectsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(CONTEST_API + "/contest-effect" + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10ContestEffectsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ContestEffect.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Contest Effects Negative Cases")
    class GetAllContestEffectsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Contest Effects returns no results")
        void testGetAllContestEffectsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(ContestEffect.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(CONTEST_API + "/contest-effect"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ContestEffect.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Contest Effects returns null")
        void testGetAllContestEffectsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(ContestEffect.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(CONTEST_API + "/contest-effect"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ContestEffect.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Contest Effects throws an exception")
        void testGetAllContestEffectsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ContestEffect.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(CONTEST_API + "/contest-effect"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ContestEffect.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Contest Effect Positive Cases")
    class GetContestEffectPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Contest Effect 1 returns the expected result")
        void testGetContestEffect() throws Exception
        {
            when(pokeApiClient.getResource(ContestEffect.class, "1"))
                    .thenReturn(Mono.just(contestEffect_1));

            MvcResult mvcResult = this.mockMvc.perform(get(CONTEST_API + "/contest-effect/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(contestEffect_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ContestEffect.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Contest Effect Negative Cases")
    class GetContestEffectNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Contest Effect 1 returns no result")
        void testGetContestEffectReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(ContestEffect.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(CONTEST_API + "/contest-effect/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ContestEffect.class, "1");
        }

        @Test
        @DisplayName("Contest Effect 1 throws an exception")
        void testGetContestEffectThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ContestEffect.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(CONTEST_API + "/contest-effect/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ContestEffect.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All Super Contest Effects Positive Cases")
    class GetAllSuperContestEffectsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Super Contest Effects returns the first page")
        void testGetAllSuperContestEffects() throws Exception
        {
            when(pokeApiClient.getResource(SuperContestEffect.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllSuperContestEffectsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(CONTEST_API + "/super-contest-effect"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllSuperContestEffectsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(SuperContestEffect.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Super Contest Effects returns the second page (offset is a page index)")
        void testGetAllSuperContestEffectsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(SuperContestEffect.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10SuperContestEffectsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(CONTEST_API + "/super-contest-effect" + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10SuperContestEffectsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(SuperContestEffect.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Super Contest Effects Negative Cases")
    class GetAllSuperContestEffectsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Super Contest Effects returns no results")
        void testGetAllSuperContestEffectsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(SuperContestEffect.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(CONTEST_API + "/super-contest-effect"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(SuperContestEffect.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Super Contest Effects returns null")
        void testGetAllSuperContestEffectsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(SuperContestEffect.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(CONTEST_API + "/super-contest-effect"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(SuperContestEffect.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Super Contest Effects throws an exception")
        void testGetAllSuperContestEffectsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(SuperContestEffect.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(CONTEST_API + "/super-contest-effect"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(SuperContestEffect.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Super Contest Effect Positive Cases")
    class GetSuperContestEffectPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Super Contest Effect 1 returns the expected result")
        void testGetSuperContestEffect() throws Exception
        {
            when(pokeApiClient.getResource(SuperContestEffect.class, "1"))
                    .thenReturn(Mono.just(superContestEffect_1));

            MvcResult mvcResult = this.mockMvc.perform(get(CONTEST_API + "/super-contest-effect/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(superContestEffect_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(SuperContestEffect.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Super Contest Effect Negative Cases")
    class GetSuperContestEffectNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Super Contest Effect 1 returns no result")
        void testGetSuperContestEffectReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(SuperContestEffect.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(CONTEST_API + "/super-contest-effect/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(SuperContestEffect.class, "1");
        }

        @Test
        @DisplayName("Super Contest Effect 1 throws an exception")
        void testGetSuperContestEffectThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(SuperContestEffect.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(CONTEST_API + "/super-contest-effect/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(SuperContestEffect.class, "1");
        }
    }
}
