package pokedexapi.utility;

public class Constants {

    public static final String BASE_URL = "https://pokeapi.co/api/v2/";

    public static final String GIF_IMAGE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/%s.gif";
    public static String GIF_IMAGE(Object pokemonId) {
        return String.format(GIF_IMAGE_URL, pokemonId.toString());
    }
}
