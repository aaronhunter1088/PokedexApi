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
import pokedexapi.service.PokemonApiService;
import reactor.core.publisher.Mono;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.PokeApiResource;
import skaro.pokeapi.resource.location.Location;
import skaro.pokeapi.resource.locationarea.LocationArea;
import skaro.pokeapi.resource.palparkarea.PalParkArea;
import skaro.pokeapi.resource.region.Region;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LocationApiTest
{
    @Autowired
    private LocationApi locationApi;

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
    @DisplayName("getLocations returns ok")
    void testGetLocations()
    {
        NamedApiResourceList<Location> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(Location.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = locationApi.getLocations(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getLocation returns ok")
    void testGetLocation()
    {
        Location response = mock(Location.class);
        when(pokeApiClient.getResource(Location.class, "1")).thenReturn(Mono.just(response));

        var result = locationApi.getLocation("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getLocationAreas returns ok")
    void testGetLocationAreas()
    {
        NamedApiResourceList<LocationArea> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(LocationArea.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = locationApi.getLocationAreas(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getLocationArea returns ok")
    void testGetLocationArea()
    {
        LocationArea response = mock(LocationArea.class);
        when(pokeApiClient.getResource(LocationArea.class, "1")).thenReturn(Mono.just(response));

        var result = locationApi.getLocationArea("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getPalParkAreas returns ok")
    void testGetPalParkAreas()
    {
        NamedApiResourceList<PalParkArea> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(PalParkArea.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = locationApi.getPalParkAreas(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getPalParkArea returns ok")
    void testGetPalParkArea()
    {
        PalParkArea response = mock(PalParkArea.class);
        when(pokeApiClient.getResource(PalParkArea.class, "1")).thenReturn(Mono.just(response));

        var result = locationApi.getPalParkArea("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getRegions returns ok")
    void testGetRegions()
    {
        NamedApiResourceList<Region> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(Region.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = locationApi.getRegions(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getRegion returns ok")
    void testGetRegion()
    {
        Region response = mock(Region.class);
        when(pokeApiClient.getResource(Region.class, "1")).thenReturn(Mono.just(response));

        var result = locationApi.getRegion("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }
}

