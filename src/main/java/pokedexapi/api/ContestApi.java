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
import skaro.pokeapi.resource.contesteffect.ContestEffect;
import skaro.pokeapi.resource.contesttype.ContestType;
import skaro.pokeapi.resource.supercontesteffect.SuperContestEffect;

@RestController
@RequestMapping("/contest")
class ContestApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(ContestApi.class);

    ContestApi(PokeApiClient client)
    {
        super(client);
    }

    // ContestType

    @GetMapping(value = "/contest-type")
    ResponseEntity<?> getAllContestTypes(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                         @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllContestTypes limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<ContestType> contestTypes = pokeApiClient.getResource(ContestType.class, new PageQuery(limit, offset * limit)).block();
            if (contestTypes != null) return ResponseEntity.ok(contestTypes);
            return ResponseEntity.badRequest().body("Could not access ContestType endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/contest-type/{id}")
    ResponseEntity<?> getContestType(@PathVariable String id)
    {
        LOGGER.info("getContestType {}", id);
        try {
            ContestType contestType = pokeApiClient.getResource(ContestType.class, id).block();
            if (contestType != null) return ResponseEntity.ok(contestType);
            return ResponseEntity.badRequest().body("Could not find a ContestType with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // ContestEffect

    @GetMapping(value = "/contest-effect")
    ResponseEntity<?> getAllContestEffect(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                          @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllContestEffects limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<ContestEffect> contestEffects = pokeApiClient.getResource(ContestEffect.class, new PageQuery(limit, offset * limit)).block();
            if (contestEffects != null) return ResponseEntity.ok(contestEffects);
            return ResponseEntity.badRequest().body("Could not access ContestEffect endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/contest-effect/{id}")
    ResponseEntity<?> getContestEffect(@PathVariable String id)
    {
        LOGGER.info("getContestEffect with {}", id);
        try {
            ContestEffect contestEffect = pokeApiClient.getResource(ContestEffect.class, id).block();
            if (contestEffect != null) return ResponseEntity.ok(contestEffect);
            return ResponseEntity.badRequest().body("Could not find ContestEffect with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // SuperContestEffects

    @GetMapping(value = "/super-contest-effect")
    ResponseEntity<?> getAllSuperContestEffect(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                               @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllSuperContestEffects limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<SuperContestEffect> superContestEffects = pokeApiClient.getResource(SuperContestEffect.class, new PageQuery(limit, offset * limit)).block();
            if (superContestEffects != null) return ResponseEntity.ok(superContestEffects);
            return ResponseEntity.badRequest().body("Could not access SuperContestEffect endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/super-contest-effect/{id}")
    ResponseEntity<?> getSuperContestEffect(@PathVariable String id)
    {
        LOGGER.info("getSuperContestEffect with {}", id);
        try {
            SuperContestEffect superContestEffect = pokeApiClient.getResource(SuperContestEffect.class, id).block();
            if (superContestEffect != null) return ResponseEntity.ok(superContestEffect);
            return ResponseEntity.badRequest().body("Could not find SuperContestEffect with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
