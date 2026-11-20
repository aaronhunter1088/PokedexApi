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
import skaro.pokeapi.resource.type.Type;


@RestController
@RequestMapping("/type")
public class TypeApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(TypeApi.class);

    TypeApi(PokemonService pokemonService, PokeApiClient client)
    {
        super(pokemonService, client);
    }

    @GetMapping(value = "")
    ResponseEntity<?> getTypes(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getTypes limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Type> page = pokeApiClient.getResource(Type.class, new PageQuery(limit, offset)).block();
            if (null != page) return ResponseEntity.ok(page.results());
            else return ResponseEntity.badRequest().body("Could not access Type endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{nameOrId}")
    ResponseEntity<?> getAPokemonsType(@PathVariable("nameOrId") String nameOrId)
    {
        LOGGER.info("getType {}", nameOrId);
        try {
            Type type = pokeApiClient.getResource(Type.class, nameOrId).block();
            if (null != type) return ResponseEntity.ok(type);
            else return ResponseEntity.badRequest().body("Could not find Type with " + nameOrId);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
