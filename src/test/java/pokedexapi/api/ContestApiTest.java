package pokedexapi.api;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import skaro.pokeapi.query.PageQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pokedexapi.service.PokemonApiService;
import reactor.core.publisher.Mono;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.PokeApiResource;
import skaro.pokeapi.resource.contesteffect.ContestEffect;
import skaro.pokeapi.resource.contesttype.ContestType;
import skaro.pokeapi.resource.supercontesteffect.SuperContestEffect;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContestApiTest
{
    @Autowired
    private ContestApi contestApi;

    @MockitoBean
    private PokeApiClient pokeApiClient;

    @MockitoBean
    private PokemonApiService pokemonApiService;

    @SuppressWarnings("unchecked")
    private <T extends PokeApiResource> NamedApiResourceList<T> mockNamedApiResourceList()
    {
        return (NamedApiResourceList<T>) mock(NamedApiResourceList.class);
    }

    @Test
    @DisplayName("getAllContests returns ok")
    void testGetAllContests()
    {
        NamedApiResourceList<ContestType> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(ContestType.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = contestApi.getAllContests(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getContestType returns ok")
    void testGetContestType()
    {
        ContestType response = mock(ContestType.class);
        when(pokeApiClient.getResource(ContestType.class, "1")).thenReturn(Mono.just(response));

        var result = contestApi.getContestType("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getAllContestEffect returns ok")
    void testGetAllContestEffect()
    {
        NamedApiResourceList<ContestEffect> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(ContestEffect.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = contestApi.getAllContestEffect(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getContestEffect handles invalid base URI")
    void testGetContestEffect()
    {
        ReflectionTestUtils.setField(contestApi, "pokeApiBaseUrl", "::bad-uri::");

        var result = contestApi.getContestEffect("1");

        assertThat(result.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    @DisplayName("getAllSuperContestEffect returns ok")
    void testGetAllSuperContestEffect()
    {
        NamedApiResourceList<SuperContestEffect> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(SuperContestEffect.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = contestApi.getAllSuperContestEffect(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getSuperContestEffect handles invalid base URI")
    void testGetSuperContestEffect()
    {
        ReflectionTestUtils.setField(contestApi, "pokeApiBaseUrl", "::bad-uri::");

        var result = contestApi.getSuperContestEffect("1");

        assertThat(result.getStatusCode().value()).isEqualTo(500);
    }
}

