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
import skaro.pokeapi.resource.growthrate.GrowthRate;


@RestController
@RequestMapping("/growth-rate")
public class GrowthRateApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(GrowthRateApi.class);

    GrowthRateApi(PokemonService pokemonService, PokeApiClient client)
    {
        super(pokemonService, client);
    }

    @GetMapping(value = "")
    ResponseEntity<?> getGrowthRates(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getGrowthRates limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<GrowthRate> page = pokeApiClient.getResource(GrowthRate.class, new PageQuery(limit, offset)).block();
            if (null != page) return ResponseEntity.ok(page.results());
            else return ResponseEntity.badRequest().body("Could not access GrowthRate endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> getGrowthRate(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getGrowthRate {}", id);
        try {
            GrowthRate growthRate = pokeApiClient.getResource(GrowthRate.class, id).block();
            if (null != growthRate) return ResponseEntity.ok(growthRate);
            else return ResponseEntity.badRequest().body("Could not find GrowthRate with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
