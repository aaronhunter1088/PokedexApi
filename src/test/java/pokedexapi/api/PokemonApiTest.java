package pokedexapi.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import skaro.pokeapi.resource.pokemon.Pokemon;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains unit tests for the {@link PokemonApi} controller.
 * It uses Spring Boot's testing support to load the application context
 * and configure MockMvc for testing the web layer.
 * <p>
 * Annotations:
 * ActiveProfiles("test"): Activates the 'test' profile for the tests.
 * ExtendWith(SpringExtension.class): Integrates the Spring TestContext Framework with JUnit 5.
 * SpringBootTest: Loads the full application context for integration tests.
 * AutoConfigureMockMvc: Configures MockMvc for testing the web layer.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PokemonApiTest
{
    @Autowired
    private PokemonApi pokemonApi;

    @ParameterizedTest
    @DisplayName("Test validateNameOrId with valid cases")
    @CsvSource({
        "pikachu",
        "25",
        "deoxys"
    })
    void testValidateNameOrIdExpectsTrue(String name)
    {
        ResponseEntity<?> result = pokemonApi.getAPokemon(name);
        Pokemon actual = result.getBody() != null ? (Pokemon) result.getBody() : null;
        assertNotNull(actual);
    }

    @ParameterizedTest
    @DisplayName("Test validateNameOrId with invalid cases")
    @CsvSource({
            "invalidName",
            "-1",
            "0",
            "9999"
    })
    void testValidateNameOrIdExpectsFalse(String name)
    {
        ResponseEntity<?> result = pokemonApi.getAPokemon(name);
        assertEquals(400,  result.getStatusCode().value());
    }
}