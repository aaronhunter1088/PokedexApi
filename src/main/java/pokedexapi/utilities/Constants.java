package pokedexapi.utilities;

public class Constants
{

    public static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2";

    // Endpoints
    public static final String POKEMON_SPECIES_ENDPOINT = POKEAPI_BASE_URL + "/pokemon-species/%s";
    public static String POKEMON_SPECIES_URL(String idOrName)
    {
        return String.format(POKEMON_SPECIES_ENDPOINT, idOrName);
    }

    public static final String DEFAULT_URL_TEMPLATE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/%s.png";
    public static final String OFFICIAL_URL_TEMPLATE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/%s.png";
    public static final String GIF_URL_TEMPLATE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/%s.gif";
    public static final String SHINY_URL_TEMPLATE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/shiny/%s.gif";

    public static String DEFAULT_IMAGE_URL(Object pokemonId)
    {
        return String.format(DEFAULT_URL_TEMPLATE, pokemonId.toString());
    }

    public static String OFFICIAL_IMAGE_URL(Object pokemonId)
    {
        return String.format(OFFICIAL_URL_TEMPLATE, pokemonId.toString());
    }

    public static String GIF_IMAGE_URL(Object pokemonId)
    {
        return String.format(GIF_URL_TEMPLATE, pokemonId.toString());
    }

    public static String SHINY_IMAGE_URL(Object pokemonId)
    {
        return String.format(SHINY_URL_TEMPLATE, pokemonId.toString());
    }
}
