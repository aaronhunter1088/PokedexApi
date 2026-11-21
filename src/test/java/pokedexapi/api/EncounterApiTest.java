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
import skaro.pokeapi.resource.encountercondition.EncounterCondition;
import skaro.pokeapi.resource.encounterconditionvalue.EncounterConditionValue;
import skaro.pokeapi.resource.encountermethod.EncounterMethod;
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
class EncounterApiTest extends BaseApiTest
{
    private static final String ENCOUNTER_API = "/encounter";
    private static NamedApiResourceList<EncounterMethod> getAllEncounterMethodsResponse;
    private static NamedApiResourceList<EncounterMethod> getNext10EncounterMethodsResponse;
    private static NamedApiResourceList<EncounterCondition> getAllEncounterConditionsResponse;
    private static NamedApiResourceList<EncounterCondition> getNext10EncounterConditionsResponse;
    private static NamedApiResourceList<EncounterConditionValue> getAllEncounterConditionValuesResponse;
    private static NamedApiResourceList<EncounterConditionValue> getNext10EncounterConditionValuesResponse;
    private static EncounterMethod encounterMethod_1;
    private static EncounterCondition encounterCondition_1;
    private static EncounterConditionValue encounterConditionValue_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllEncounterMethodsResponse = jsonMapper.readValue(
                new ClassPathResource("encounterApi/getAllEncounterMethodsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10EncounterMethodsResponse = jsonMapper.readValue(
                new ClassPathResource("encounterApi/getNext10EncounterMethodsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getAllEncounterConditionsResponse = jsonMapper.readValue(
                new ClassPathResource("encounterApi/getAllEncounterConditionsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10EncounterConditionsResponse = jsonMapper.readValue(
                new ClassPathResource("encounterApi/getNext10EncounterConditionsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getAllEncounterConditionValuesResponse = jsonMapper.readValue(
                new ClassPathResource("encounterApi/getAllEncounterConditionValuesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10EncounterConditionValuesResponse = jsonMapper.readValue(
                new ClassPathResource("encounterApi/getNext10EncounterConditionValuesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        encounterMethod_1 = jsonMapper.readValue(
                new ClassPathResource("encounterApi/encounter_method_1.json").getFile(),
                EncounterMethod.class
        );
        encounterCondition_1 = jsonMapper.readValue(
                new ClassPathResource("encounterApi/encounter_condition_1.json").getFile(),
                EncounterCondition.class
        );
        encounterConditionValue_1 = jsonMapper.readValue(
                new ClassPathResource("encounterApi/encounter_condition_value_1.json").getFile(),
                EncounterConditionValue.class
        );
    }

    @Nested
    @DisplayName("Get All Encounter Methods Positive Cases")
    class GetAllEncounterMethodsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Methods returns the first page")
        void testGetAllEncounterMethods() throws Exception
        {
            when(pokeApiClient.getResource(EncounterMethod.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllEncounterMethodsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-method"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllEncounterMethodsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EncounterMethod.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Encounter Methods returns the second page (offset is a page index)")
        void testGetAllEncounterMethodsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(EncounterMethod.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10EncounterMethodsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-method" + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10EncounterMethodsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EncounterMethod.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Encounter Methods Negative Cases")
    class GetAllEncounterMethodsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Methods returns no results")
        void testGetAllEncounterMethodsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(EncounterMethod.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-method"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EncounterMethod.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Encounter Methods returns null")
        void testGetAllEncounterMethodsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(EncounterMethod.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-method"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EncounterMethod.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Encounter Methods throws an exception")
        void testGetAllEncounterMethodsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EncounterMethod.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-method"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EncounterMethod.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Encounter Method Positive Cases")
    class GetEncounterMethodPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Method 1 returns the expected result")
        void testGetEncounterMethod() throws Exception
        {
            when(pokeApiClient.getResource(EncounterMethod.class, "1"))
                    .thenReturn(Mono.just(encounterMethod_1));

            MvcResult mvcResult = this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-method/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(encounterMethod_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EncounterMethod.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Encounter Method Negative Cases")
    class GetEncounterMethodNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Method 1 returns no result")
        void testGetEncounterMethodReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(EncounterMethod.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-method/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EncounterMethod.class, "1");
        }

        @Test
        @DisplayName("Encounter Method 1 throws an exception")
        void testGetEncounterMethodThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EncounterMethod.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-method/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EncounterMethod.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All Encounter Conditions Positive Cases")
    class GetAllEncounterConditionsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Conditions returns the first page")
        void testGetAllEncounterConditions() throws Exception
        {
            when(pokeApiClient.getResource(EncounterCondition.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllEncounterConditionsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllEncounterConditionsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EncounterCondition.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Encounter Conditions returns the second page (offset is a page index)")
        void testGetAllEncounterConditionsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(EncounterCondition.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10EncounterConditionsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition" + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10EncounterConditionsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EncounterCondition.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Encounter Conditions Negative Cases")
    class GetAllEncounterConditionsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Conditions returns no results")
        void testGetAllEncounterConditionsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(EncounterCondition.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EncounterCondition.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Encounter Conditions returns null")
        void testGetAllEncounterConditionsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(EncounterCondition.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EncounterCondition.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Encounter Conditions throws an exception")
        void testGetAllEncounterConditionsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EncounterCondition.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EncounterCondition.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Encounter Condition Positive Cases")
    class GetEncounterConditionPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Condition 1 returns the expected result")
        void testGetEncounterCondition() throws Exception
        {
            when(pokeApiClient.getResource(EncounterCondition.class, "1"))
                    .thenReturn(Mono.just(encounterCondition_1));

            MvcResult mvcResult = this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(encounterCondition_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EncounterCondition.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Encounter Condition Negative Cases")
    class GetEncounterConditionNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Condition 1 returns no result")
        void testGetEncounterConditionReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(EncounterCondition.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EncounterCondition.class, "1");
        }

        @Test
        @DisplayName("Encounter Condition 1 throws an exception")
        void testGetEncounterConditionThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EncounterCondition.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EncounterCondition.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All Encounter Condition Values Positive Cases")
    class GetAllEncounterConditionValuesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Condition Values returns the first page")
        void testGetAllEncounterConditionValues() throws Exception
        {
            when(pokeApiClient.getResource(EncounterConditionValue.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllEncounterConditionValuesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition-value"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllEncounterConditionValuesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EncounterConditionValue.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Encounter Condition Values returns the second page (offset is a page index)")
        void testGetAllEncounterConditionValuesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(EncounterConditionValue.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10EncounterConditionValuesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition-value" + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10EncounterConditionValuesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EncounterConditionValue.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Encounter Condition Values Negative Cases")
    class GetAllEncounterConditionValuesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Condition Values returns no results")
        void testGetAllEncounterConditionValuesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(EncounterConditionValue.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition-value"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EncounterConditionValue.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Encounter Condition Values returns null")
        void testGetAllEncounterConditionValuesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(EncounterConditionValue.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition-value"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EncounterConditionValue.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("Encounter Condition Values throws an exception")
        void testGetAllEncounterConditionValuesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EncounterConditionValue.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition-value"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EncounterConditionValue.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Encounter Condition Value Positive Cases")
    class GetEncounterConditionValuePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Condition Value 1 returns the expected result")
        void testGetEncounterConditionValue() throws Exception
        {
            when(pokeApiClient.getResource(EncounterConditionValue.class, "1"))
                    .thenReturn(Mono.just(encounterConditionValue_1));

            MvcResult mvcResult = this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition-value/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(encounterConditionValue_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(EncounterConditionValue.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Encounter Condition Value Negative Cases")
    class GetEncounterConditionValueNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("Encounter Condition Value 1 returns no result")
        void testGetEncounterConditionValueReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(EncounterConditionValue.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition-value/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(EncounterConditionValue.class, "1");
        }

        @Test
        @DisplayName("Encounter Condition Value 1 throws an exception")
        void testGetEncounterConditionValueThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(EncounterConditionValue.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ENCOUNTER_API + "/encounter-condition-value/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(EncounterConditionValue.class, "1");
        }
    }
}
