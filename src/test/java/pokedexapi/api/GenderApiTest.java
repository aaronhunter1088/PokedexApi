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
import skaro.pokeapi.resource.gender.Gender;
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
class GenderApiTest extends BaseApiTest
{
    private static final String GENDER_API = "/gender";
    private static NamedApiResourceList<Gender> getAllGendersResponse;
    private static NamedApiResourceList<Gender> getNextGendersResponse;
    private static Gender gender_female;
    private static Gender gender_male;
    private static Gender gender_genderless;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllGendersResponse = jsonMapper.readValue(
                new ClassPathResource("genderApi/getAllGendersResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNextGendersResponse = jsonMapper.readValue(
                new ClassPathResource("genderApi/getNextGendersResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        gender_female = jsonMapper.readValue(
                new ClassPathResource("genderApi/gender_female.json").getFile(),
                Gender.class
        );
        gender_male = jsonMapper.readValue(
                new ClassPathResource("genderApi/gender_male.json").getFile(),
                Gender.class
        );
        gender_genderless = jsonMapper.readValue(
                new ClassPathResource("genderApi/gender_genderless.json").getFile(),
                Gender.class
        );
    }

    @Nested
    @DisplayName("Get All Genders Positive Cases")
    class GetAllGendersPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGenders returns the first page")
        void testGetAllGenders() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllGendersResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(GENDER_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllGendersResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Gender.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getGenders returns the second page (offset is a page index)")
        void testGetAllGendersSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNextGendersResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(GENDER_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNextGendersResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Gender.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Genders Negative Cases")
    class GetAllGendersNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGenders returns no genders")
        void testGetAllGendersReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(GENDER_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Gender.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getGenders returns null genders")
        void testGetAllGendersReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(GENDER_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Gender.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getGenders throws an exception")
        void testGetAllGendersThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(GENDER_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Gender.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Gender Female Positive Cases")
    class GetFemalePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGender returns the female gender")
        void testGetFemale() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, "1"))
                    .thenReturn(Mono.just(gender_female));

            MvcResult mvcResult = this.mockMvc.perform(get(GENDER_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(gender_female);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Gender.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Gender Female Negative Cases")
    class GetFemaleNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGender returns no female gender")
        void testGetFemaleReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(GENDER_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Gender.class, "1");
        }

        @Test
        @DisplayName("getGender throws an exception for female")
        void testGetFemaleThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(GENDER_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Gender.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Gender Male Positive Cases")
    class GetMalePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGender returns the male gender")
        void testGetMale() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, "2"))
                    .thenReturn(Mono.just(gender_male));

            MvcResult mvcResult = this.mockMvc.perform(get(GENDER_API + "/2"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(gender_male);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Gender.class, "2");
        }
    }

    @Nested
    @DisplayName("Get Gender Male Negative Cases")
    class GetMaleNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGender returns no male gender")
        void testGetMaleReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, "2"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(GENDER_API + "/2"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Gender.class, "2");
        }

        @Test
        @DisplayName("getGender throws an exception for male")
        void testGetMaleThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, "2"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(GENDER_API + "/2"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Gender.class, "2");
        }
    }

    @Nested
    @DisplayName("Get Gender Genderless Positive Cases")
    class GetGenderlessPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGender returns the genderless gender")
        void testGetGenderless() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, "3"))
                    .thenReturn(Mono.just(gender_genderless));

            MvcResult mvcResult = this.mockMvc.perform(get(GENDER_API + "/3"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(gender_genderless);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Gender.class, "3");
        }
    }

    @Nested
    @DisplayName("Get Gender Genderless Negative Cases")
    class GetGenderlessNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getGender returns no genderless gender")
        void testGetGenderlessReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, "3"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(GENDER_API + "/3"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Gender.class, "3");
        }

        @Test
        @DisplayName("getGender throws an exception for genderless")
        void testGetGenderlessThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Gender.class, "3"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(GENDER_API + "/3"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Gender.class, "3");
        }
    }
}
