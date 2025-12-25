package pokedexapi.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pokedexapi.controllers.BaseApiTest;
import skaro.pokeapi.resource.pokemon.Pokemon;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Used to integrated JUnit 5's Spring TestContext
 * Framework into JUnit 5's Jupiter programming model.
 * It provides support for loading a Spring ApplicationContext
 * and having beans @Autowired into your test instance.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PokemonTest extends BaseApiTest {

    skaro.pokeapi.resource.pokemon.Pokemon skaroPichu;
    skaro.pokeapi.resource.pokemon.Pokemon skaroPikachu;
    Pokemon ditto;
    Pokemon pichu;
    Pokemon pikachu;

    @BeforeEach
    void setUp() throws IOException {
        skaroPichu = jsonMapper.readValue(
                new ClassPathResource("entity/pichu.json").getFile(),
                skaro.pokeapi.resource.pokemon.Pokemon.class
        );
        skaroPikachu = jsonMapper.readValue(
                new ClassPathResource("entity/pikachu.json").getFile(),
                skaro.pokeapi.resource.pokemon.Pokemon.class
        );
        pichu = Pokemon.from(skaroPichu, Collections.emptyMap());
        pikachu = Pokemon.from(skaroPikachu, Collections.emptyMap());
        ditto = Pokemon.from(skaroPikachu, Collections.emptyMap());

        assertEquals("pikachu", skaroPikachu.getName());
        assertSame(25, skaroPikachu.getId());
    }

    @Test
    @DisplayName("Test pokemon toString are the same")
    void testPokemonToString() {
        assertEquals(ditto.toString(), pikachu.toString());
    }

    @Test
    @DisplayName("Test pokemon hash are the same")
    void testPokemonHash() {
        assertEquals(ditto.hashCode(), pikachu.hashCode());
    }

    @Test
    @DisplayName("Test ditto is the same as pikachu")
    void testDittoIsSameAsPikachu() {
        assertEquals(ditto, pikachu);
    }

    @Test
    @DisplayName("Test pichu is not the same as pikachu")
    void testPichuIsNotSameAsPikachu() {
        assertNotEquals(pichu, pikachu);
    }

    @Test
    @DisplayName("pichu compareTo pikachu returns 1")
    void testPichuCompareToPikachu() {
        assertSame(1, pichu.compareTo(pikachu));
    }

    @Test
    @DisplayName("ditto compareTo pikachu returns 0")
    void testDittoCompareToPikachu() {
        assertSame(0, ditto.compareTo(pikachu));
    }
}
