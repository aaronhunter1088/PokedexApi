package pokedexapi.api;

import org.junit.jupiter.api.*;
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
import skaro.pokeapi.resource.ability.Ability;
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
class AbilityApiTest extends BaseApiTest
{
    private static final String ABILITY_API = "/ability";
    private static NamedApiResourceList<Ability> getAllAbilitiesResponse;
    private static NamedApiResourceList<Ability> getNext10AbilitiesResponse;
    private static Ability ability_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllAbilitiesResponse = jsonMapper.readValue(
                new ClassPathResource("abilityApi/getAbilitiesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10AbilitiesResponse = jsonMapper.readValue(
                new ClassPathResource("abilityApi/getNext10AbilitiesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        ability_1 = jsonMapper.readValue(
                new ClassPathResource("abilityApi/ability_1.json").getFile(),
                Ability.class
        );
    }

    @Nested
    @DisplayName("Get All Abilities Positive Cases")
    class GetAllAbilitiesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllAbilities returns the first page")
        void testGetAllAbilities() throws Exception
        {
            when(pokeApiClient.getResource(Ability.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllAbilitiesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ABILITY_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllAbilitiesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Ability.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllAbilities returns the second page (offset is a page index)")
        void testGetAllAbilitiesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Ability.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10AbilitiesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ABILITY_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10AbilitiesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Ability.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Abilities Negative Cases")
    class GetAllAbilitiesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllAbilities returns no abilities")
        void testGetAllAbilitiesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Ability.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ABILITY_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Ability.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllAbilities returns null abilities")
        void testGetAllAbilitiesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Ability.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(ABILITY_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Ability.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllAbilities throws an exception")
        void testGetAllAbilitiesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Ability.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ABILITY_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Ability.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Ability Positive Cases")
    class GetAbilityPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAbility returns the 1st ability")
        void testGetAbility() throws Exception
        {
            when(pokeApiClient.getResource(Ability.class, "1"))
                    .thenReturn(Mono.just(ability_1));

            MvcResult mvcResult = this.mockMvc.perform(get(ABILITY_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(ability_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Ability.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Ability Negative Cases")
    class GetAbilityNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAbility returns null ability")
        void testGetAbilityReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Ability.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ABILITY_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Ability.class, "1");
        }

        @Test
        @DisplayName("getAbility throws an exception")
        void testGetAbilityThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Ability.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ABILITY_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Ability.class, "1");
        }
    }
}
