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
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.PokeApiResource;
import skaro.pokeapi.resource.ability.Ability;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AbilityApiTest
{
    @Autowired
    private AbilityApi abilityApi;

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
    @DisplayName("getAllAbilities returns ok")
    void testGetAllAbilities()
    {
        NamedApiResourceList<Ability> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(Ability.class)).thenReturn(Mono.just(response));

        var result = abilityApi.getAllAbilities();

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getAbility returns ok")
    void testGetAbility()
    {
        Ability response = mock(Ability.class);
        when(pokeApiClient.getResource(Ability.class, "1")).thenReturn(Mono.just(response));

        var result = abilityApi.getAbility("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }
}

