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
import skaro.pokeapi.resource.encountercondition.EncounterCondition;
import skaro.pokeapi.resource.encounterconditionvalue.EncounterConditionValue;
import skaro.pokeapi.resource.encountermethod.EncounterMethod;

@RestController
@RequestMapping("/encounter")
class EncounterApi extends BaseApiController
{
    private static final Logger logger = LogManager.getLogger(EncounterApi.class);

    EncounterApi(PokemonService pokemonService, PokeApiClient client)
    {
        super(pokemonService, client);
    }

    // Encounter Methods

    @GetMapping(value = "/encounter-method")
    ResponseEntity<?> getEncounterMethods(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getEncounterMethods");
        try {
            NamedApiResourceList<EncounterMethod> encounters = pokeApiClient.getResource(skaro.pokeapi.resource.encountermethod.EncounterMethod.class, new PageQuery(limit, offset)).block();
            if (null != encounters) return ResponseEntity.ok(encounters.results());
            else return ResponseEntity.badRequest().body("Could not access EncounterMethod endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/encounter-method/{id}")
    ResponseEntity<?> getEncounterMethod(@PathVariable(value = "id") String id)
    {
        logger.info("getEncounterMethod {}", id);
        try {
            EncounterMethod encounterMethod = pokeApiClient.getResource(EncounterMethod.class, id).block();
            if (null != encounterMethod) return ResponseEntity.ok(encounterMethod);
            else return ResponseEntity.badRequest().body("Could not find an encounterMethod with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Conditions

    @GetMapping(value = "/encounter-condition")
    ResponseEntity<?> getConditions(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getConditions");
        try {
            NamedApiResourceList<EncounterCondition> conditions = pokeApiClient.getResource(skaro.pokeapi.resource.encountercondition.EncounterCondition.class, new PageQuery(limit, offset)).block();
            if (null != conditions) return ResponseEntity.ok(conditions.results());
            else return ResponseEntity.badRequest().body("Could not access EncounterCondition endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/encounter-condition/{id}")
    ResponseEntity<?> getCondition(@PathVariable(value = "id") String id)
    {
        logger.info("getCondition {}", id);
        try {
            EncounterCondition encounterCondition = pokeApiClient.getResource(EncounterCondition.class, id).block();
            if (null != encounterCondition) return ResponseEntity.ok(encounterCondition);
            else return ResponseEntity.badRequest().body("Could not find an encounterCondition with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Condition Values

    @GetMapping(value = "/encounter-condition-value")
    ResponseEntity<?> getConditionValues(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getConditionValues");
        try {
            NamedApiResourceList<EncounterConditionValue> conditionValues = pokeApiClient.getResource(skaro.pokeapi.resource.encounterconditionvalue.EncounterConditionValue.class, new PageQuery(limit, offset)).block();
            if (null != conditionValues) return ResponseEntity.ok(conditionValues.results());
            else return ResponseEntity.badRequest().body("Could not access EncounterConditionValue endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/encounter-condition-value/{id}")
    ResponseEntity<?> getConditionValue(@PathVariable(value = "id") String id)
    {
        logger.info("getConditionValue {}", id);
        try {
            EncounterConditionValue conditionValue = pokeApiClient.getResource(EncounterConditionValue.class, id).block();
            if (null != conditionValue) return ResponseEntity.ok(conditionValue);
            else return ResponseEntity.badRequest().body("Could not find an conditionValue with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
