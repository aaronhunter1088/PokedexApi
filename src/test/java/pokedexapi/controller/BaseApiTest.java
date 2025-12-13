package pokedexapi.controller;

import tools.jackson.databind.ObjectMapper;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import pokedexapi.entity.Pokemon;
import pokedexapi.service.PokemonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.NamedApiResource;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.PokeApiResource;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * BaseApiTest is a base class for API tests in the Pokedex API project.
 * <p>
 * Annotations:
 * ActiveProfiles("test"): Activates the "test" profile for the application context.
 * ExtendWith(SpringExtension.class): Integrates the Spring TestContext Framework with JUnit 5.
 * SpringBootTest: Indicates that the class is a Spring Boot test and will bootstrap the entire container.
 */
@ExtendWith(MockitoExtension.class)
public class BaseApiTest {

    private static final Logger logger = LogManager.getLogger(BaseApiTest.class);

    @Autowired
    protected PokemonService pokemonService;
    @MockitoBean
    protected PokeApiClient pokeApiClient;
    @Autowired
    protected ObjectMapper objectMapper;

    @Value("${skaro.pokeapi.baseUri}")
    protected String pokeApiBaseUrl;

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
     * @param nameOrId String the name or id of a Pokemon
     * @return the Pokemon or null
     */
    protected skaro.pokeapi.resource.pokemon.Pokemon retrievePokemon(String nameOrId)
    {
        logger.info("retrievePokemon");
        try {
            // validate nameOrId here
            return pokeApiClient.getResource(skaro.pokeapi.resource.pokemon.Pokemon.class, nameOrId).block();
        } catch (Exception e) {
            logger.error("Could not find pokemon with value: {}", nameOrId);
            return null;
        }
    }

    protected void setGifImage(Pokemon pokemon)
    {
        pokemon.setGifImage("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/"+pokemon.getId()+".gif");
        HttpResponse<String> response;
        try {
            response = pokemonService.callUrl(pokemon.getGifImage());
            if (response.statusCode() == 404) pokemon.setGifImage(null);
        } catch (Exception e) {
            logger.error("Could not find gif image for pokemon with id: {}", pokemon.getId());
            pokemon.setGifImage(null);
        }
    }

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

    // Helper methods
    /**
     * Helper method to convert a NamedApiResourceList<Berry> to a Mono<?>
     * @param response NamedApiResourceList<Berry> the response to convert
     * @return Mono<?> the response as a Mono
     */
    protected <T extends PokeApiResource> Mono<NamedApiResourceList<T>> getMonoFromListResponse(NamedApiResourceList<?> response, NamedApiResource<T> namedApiResource) {
        NamedApiResourceList<T> just = (NamedApiResourceList<T>) Mono.just(response).block();
        return Mono.just((NamedApiResourceList<T>) response);
    }
}
