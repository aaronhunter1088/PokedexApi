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
import skaro.pokeapi.resource.evolutionchain.EvolutionChain;
import skaro.pokeapi.resource.evolutiontrigger.EvolutionTrigger;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/evolution")
class EvolutionApi extends BaseApiController
{
    private static final Logger logger = LogManager.getLogger(EvolutionApi.class);

    EvolutionApi(PokeApiClient client)
    {
        super(client);
    }

    // Evolution Chains

    @GetMapping(value = "/evolution-chain")
    ResponseEntity<?> getEvolutionChains(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                         @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getEvolutionChains limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<EvolutionChain> evolutionChains = pokeApiClient.getResource(EvolutionChain.class, new PageQuery(limit, offset * limit)).block();
            if (evolutionChains != null) return ResponseEntity.ok(evolutionChains);
            return ResponseEntity.badRequest().body("Could not access EvolutionChain endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/evolution-chain/{id}")
    ResponseEntity<?> getEvolutionChain(@PathVariable("id") String id)
    {
        logger.info("getEvolutionChain {}", id);
        try {
            EvolutionChain evolutionChain = pokeApiClient.getResource(EvolutionChain.class, id).block();
            if (evolutionChain != null) return ResponseEntity.ok(evolutionChain);
            return ResponseEntity.badRequest().body("Could not find an EvolutionChain with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Evolution Triggers

    @GetMapping(value = "/evolution-trigger")
    ResponseEntity<?> getEvolutionTriggers(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                           @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getEvolutionTriggers");
        try {
            NamedApiResourceList<EvolutionTrigger> evolutionTriggers = pokeApiClient.getResource(EvolutionTrigger.class, new PageQuery(limit, offset * limit)).block();
            if (evolutionTriggers != null) return ResponseEntity.ok(evolutionTriggers);
            return ResponseEntity.badRequest().body("Could not access EvolutionTrigger endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/evolution-trigger/{id}")
    ResponseEntity<?> getEvolutionTrigger(@PathVariable("id") String id)
    {
        logger.info("getEvolutionTrigger {}", id);
        try {
            EvolutionTrigger evolutionTrigger = pokeApiClient.getResource(EvolutionTrigger.class, id).block();
            if (evolutionTrigger != null) return ResponseEntity.ok(evolutionTrigger);
            return ResponseEntity.badRequest().body("Could not find an EvolutionTrigger with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
