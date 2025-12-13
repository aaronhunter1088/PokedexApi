package pokedexapi.controller;

import pokedexapi.entity.Pokemon;
import pokedexapi.service.PokemonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import skaro.pokeapi.client.PokeApiClient;
import tools.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@CrossOrigin(origins = "*")
@Controller
public class BaseController {

    private static final Logger logger = LogManager.getLogger(BaseController.class);

    protected final PokemonService pokemonService;
    protected final PokeApiClient pokeApiClient;
    protected final ObjectMapper objectMapper;

    @Value("${skaro.pokeapi.baseUri}")
    protected String pokeApiBaseUrl;

    protected BaseController(PokemonService pokemonService, PokeApiClient client, ObjectMapper objectMapper) {
        this.pokemonService = pokemonService;
        this.pokeApiClient = client;
        this.objectMapper = objectMapper;
    }

    @Deprecated(forRemoval = true)
    protected Integer getEvolutionChainID(Map<Integer, List<List<Integer>>> pokemonIDToEvolutionChainMap, String pokemonId)
    {
        logger.info("id: {}", pokemonId);
        List<Integer> keys = pokemonIDToEvolutionChainMap.keySet().stream().toList();
        Integer keyToReturn = 0;
        keysLoop:
        for(Integer key: keys) {
            List<List<Integer>> pokemonIds = pokemonIDToEvolutionChainMap.get(key);
            for (List<Integer> chainIds : pokemonIds) {
                if (chainIds.contains(Integer.valueOf(pokemonId))) {
                    keyToReturn = key;
                    break keysLoop;
                }
            }
        }
        logger.info("chainKey: {}", keyToReturn);
        return keyToReturn;
    }

    /**
     * Fetch the pokemon resource
     * <a href="https://pokeapi.co/api/v2/pokemon/{nameOrId}">Pokemon</a>
     * @param nameOrId String the name or id of a Pokemon
     * @return the Pokemon or null
     */
    protected skaro.pokeapi.resource.pokemon.Pokemon retrievePokemon(String nameOrId)
    {
        logger.info("retrievePokemon: {}", nameOrId);
        return pokemonService.getPokemonByIdOrName(nameOrId);
    }

    @Deprecated(forRemoval = true)
    protected void setGifImage(Pokemon pokemon)
    {
        pokemon.setGifImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/"+pokemon.getId()+".gif");
        HttpResponse<String> response = null;
        try {
            response = pokemonService.callUrl(pokemon.getGifImage());
            if (response.statusCode() == 404) pokemon.setGifImage(null);
        } catch (Exception e) {
            logger.error("Failed to fetch the gif image at: {}", pokemon.getGifImage());
        }
    }

    @Deprecated(forRemoval = true)
    protected Map<String, Object> generateDefaultAttributesMap()
    {
        return new TreeMap<>() {{
            put("name", null); // on screen
            put("gender", null);
            put("id", null);
            put("isBaby", null);
            put("heldItem", null); // on screen
            put("useItem", null); // on screen
            put("knownMove", null); // on screen
            put("knownMoveType", null); // on screen
            put("location", null); // on screen
            put("minAffection", null); // on screen
            put("minBeauty", null); // on screen
            put("minHappiness", null); // on screen
            put("minLevel", null); // on screen
            put("needsRain", null); // on screen
            put("timeOfDay", null); // on screen
            put("partySpecies", null);
            put("partyType", null); // not implemented
            put("trigger", null); // not implemented
            put("relativePhysicalStats", null);
            put("tradeSpecies", null);
            put("turnUpsideDown", null); // on screen
        }};
    }
}