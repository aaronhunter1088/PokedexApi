package pokedexapi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static pokedexapi.utilities.Constants.MY_POKEAPI_BASE_URL;

class PokemonLocationEncounterServiceTest
{
    @Test
    @DisplayName("interface has expected HttpExchange metadata")
    void testHttpExchangeAnnotation()
    {
        HttpExchange annotation = PokemonLocationEncounterService.class.getAnnotation(HttpExchange.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.url()).isEqualTo(MY_POKEAPI_BASE_URL);
        assertThat(annotation.accept()).containsExactly("application/json");
    }

    @Test
    @DisplayName("getPokemonLocationEncounters method has expected annotations")
    void testGetPokemonLocationEncountersMethodAnnotations() throws NoSuchMethodException
    {
        Method method = PokemonLocationEncounterService.class
                .getMethod("getPokemonLocationEncounters", Integer.class);

        GetExchange getExchange = method.getAnnotation(GetExchange.class);
        ResponseBody responseBody = method.getAnnotation(ResponseBody.class);

        assertThat(getExchange).isNotNull();
        assertThat(getExchange.value()).isEqualTo("/pokemon/{id}/encounters");
        assertThat(responseBody).isNotNull();
    }
}

