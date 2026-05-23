package pokedexapi.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pokedexapi.service.PokemonApiService;
import skaro.pokeapi.client.PokeApiClient;

import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CharacteristicApiTest
{
    @Autowired
    private CharacteristicApi characteristicApi;

    @MockitoBean
    private PokemonApiService pokemonApiService;

    @MockitoBean
    private PokeApiClient pokeApiClient;

    @Test
    @DisplayName("getCharacteristics returns ok")
    void testGetCharacteristics() throws Exception
    {
        @SuppressWarnings("unchecked")
        HttpResponse<String> response = (HttpResponse<String>) mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn("{\"results\":[]}");
        when(pokemonApiService.callUrl(org.mockito.ArgumentMatchers.contains("characteristic?limit=10&offset=0")))
                .thenReturn(response);

        var result = characteristicApi.getCharacteristics(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo("{\"results\":[]}");
    }

    @Test
    @DisplayName("getCharacteristic returns ok")
    void testGetCharacteristic() throws Exception
    {
        @SuppressWarnings("unchecked")
        HttpResponse<String> response = (HttpResponse<String>) mock(HttpResponse.class);
        when(response.body()).thenReturn("{\"id\":1}");
        when(pokemonApiService.callUrl(org.mockito.ArgumentMatchers.contains("characteristic/1")))
                .thenReturn(response);

        var result = characteristicApi.getCharacteristic("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo("{\"id\":1}");
    }
}

