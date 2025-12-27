package pokedexapi.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseApiController;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResource;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;

import java.net.http.HttpResponse;
import java.util.Arrays;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/pokemon-species")
public class PokemonSpeciesApi extends BaseApiController
{
    /* Logging instance */
    private static final Logger logger = LogManager.getLogger(PokemonSpeciesApi.class);

    PokemonSpeciesApi(PokemonService pokemonService, PokeApiClient client)
    {
        super(pokemonService, client);
    }

    @GetMapping("")
    @ResponseBody
    ResponseEntity<?> getAllSpeciesData(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                        @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getAllSpeciesData limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonSpecies> allSpeciesData = pokeApiClient.getResource(PokemonSpecies.class, new PageQuery(limit, offset)).block();
            if (null != allSpeciesData) return ResponseEntity.ok(allSpeciesData);
            else return ResponseEntity.badRequest().body("Could not access PokemonSpecies endpoint");
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all species because " + e.getMessage());
        }
    }

    @RequestMapping(value = "/{nameOrId}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokemonSpeciesData(@PathVariable String nameOrId)
    {
        logger.info("getSpeciesData: {}", nameOrId);
        try {
            PokemonSpecies speciesData;
            try {
                speciesData = pokemonService.getPokemonSpeciesData(nameOrId);
            }
            catch (Exception e) {
                speciesData = null;
            }
            if (speciesData != null) return ResponseEntity.ok(speciesData);
            else {
                Pokemon pokemonResource = retrievePokemon(nameOrId);
                assert pokemonResource != null;
                NamedApiResource<PokemonSpecies> speciesResource = pokemonResource.species();
                if (null == speciesResource) {
                    return ResponseEntity.noContent().build();
                }
                HttpResponse<String> response = pokemonService.callUrl(speciesResource.url());
                // TODO: Convert response.body() to PokemonSpecies
                if (response.statusCode() == 200) return ResponseEntity.ok(response.body());
                else return ResponseEntity.badRequest().body("Could not find PokemonSpecies with: " + nameOrId);
            }
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
