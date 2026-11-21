package pokedexapi.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseApiController;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResource;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;

import java.util.Arrays;

@RestController
@RequestMapping("/pokemon-species")
public class PokemonSpeciesApi extends BaseApiController
{
    private static final Logger logger = LogManager.getLogger(PokemonSpeciesApi.class);

    PokemonSpeciesApi(PokeApiClient client)
    {
        super(client);
    }

    @GetMapping(value = "")
    ResponseEntity<?> getAllSpeciesData(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                        @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getAllSpeciesData limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonSpecies> speciesData = pokeApiClient.getResource(PokemonSpecies.class, new PageQuery(limit, offset * limit)).block();
            if (speciesData != null) return ResponseEntity.ok(speciesData);
            return ResponseEntity.badRequest().body("Could not access PokemonSpecies endpoint");
        }
        catch (Exception e) {
            logger.error("Could not fetch all species: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all species because " + e.getMessage());
        }
    }

    @RequestMapping(value = "/{nameOrId}", method = RequestMethod.GET)
    public ResponseEntity<?> getPokemonSpeciesData(@PathVariable String nameOrId)
    {
        logger.info("getSpeciesData: {}", nameOrId);
        try {
            PokemonSpecies speciesData;
            try {
                speciesData = pokeApiClient.getResource(PokemonSpecies.class, nameOrId).block();
                if (speciesData != null) {
                    return ResponseEntity.ok(speciesData);
                }
            }
            catch (Exception e) {
                logger.error("Error fetching species data for {}: {}", nameOrId, e.getMessage());
            }
            Pokemon pokemon = retrievePokemon(nameOrId);
            if (pokemon == null) return ResponseEntity.badRequest().body("Could not find PokemonSpecies with: " + nameOrId);
            NamedApiResource<PokemonSpecies> speciesUrl = pokemon.species();
            if (speciesUrl != null) {
                return ResponseEntity.ok(pokeApiClient.getResource(PokemonSpecies.class, speciesUrl.name()).block());
            } else {
                return ResponseEntity.badRequest().body("Could not find PokemonSpecies with: " + nameOrId);
            }
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
