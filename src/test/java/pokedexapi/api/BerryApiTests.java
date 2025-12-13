package pokedexapi.api;

import pokedexapi.controller.BaseApiTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResource;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.PokeApiResource;
import skaro.pokeapi.resource.berry.Berry;
import skaro.pokeapi.resource.berryfirmness.BerryFirmness;
import skaro.pokeapi.resource.berryflavor.BerryFlavor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * This class contains integration tests for the BerryApi controller.
 * It uses Spring Boot's testing support to load the application context
 * and configure MockMvc for testing the web layer.
 * <p>
 * Annotations:
 * ActiveProfiles("test"): Activates the 'test' profile for the tests.
 * ExtendWith(SpringExtension.class): Integrates the Spring TestContext Framework with JUnit 5.
 * SpringBootTest: Loads the full application context for integration tests.
 * AutoConfigureMockMvc: Configures MockMvc for testing the web layer.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BerryApiTests extends BaseApiTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String BERRY_API = "/berry";

    NamedApiResourceList<Berry> getAllBerriesResponse;
    NamedApiResourceList<Berry> getNext10BerriesResponse;
    Berry berry_1;
    NamedApiResourceList<BerryFirmness> getAllBerryFirmnessResponse;
    BerryFirmness berryFirmness_1;
    NamedApiResourceList<BerryFlavor> getAllBerryFlavorsResponse;
    BerryFlavor berryFlavor_1;

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
        berry_1 = objectMapper.readValue(
                new ClassPathResource("berryApi/berry_1.json").getFile(),
                Berry.class
        );
        getAllBerryFirmnessResponse = objectMapper.readValue(
                new ClassPathResource("berryApi/getAllBerryFirmnessResponse.json").getFile(),
                new TypeReference<NamedApiResourceList<BerryFirmness>>() {}
        );
        berryFirmness_1 = objectMapper.readValue(
                new ClassPathResource("berryApi/berryFirmness_1.json").getFile(),
                BerryFirmness.class
        );
        getAllBerryFlavorsResponse = objectMapper.readValue(
                new ClassPathResource("berryApi/getAllBerryFlavorResponse.json").getFile(),
                new TypeReference<NamedApiResourceList<BerryFlavor>>() {}
        );
        berryFlavor_1 = objectMapper.readValue(
                new ClassPathResource("berryApi/berryFlavor_1.json").getFile(),
                BerryFlavor.class
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
        Mono<?> mono = getMonoFromListResponse(getAllBerriesResponse, new NamedApiResource<Berry>());
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
        Mono<?> mono = getMonoFromListResponse(getNext10BerriesResponse, new NamedApiResource<Berry>());
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

    //

    @Test
    @DisplayName("Test getBerry returns no berry")
    void testGetBerryReturnsNullResponse() throws Exception {
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(Mono.empty());
        this.mockMvc.perform(get(BERRY_API + '/' + berry_1.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo("Could not find a berry with " + berry_1.getId());
                });
    }

    @Test
    @DisplayName("Test getBerry returns expected berry")
    void testGetBerryReturnsExpectedBerry() throws Exception {
        Mono<PokeApiResource> mono = Mono.just(berry_1);
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(mono);
        this.mockMvc.perform(get(BERRY_API + '/' + berry_1.getId()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    Berry response = objectMapper.readValue(contentAsString, Berry.class);
                    assertThat(response.getName()).isEqualTo(berry_1.getName());
                    assertThat(response.getId()).isEqualTo(berry_1.getId());
                });
    }

    @Test
    @DisplayName("Test getBerry returns an exception")
    void testGetBerryThrowsAnException() throws Exception {
        when(pokeApiClient.getResource(any(), anyString())).thenThrow(new RuntimeException("From testGetBerryThrowsAnException"));
        this.mockMvc.perform(get(BERRY_API + '/' + berry_1.getId()))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo("From testGetBerryThrowsAnException");
                });
    }

    //

    @Test
    @DisplayName("Test getBerryFirmness returns no berry firmnesses")
    void testGetAllBerryFirmnessReturnsNullResponse() throws Exception {
        when(pokeApiClient.getResource(any(), any(PageQuery.class))).thenReturn(Mono.empty());
        this.mockMvc.perform(get(BERRY_API + "/berry-firmness"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo("Could not access berry-firmness endpoint");
                });
    }

    @Test
    @DisplayName("Test getBerryFirmness returns the expected results")
    void testGetBerryFirmness() throws Exception {
        Mono<?> mono = getMonoFromListResponse(getAllBerryFirmnessResponse, new NamedApiResource<BerryFirmness>());
        when(pokeApiClient.getResource(any(), any(PageQuery.class))).thenReturn((Mono<NamedApiResourceList<PokeApiResource>>) mono);
        this.mockMvc.perform(get(BERRY_API + "/berry-firmness"))
                //.andExpect(status().is(200))
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    NamedApiResourceList<Berry> response = objectMapper.readValue(contentAsString, new TypeReference<>() {});
                    List<NamedApiResource<Berry>> berries = response.getResults();
                    assertThat(berries).size().isEqualTo(getAllBerryFirmnessResponse.getResults().size());
                    assertThat(response.getCount()).isEqualTo(getAllBerryFirmnessResponse.getCount());
                    assertThat(response.getNext()).isEqualTo(null);
                    assertThat(response.getPrevious()).isEqualTo(null);
                });
    }

    @Test
    @DisplayName("Test getBerryFirmness returns an exception")
    void testGetBerryFirmnessThrowsAnException() throws Exception {
        when(pokeApiClient.getResource(any(), any(PageQuery.class))).thenThrow(new RuntimeException("From testGetBerryFirmnessThrowsAnException"));
        this.mockMvc.perform(get(BERRY_API + "/berry-firmness"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo("From testGetBerryFirmnessThrowsAnException");
                });
    }

    //

    @Test
    @DisplayName("Test getBerryFirmness returns no berry firmness")
    void testGetBerryFirmnessReturnsNullResponse() throws Exception {
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(Mono.empty());
        this.mockMvc.perform(get(BERRY_API + "/berry-firmness/" + berryFirmness_1.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo("Could not find berry-firmness with " + berry_1.getId());
                });
    }

    @Test
    @DisplayName("Test getBerryFirmness returns expected berry firmness")
    void testGetBerryFirmnessReturnsExpectedBerryFirmness() throws Exception {
        Mono<PokeApiResource> mono = Mono.just(berryFirmness_1);
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(mono);
        this.mockMvc.perform(get(BERRY_API + "/berry-firmness/" + berryFirmness_1.getId()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    BerryFirmness response = objectMapper.readValue(contentAsString, BerryFirmness.class);
                    assertThat(response.getName()).isEqualTo(berryFirmness_1.getName());
                    assertThat(response.getId()).isEqualTo(berryFirmness_1.getId());
                });
    }

    @Test
    @DisplayName("Test getBerryFirmness/1 returns an exception")
    void testGetBerryFirmness1ThrowsAnException() throws Exception {
        when(pokeApiClient.getResource(any(), anyString())).thenThrow(new RuntimeException("From testGetBerryFirmness1ThrowsAnException"));
        this.mockMvc.perform(get(BERRY_API + "/berry-firmness/" + berryFirmness_1.getId()))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo("From testGetBerryFirmness1ThrowsAnException");
                });
    }

    //

    @Test
    @DisplayName("Test getBerryFlavor returns no berry flavors")
    void testGetAllBerryFlavorsReturnsNullResponse() throws Exception {
        when(pokeApiClient.getResource(any(), any(PageQuery.class))).thenReturn(Mono.empty());
        this.mockMvc.perform(get(BERRY_API + "/berry-flavor"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo("Could not access berry-flavor endpoint");
                });
    }

    @Test
    @DisplayName("Test getBerryFlavors returns the expected results")
    void testGetBerryFlavors() throws Exception {
        Mono<?> mono = getMonoFromListResponse(getAllBerryFlavorsResponse, new NamedApiResource<BerryFlavor>());
        when(pokeApiClient.getResource(any(), any(PageQuery.class))).thenReturn((Mono<NamedApiResourceList<PokeApiResource>>) mono);
        this.mockMvc.perform(get(BERRY_API + "/berry-flavor"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    NamedApiResourceList<Berry> response = objectMapper.readValue(contentAsString, new TypeReference<>() {});
                    List<NamedApiResource<Berry>> berries = response.getResults();
                    assertThat(berries).size().isEqualTo(getAllBerryFlavorsResponse.getResults().size());
                    assertThat(response.getCount()).isEqualTo(getAllBerryFlavorsResponse.getCount());
                    assertThat(response.getNext()).isEqualTo(null);
                    assertThat(response.getPrevious()).isEqualTo(null);
                });
    }

    @Test
    @DisplayName("Test getBerryFlavors returns an exception")
    void testGetBerryFlavorsThrowsAnException() throws Exception {
        when(pokeApiClient.getResource(any(), any(PageQuery.class))).thenThrow(new RuntimeException("From getBerryFlavorsThrowsAnException"));
        this.mockMvc.perform(get(BERRY_API + "/berry-flavor"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo("From getBerryFlavorsThrowsAnException");
                });
    }

    //

    @Test
    @DisplayName("Test getBerryFlavor/1 returns no berry flavor")
    void testGetBerryFlavorReturnsNullResponse() throws Exception {
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(Mono.empty());
        this.mockMvc.perform(get(BERRY_API + "/berry-flavor/" + berryFlavor_1.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo("Could not find berry-flavor with " + berryFlavor_1.getId());
                });
    }

    @Test
    @DisplayName("Test getBerryFlavor/1 returns expected berry flavor")
    void testGetBerryFlavorReturnsExpectedBerryFlavor() throws Exception {
        Mono<PokeApiResource> mono = Mono.just(berryFlavor_1);
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(mono);
        this.mockMvc.perform(get(BERRY_API + "/berry-flavor/" + berryFlavor_1.getId()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    BerryFlavor response = objectMapper.readValue(contentAsString, BerryFlavor.class);
                    assertThat(response.getName()).isEqualTo(berryFlavor_1.getName());
                    assertThat(response.getId()).isEqualTo(berryFlavor_1.getId());
                });
    }

    @Test
    @DisplayName("Test getBerryFlavor/1 returns an exception")
    void testGetBerryFlavor1ThrowsAnException() throws Exception {
        when(pokeApiClient.getResource(any(), anyString())).thenThrow(new RuntimeException("From testGetBerryFlavor1ThrowsAnException"));
        this.mockMvc.perform(get(BERRY_API + "/berry-flavor/" + berryFlavor_1.getId()))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    final var contentAsString = result.getResponse().getContentAsString();
                    assertThat(contentAsString).isEqualTo("From testGetBerryFlavor1ThrowsAnException");
                });
    }
}
