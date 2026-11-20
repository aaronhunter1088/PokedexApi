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
import skaro.pokeapi.resource.machine.Machine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MachineApiTest
{
    @Autowired
    private MachineApi machineApi;

    @MockitoBean
    private PokemonApiService pokemonApiService;

    @MockitoBean
    private PokeApiClient pokeApiClient;

    @Test
    @DisplayName("getMachines returns ok")
    void testGetMachines()
    {
        @SuppressWarnings("unchecked")
        NamedApiResourceList<Machine> response = (NamedApiResourceList<Machine>) mock(NamedApiResourceList.class);
        when(pokeApiClient.getResource(eq(Machine.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = machineApi.getMachines(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getMachine returns ok")
    void testGetMachine()
    {
        Machine response = mock(Machine.class);
        when(pokeApiClient.getResource(Machine.class, "1")).thenReturn(Mono.just(response));

        var result = machineApi.getMachine("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }
}
