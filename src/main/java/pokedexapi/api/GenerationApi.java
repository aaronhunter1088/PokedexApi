package pokedexapi.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseApiController;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.generation.Generation;

@RestController
@RequestMapping("/generation")
public class GenerationApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(GenerationApi.class);

    GenerationApi(PokemonService pokemonService, PokeApiClient client)
    {
        super(pokemonService, client);
    }

    @GetMapping(value = "")
    ResponseEntity<?> getGenerations(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getGenerations limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Generation> page = pokeApiClient.getResource(Generation.class, new PageQuery(limit, offset)).block();
            if (null != page) return ResponseEntity.ok(page.results());
            else return ResponseEntity.badRequest().body("Could not access Generation endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> getGeneration(@PathVariable("id") int id)
    {
        LOGGER.info("getGeneration {}", id);
        try {
            Generation generation = pokeApiClient.getResource(Generation.class, String.valueOf(id)).block();
            if (null != generation) return ResponseEntity.ok(generation);
            else return ResponseEntity.badRequest().body("Could not find Generation with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
