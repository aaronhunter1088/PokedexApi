package com.example.pokedexapi.api;

//import com.example.pokedexapi.config.ObjectMapperConfig;
import com.example.pokedexapi.BaseApiTest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;
import skaro.pokeapi.resource.NamedApiResource;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.PokeApiResource;
import skaro.pokeapi.resource.berry.Berry;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * WebMvcTest(ResourceClass.class)
 * Configures the test class to focus only on the
 * web layer of the BerryApi controller. This means
 * that only the web-related components (like controllers,
 * filters, and MockMvc) are instantiated, making the test
 * lightweight and faster. This setup is useful for testing
 * the controller's request handling and response without
 * involving the full application context.
 */
//@TestPropertySource(locations="classpath:application-test.properties")
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BerryApiTests extends BaseApiTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BERRY_API = "/berry";

    NamedApiResourceList<Berry> getAllBerriesResponse;

    NamedApiResourceList<Berry> getNext10BerriesResponse;

    @BeforeEach
    void setUpEach() throws IOException {
        getAllBerriesResponse = objectMapper.readValue(
                new ClassPathResource("berryApi/getAllBerriesResponse.json").getFile(),
                new TypeReference<NamedApiResourceList<Berry>>() {}
        );
        getNext10BerriesResponse = objectMapper.readValue(
                new ClassPathResource("berryApi/getNext10BerriesResponse.json").getFile(),
                new TypeReference<NamedApiResourceList<Berry>>() {}
        );
    }

    @Test
    @DisplayName("Test getAllBerries returns no berries")
    void testGetAllBerriesReturnsNullResponse() throws Exception {
        when(pokeApiClient.getResource(any())).thenReturn(Mono.empty());
        this.mockMvc.perform(get(BERRY_API)).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test getAllBerries returns the first 10 results")
    void testGetAllBerries() throws Exception {
        //NamedApiResourceList<Berry> just = Mono.just(getAllBerriesResponse).block();
        Mono<?> mono = Mono.just(getAllBerriesResponse);
        when(pokeApiClient.getResource(any())).thenReturn((Mono<NamedApiResourceList<PokeApiResource>>) mono);
        this.mockMvc.perform(get(BERRY_API)).andExpect(status()
                .isOk())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    NamedApiResourceList<Berry> response = objectMapper.readValue(contentAsString, new TypeReference<>() {});
                    List<NamedApiResource<Berry>> berries = response.getResults();
                    assertThat(berries).size().isEqualTo(getAllBerriesResponse.getResults().size());
                    assertThat(response.getCount()).isEqualTo(getAllBerriesResponse.getCount());
                    assertThat(response.getNext()).isEqualTo(getAllBerriesResponse.getNext());
                    assertThat(response.getPrevious()).isEqualTo(null);
                });
    }

    @Test
    @DisplayName("Test getAllBerries returns the second next 10 results")
    void testNextTenBerries() throws Exception {
        NamedApiResourceList<Berry> just = Mono.just(getNext10BerriesResponse).block();
        Mono<?> mono = Mono.just(getNext10BerriesResponse);
        when(pokeApiClient.getResource(any())).thenReturn((Mono<NamedApiResourceList<PokeApiResource>>) mono);
        this.mockMvc.perform(get(BERRY_API)).andExpect(status()
                        .isOk())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    NamedApiResourceList<Berry> response = objectMapper.readValue(contentAsString, new TypeReference<>() {});
                    List<NamedApiResource<Berry>> berries = response.getResults();
                    assertThat(berries).size().isEqualTo(getNext10BerriesResponse.getResults().size());
                    assertThat(response.getCount()).isEqualTo(getNext10BerriesResponse.getCount());
                    assertThat(response.getNext()).isEqualTo(getNext10BerriesResponse.getNext());
                    assertThat(response.getPrevious()).isEqualTo(getNext10BerriesResponse.getPrevious());
                });
    }

    @Test
    @DisplayName("Test getAllBerries returns an exception")
    void testGetAllBerriesThrowsAnException() throws Exception {
        when(pokeApiClient.getResource(any())).thenThrow(new RuntimeException("From testGetAllBerriesThrowsAnException"));
        this.mockMvc.perform(get(BERRY_API)).andExpect(status().isInternalServerError());
    }
}
