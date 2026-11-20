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
import skaro.pokeapi.resource.encountercondition.EncounterCondition;
import skaro.pokeapi.resource.encounterconditionvalue.EncounterConditionValue;
import skaro.pokeapi.resource.encountermethod.EncounterMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EncounterApiTest
{
    @Autowired
    private EncounterApi encounterApi;

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
    @DisplayName("getEncounterMethods returns ok")
    void testGetEncounterMethods()
    {
        NamedApiResourceList<EncounterMethod> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(EncounterMethod.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = encounterApi.getEncounterMethods(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getEncounterMethod returns ok")
    void testGetEncounterMethod()
    {
        EncounterMethod response = mock(EncounterMethod.class);
        when(pokeApiClient.getResource(EncounterMethod.class, "1")).thenReturn(Mono.just(response));

        var result = encounterApi.getEncounterMethod("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getConditions returns ok")
    void testGetConditions()
    {
        NamedApiResourceList<EncounterCondition> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(EncounterCondition.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = encounterApi.getConditions(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getCondition returns ok")
    void testGetCondition()
    {
        EncounterCondition response = mock(EncounterCondition.class);
        when(pokeApiClient.getResource(EncounterCondition.class, "1")).thenReturn(Mono.just(response));

        var result = encounterApi.getCondition("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getConditionValues returns ok")
    void testGetConditionValues()
    {
        NamedApiResourceList<EncounterConditionValue> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(eq(EncounterConditionValue.class), any(PageQuery.class))).thenReturn(Mono.just(response));

        var result = encounterApi.getConditionValues(10, 0);

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(response.results());
    }

    @Test
    @DisplayName("getConditionValue returns ok")
    void testGetConditionValue()
    {
        EncounterConditionValue response = mock(EncounterConditionValue.class);
        when(pokeApiClient.getResource(EncounterConditionValue.class, "1")).thenReturn(Mono.just(response));

        var result = encounterApi.getConditionValue("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }
}

