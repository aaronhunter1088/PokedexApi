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
public class PokemonTest extends BaseApiTest
{

    //Pokemon skaroPichu;
    //Pokemon skaroPikachu;
    Pokemon ditto;
    Pokemon pichu;
    Pokemon pikachu;

    @BeforeEach
    void setUp() throws IOException
    {
        pichu = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/pichu.json").getFile(),
                Pokemon.class
        );
        pikachu = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/pikachu.json").getFile(),
                Pokemon.class
        );

        ditto = Pokemon.from(pikachu, Collections.emptyMap()); // transforms into Pikachu

        assertNotNull(pichu);
        assertEquals("pichu", pichu.getName());
        assertEquals(172, pichu.getId());
        assertNotNull(pikachu);
        assertEquals("pikachu", pikachu.getName());
        assertEquals(25, pikachu.getId());
        assertNotNull(ditto);
        assertEquals("pikachu", ditto.getName());
        assertEquals(25, ditto.getId());
    }

    @Test
    @DisplayName("Test pokemon toString are the same")
    void testPokemonToString()
    {
        assertEquals(ditto.toString(), pikachu.toString());
    }

    @Test
    @DisplayName("Test pokemon hash are the same")
    void testPokemonHash()
    {
        assertEquals(ditto.hashCode(), pikachu.hashCode());
    }

    @Test
    @DisplayName("Test ditto is the same as pikachu")
    void testDittoIsSameAsPikachu()
    {
        assertEquals(ditto, pikachu);
    }

    @Test
    @DisplayName("Test pichu is not the same as pikachu")
    void testPichuIsNotSameAsPikachu()
    {
        assertNotEquals(pichu, pikachu);
    }

    @Test
    @DisplayName("pichu compareTo pikachu returns 1")
    void testPichuCompareToPikachu()
    {
        assertSame(1, pichu.compareTo(pikachu));
    }

    @Test
    @DisplayName("ditto compareTo pikachu returns 0")
    void testDittoCompareToPikachu()
    {
        assertSame(0, ditto.compareTo(pikachu));
    }
}
