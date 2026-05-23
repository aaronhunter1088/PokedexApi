package pokedexapi.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pokedexapi.service.PokemonApiService;
import reactor.core.publisher.Mono;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.PokeApiResource;
import skaro.pokeapi.resource.pokedex.Pokedex;
import skaro.pokeapi.resource.version.Version;
import skaro.pokeapi.resource.versiongroup.VersionGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GameApiTest
{
    @Autowired
    private GameApi gameApi;

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
    @DisplayName("getPokedexes returns ok")
    void testGetPokedexes()
    {
        NamedApiResourceList<Pokedex> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(Pokedex.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = gameApi.getPokedexes(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getPokedex returns ok")
    void testGetPokedex()
    {
        Pokedex response = mock(Pokedex.class);
        when(pokeApiClient.getResource(Pokedex.class, "1")).thenReturn(Mono.just(response));

        var result = gameApi.getPokedex(1);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getVersions returns ok")
    void testGetVersions()
    {
        NamedApiResourceList<Version> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(Version.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = gameApi.getVersions(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getVersion returns ok")
    void testGetVersion()
    {
        Version response = mock(Version.class);
        when(pokeApiClient.getResource(Version.class, "1")).thenReturn(Mono.just(response));

        var result = gameApi.getVersion(1);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getVersionGroups returns ok")
    void testGetVersionGroups()
    {
        NamedApiResourceList<VersionGroup> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(VersionGroup.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = gameApi.getVersionGroups(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getVersionGroup returns ok")
    void testGetVersionGroup()
    {
        VersionGroup response = mock(VersionGroup.class);
        when(pokeApiClient.getResource(VersionGroup.class, "1")).thenReturn(Mono.just(response));

        var result = gameApi.getVersionGroup(1);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }
}

