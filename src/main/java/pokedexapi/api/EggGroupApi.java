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
import skaro.pokeapi.resource.egggroup.EggGroup;

@RestController
@RequestMapping("/egg-group")
public class EggGroupApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(EggGroupApi.class);

    EggGroupApi(PokeApiClient client)
    {
        super(client);
    }

    @GetMapping(value = "")
    ResponseEntity<?> getEggGroups(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                   @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getEggGroups limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<EggGroup> eggGroups = pokeApiClient.getResource(EggGroup.class, new PageQuery(limit, offset * limit)).block();
            if (null != eggGroups) return ResponseEntity.ok(eggGroups);
            return ResponseEntity.badRequest().body("Could not access EggGroup endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> getEggGroup(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getEggGroup {}", id);
        try {
            EggGroup eggGroup = pokeApiClient.getResource(EggGroup.class, id).block();
            if (null != eggGroup) return ResponseEntity.ok(eggGroup);
            return ResponseEntity.badRequest().body("Could not find EggGroup with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
