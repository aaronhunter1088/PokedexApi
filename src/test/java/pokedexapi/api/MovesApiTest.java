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
import skaro.pokeapi.resource.move.Move;
import skaro.pokeapi.resource.moveailment.MoveAilment;
import skaro.pokeapi.resource.movebattlestyle.MoveBattleStyle;
import skaro.pokeapi.resource.movecategory.MoveCategory;
import skaro.pokeapi.resource.movedamageclass.MoveDamageClass;
import skaro.pokeapi.resource.movelearnmethod.MoveLearnMethod;
import skaro.pokeapi.resource.movetarget.MoveTarget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovesApiTest
{
    @Autowired
    private MovesApi movesApi;

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
    @DisplayName("getMoves returns ok")
    void testGetMoves()
    {
        NamedApiResourceList<Move> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(Move.class)).thenReturn(Mono.just(response));
        var result = movesApi.getMoves();
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getMove returns ok")
    void testGetMove()
    {
        Move response = mock(Move.class);
        when(pokeApiClient.getResource(Move.class, "1")).thenReturn(Mono.just(response));
        var result = movesApi.getMove("1");
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getMoveAilments returns ok")
    void testGetMoveAilments()
    {
        NamedApiResourceList<MoveAilment> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(MoveAilment.class)).thenReturn(Mono.just(response));
        var result = movesApi.getMoveAilments();
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getMoveAilment returns ok")
    void testGetMoveAilment()
    {
        MoveAilment response = mock(MoveAilment.class);
        when(pokeApiClient.getResource(MoveAilment.class, "1")).thenReturn(Mono.just(response));
        var result = movesApi.getMoveAilment("1");
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getMoveBattleStyles returns ok")
    void testGetMoveBattleStyles()
    {
        NamedApiResourceList<MoveBattleStyle> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(MoveBattleStyle.class)).thenReturn(Mono.just(response));
        var result = movesApi.getMoveBattleStyles();
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getMoveBattleStyle returns ok")
    void testGetMoveBattleStyle()
    {
        MoveBattleStyle response = mock(MoveBattleStyle.class);
        when(pokeApiClient.getResource(MoveBattleStyle.class, "1")).thenReturn(Mono.just(response));
        var result = movesApi.getMoveBattleStyle("1");
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getCategories returns ok")
    void testGetCategories()
    {
        NamedApiResourceList<MoveCategory> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(MoveCategory.class)).thenReturn(Mono.just(response));
        var result = movesApi.getCategories();
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getCategory returns ok")
    void testGetCategory()
    {
        MoveCategory response = mock(MoveCategory.class);
        when(pokeApiClient.getResource(MoveCategory.class, "1")).thenReturn(Mono.just(response));
        var result = movesApi.getCategory("1");
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getDamageClasses returns ok")
    void testGetDamageClasses()
    {
        NamedApiResourceList<MoveDamageClass> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(MoveDamageClass.class)).thenReturn(Mono.just(response));
        var result = movesApi.getDamageClasses();
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getDamageClass returns ok")
    void testGetDamageClass()
    {
        MoveDamageClass response = mock(MoveDamageClass.class);
        when(pokeApiClient.getResource(MoveDamageClass.class, "1")).thenReturn(Mono.just(response));
        var result = movesApi.getDamageClass("1");
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getLearnMoves returns ok")
    void testGetLearnMoves()
    {
        NamedApiResourceList<MoveLearnMethod> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(MoveLearnMethod.class)).thenReturn(Mono.just(response));
        var result = movesApi.getLearnMoves();
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getMoveLearnMethod returns ok")
    void testGetMoveLearnMethod()
    {
        MoveLearnMethod response = mock(MoveLearnMethod.class);
        when(pokeApiClient.getResource(MoveLearnMethod.class, "1")).thenReturn(Mono.just(response));
        var result = movesApi.getMoveLearnMethod("1");
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getMoveTargets returns ok")
    void testGetMoveTargets()
    {
        NamedApiResourceList<MoveTarget> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(MoveTarget.class)).thenReturn(Mono.just(response));
        var result = movesApi.getMoveTargets();
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("getMoveTarget returns ok")
    void testGetMoveTarget()
    {
        MoveTarget response = mock(MoveTarget.class);
        when(pokeApiClient.getResource(MoveTarget.class, "1")).thenReturn(Mono.just(response));
        var result = movesApi.getMoveTarget("1");
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }
}

