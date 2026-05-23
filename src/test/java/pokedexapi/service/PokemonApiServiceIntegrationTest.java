package pokedexapi.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PokemonApiServiceIntegrationTest
{
    @Autowired
    private PokemonApiService pokemonApiService;

    @Test
    @DisplayName("getPokemonByIdOrName with monchumon returns null without throwing")
    void testGetPokemonByIdOrNameWithMonchumonReturnsNullWithoutMockingMethod()
    {
        var result = assertDoesNotThrow(() -> pokemonApiService.getPokemonByIdOrName("monchumon"));

        assertThat(result).isNull();
    }
}

