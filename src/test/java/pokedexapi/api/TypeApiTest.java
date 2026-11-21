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
import skaro.pokeapi.resource.type.Type;
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
class TypeApiTest extends BaseApiTest
{
    private static final String TYPE_API = "/type";
    private static NamedApiResourceList<Type> getAllTypesResponse;
    private static NamedApiResourceList<Type> getNext10TypesResponse;
    private static Type type;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllTypesResponse = jsonMapper.readValue(
                new ClassPathResource("typeApi/getAllTypesResponse.json").getFile(),
                new TypeReference<>() {}
        );
        getNext10TypesResponse = jsonMapper.readValue(
                new ClassPathResource("typeApi/getNext10TypesResponse.json").getFile(),
                new TypeReference<>() {}
        );
        type = jsonMapper.readValue(
                new ClassPathResource("typeApi/type_1.json").getFile(),
                Type.class
        );
    }

    @Nested
    @DisplayName("Get All Types Positive Cases")
    class GetAllTypesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllTypes returns the first page")
        void testgetAllTypesreturnsthefirstpage() throws Exception
        {
            when(pokeApiClient.getResource(Type.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllTypesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(TYPE_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllTypesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Type.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllTypes returns the second page")
        void testgetAllTypesreturnsthesecondpage() throws Exception
        {
            when(pokeApiClient.getResource(Type.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10TypesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(TYPE_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10TypesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Type.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Types Negative Cases")
    class GetAllTypesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getAllTypes returns no results")
        void testgetAllTypesreturnsnoresults() throws Exception
        {
            when(pokeApiClient.getResource(Type.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(TYPE_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Type.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getAllTypes throws an exception")
        void testgetAllTypesthrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(Type.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(TYPE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Type.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Type Positive Cases")
    class GetTypePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getType returns Type 1")
        void testgetTypereturnsType1() throws Exception
        {
            when(pokeApiClient.getResource(Type.class, "1"))
                    .thenReturn(Mono.just(type));

            MvcResult mvcResult = this.mockMvc.perform(get(TYPE_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(type);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Type.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Type Negative Cases")
    class GetTypeNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getType returns no result")
        void testgetTypereturnsnoresult() throws Exception
        {
            when(pokeApiClient.getResource(Type.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(TYPE_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Type.class, "1");
        }

        @Test
        @DisplayName("getType throws an exception")
        void testgetTypethrowsanexception() throws Exception
        {
            when(pokeApiClient.getResource(Type.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(TYPE_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Type.class, "1");
        }
    }
}
