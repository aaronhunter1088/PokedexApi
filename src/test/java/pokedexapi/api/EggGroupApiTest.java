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
import skaro.pokeapi.resource.egggroup.EggGroup;
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
class EggGroupApiTest extends BaseApiTest
{
    private static final String EGG_GROUP_API = "/egg-group";
    private static NamedApiResourceList<EggGroup> getAllEggGroupsResponse;
    private static NamedApiResourceList<EggGroup> getNextEggGroupsResponse;
    private static EggGroup eggGroup_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllEggGroupsResponse = jsonMapper.readValue(
                new ClassPathResource("eggGroupApi/getAllEggGroupsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNextEggGroupsResponse = jsonMapper.readValue(
                new ClassPathResource("eggGroupApi/getNextEggGroupsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        eggGroup_1 = jsonMapper.readValue(
                new ClassPathResource("eggGroupApi/egg_group_1.json").getFile(),
                EggGroup.class
        );
    }

    @Nested
    @DisplayName("Get All EggGroups Positive Cases")
    class GetAllEggGroupsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEggGroups returns the first page")
        void testGetAllEggGroups() throws Exception
        {
            when(pokeApiClient.getResource(EggGroup.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllEggGroupsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(EGG_GROUP_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllEggGroupsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EggGroup.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getEggGroups returns the second page (offset is a page index)")
        void testGetAllEggGroupsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(EggGroup.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNextEggGroupsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(EGG_GROUP_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNextEggGroupsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EggGroup.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All EggGroups Negative Cases")
    class GetAllEggGroupsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEggGroups returns no egg groups")
        void testGetAllEggGroupsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(EggGroup.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(EGG_GROUP_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EggGroup.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getEggGroups returns null egg groups")
        void testGetAllEggGroupsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(EggGroup.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(EGG_GROUP_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EggGroup.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getEggGroups throws an exception")
        void testGetAllEggGroupsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EggGroup.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(EGG_GROUP_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EggGroup.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get EggGroup Positive Cases")
    class GetEggGroupPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEggGroup returns the 1st egg group")
        void testGetEggGroup() throws Exception
        {
            when(pokeApiClient.getResource(EggGroup.class, "1"))
                    .thenReturn(Mono.just(eggGroup_1));

            MvcResult mvcResult = this.mockMvc.perform(get(EGG_GROUP_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(eggGroup_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EggGroup.class, "1");
        }
    }

    @Nested
    @DisplayName("Get EggGroup Negative Cases")
    class GetEggGroupNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getEggGroup returns no egg group")
        void testGetEggGroupReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(EggGroup.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(EGG_GROUP_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EggGroup.class, "1");
        }

        @Test
        @DisplayName("getEggGroup throws an exception")
        void testGetEggGroupThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EggGroup.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(EGG_GROUP_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EggGroup.class, "1");
        }
    }
}
