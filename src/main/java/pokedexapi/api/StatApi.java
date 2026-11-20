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
import skaro.pokeapi.resource.stat.Stat;
import skaro.pokeapi.resource.stat.Stat;


@RestController
@RequestMapping("/stat")
public class StatApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(StatApi.class);

    StatApi(PokemonService pokemonService, PokeApiClient client)
    {
        super(pokemonService, client);
    }

    @GetMapping(value = "")
    ResponseEntity<?> getStats(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getStats limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Stat> page = pokeApiClient.getResource(Stat.class, new PageQuery(limit, offset)).block();
            if (null != page) return ResponseEntity.ok(page.results());
            else return ResponseEntity.badRequest().body("Could not access Stats endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{nameOrId}")
    ResponseEntity<?> getStat(@PathVariable("nameOrId") String nameOrId)
    {
        LOGGER.info("getStat {}", nameOrId);
        try {
            Stat stat = pokeApiClient.getResource(Stat.class, nameOrId).block();
            if (null != stat) return ResponseEntity.ok(stat);
            else return ResponseEntity.badRequest().body("Could not find Stat with " + nameOrId);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
