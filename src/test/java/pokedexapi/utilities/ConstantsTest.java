package pokedexapi.utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConstantsTest
{
    @Test
    @DisplayName("POKEMON_SPECIES_URL formats using endpoint template")
    void testPokemonSpeciesUrl()
    {
        assertThat(Constants.POKEMON_SPECIES_URL("pikachu"))
                .isEqualTo("https://pokeapi.co/api/v2/pokemon-species/pikachu");
    }

    @Test
    @DisplayName("DEFAULT_IMAGE_URL formats pokemon id")
    void testDefaultImageUrl()
    {
        assertThat(Constants.DEFAULT_IMAGE_URL(25))
                .contains("/sprites/pokemon/25.png");
    }

    @Test
    @DisplayName("OFFICIAL_IMAGE_URL formats pokemon id")
    void testOfficialImageUrl()
    {
        assertThat(Constants.OFFICIAL_IMAGE_URL(25))
                .contains("/official-artwork/25.png");
    }

    @Test
    @DisplayName("GIF_IMAGE_URL formats pokemon id")
    void testGifImageUrl()
    {
        assertThat(Constants.GIF_IMAGE_URL(25))
                .contains("/animated/25.gif");
    }

    @Test
    @DisplayName("SHINY_IMAGE_URL formats pokemon id")
    void testShinyImageUrl()
    {
        assertThat(Constants.SHINY_IMAGE_URL(25))
                .contains("/animated/shiny/25.gif");
    }
}

