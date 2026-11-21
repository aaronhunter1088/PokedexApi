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

    EncounterApi(PokeApiClient client)
    {
        super(client);
    }

    // Encounter Methods

    @GetMapping(value = "/encounter-method")
    ResponseEntity<?> getEncounterMethods(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                          @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getEncounterMethods limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<EncounterMethod> encounterMethods = pokeApiClient.getResource(EncounterMethod.class, new PageQuery(limit, offset * limit)).block();
            if (null != encounterMethods) return ResponseEntity.ok(encounterMethods);
            return ResponseEntity.badRequest().body("Could not access EncounterMethods endpoint");
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
            return ResponseEntity.badRequest().body("Could not find an EncounterMethod with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Conditions

    @GetMapping(value = "/encounter-condition")
    ResponseEntity<?> getEncounterConditions(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                             @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getEncounterConditions limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<EncounterCondition> encounterConditions = pokeApiClient.getResource(EncounterCondition.class, new PageQuery(limit, offset * limit)).block();
            if (null != encounterConditions) return ResponseEntity.ok(encounterConditions);
            return ResponseEntity.badRequest().body("Could not access EncounterConditions endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/encounter-condition/{id}")
    ResponseEntity<?> getEncounterCondition(@PathVariable(value = "id") String id)
    {
        logger.info("getEncounterCondition {}", id);
        try {
            EncounterCondition encounterCondition = pokeApiClient.getResource(EncounterCondition.class, id).block();
            if (null != encounterCondition) return ResponseEntity.ok(encounterCondition);
            return ResponseEntity.badRequest().body("Could not find an EncounterCondition with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Condition Values

    @GetMapping(value = "/encounter-condition-value")
    ResponseEntity<?> getEncounterConditionValues(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                                  @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getEncounterConditionValues limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<EncounterConditionValue> encounterConditionValues = pokeApiClient.getResource(EncounterConditionValue.class, new PageQuery(limit, offset * limit)).block();
            if (null != encounterConditionValues) return ResponseEntity.ok(encounterConditionValues);
            return ResponseEntity.badRequest().body("Could not access EncounterConditionValues endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/encounter-condition-value/{id}")
    ResponseEntity<?> getEncounterConditionValue(@PathVariable(value = "id") String id)
    {
        logger.info("getEncounterConditionValue {}", id);
        try {
            EncounterConditionValue encounterConditionValue = pokeApiClient.getResource(EncounterConditionValue.class, id).block();
            if (null != encounterConditionValue) return ResponseEntity.ok(encounterConditionValue);
            return ResponseEntity.badRequest().body("Could not find an EncounterConditionValue with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
