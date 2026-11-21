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
import skaro.pokeapi.resource.characteristic.Characteristic;
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
class CharacteristicApiTest extends BaseApiTest
{
    private static final String CHARACTERISTIC_API = "/characteristic";
    private static NamedApiResourceList<Characteristic> getAllCharacteristicsResponse;
    private static NamedApiResourceList<Characteristic> getNext10CharacteristicsResponse;
    private static Characteristic characteristic_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllCharacteristicsResponse = jsonMapper.readValue(
                new ClassPathResource("characteristicApi/getAllCharacteristicsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10CharacteristicsResponse = jsonMapper.readValue(
                new ClassPathResource("characteristicApi/getNext10CharacteristicsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        characteristic_1 = jsonMapper.readValue(
                new ClassPathResource("characteristicApi/characteristic_1.json").getFile(),
                Characteristic.class
        );
    }

    @Nested
    @DisplayName("Get All Characteristics Positive Cases")
    class GetAllCharacteristicsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getCharacteristics returns the first page")
        void testGetAllCharacteristics() throws Exception
        {
            when(pokeApiClient.getResource(Characteristic.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllCharacteristicsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(CHARACTERISTIC_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllCharacteristicsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Characteristic.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getCharacteristics returns the second page (offset is a page index)")
        void testGetAllCharacteristicsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Characteristic.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10CharacteristicsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(CHARACTERISTIC_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10CharacteristicsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Characteristic.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Characteristics Negative Cases")
    class GetAllCharacteristicsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getCharacteristics returns no characteristics")
        void testGetAllCharacteristicsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Characteristic.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(CHARACTERISTIC_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Characteristic.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getCharacteristics returns null characteristics")
        void testGetAllCharacteristicsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Characteristic.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(CHARACTERISTIC_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Characteristic.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getCharacteristics throws an exception")
        void testGetAllCharacteristicsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Characteristic.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(CHARACTERISTIC_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Characteristic.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Characteristic Positive Cases")
    class GetCharacteristicPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getCharacteristic returns the 1st characteristic")
        void testGetCharacteristic() throws Exception
        {
            when(pokeApiClient.getResource(Characteristic.class, "1"))
                    .thenReturn(Mono.just(characteristic_1));

            MvcResult mvcResult = this.mockMvc.perform(get(CHARACTERISTIC_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(characteristic_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Characteristic.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Characteristic Negative Cases")
    class GetCharacteristicNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getCharacteristic returns no characteristic")
        void testGetCharacteristicReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Characteristic.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(CHARACTERISTIC_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Characteristic.class, "1");
        }

        @Test
        @DisplayName("getCharacteristic throws an exception")
        void testGetCharacteristicThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Characteristic.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(CHARACTERISTIC_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Characteristic.class, "1");
        }
    }
}
