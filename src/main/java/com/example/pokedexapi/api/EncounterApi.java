package com.example.pokedexapi.api;

import com.example.pokedexapi.controller.BaseController;
import com.example.pokedexapi.service.PokemonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.encountercondition.EncounterCondition;
import skaro.pokeapi.resource.encounterconditionvalue.EncounterConditionValue;
import skaro.pokeapi.resource.encountermethod.EncounterMethod;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/encounter")
class EncounterApi extends BaseController {

    private static final Logger logger = LogManager.getLogger(EncounterApi.class);

    // Encounter Method
    @Autowired
    EncounterApi(PokemonService pokemonService, PokeApiClient client, ObjectMapper objectMapper) {
        super(pokemonService, client, objectMapper);
    }

    @GetMapping(value="/encounter-method")
    @ResponseBody
    ResponseEntity<?> getEncounterMethods()
    {
        logger.info("getEncounterMethods");
        try {
            NamedApiResourceList<EncounterMethod> encounters = pokeApiClient.getResource(skaro.pokeapi.resource.encountermethod.EncounterMethod.class).block();
            if (null != encounters) return ResponseEntity.ok(encounters);
            else return ResponseEntity.badRequest().body("Could not access EncounterMethod endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/encounter-method/{id}")
    ResponseEntity<?> getEncounterMethod(@PathVariable(value="id") String id)
    {
        logger.info("getEncounterMethod {}", id);
        try {
            EncounterMethod encounterMethod = pokeApiClient.getResource(EncounterMethod.class, id).block();
            if (null != encounterMethod) return ResponseEntity.ok(encounterMethod);
            else return ResponseEntity.badRequest().body("Could not find an encounterMethod with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Conditions
    @GetMapping(value="/encounter-condition")
    @ResponseBody
    ResponseEntity<?> getConditions()
    {
        logger.info("getConditions");
        try {
            NamedApiResourceList<EncounterCondition> conditions = pokeApiClient.getResource(skaro.pokeapi.resource.encountercondition.EncounterCondition.class).block();
            if (null != conditions) return ResponseEntity.ok(conditions);
            else return ResponseEntity.badRequest().body("Could not access EncounterCondition endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/encounter-condition/{id}")
    ResponseEntity<?> getCondition(@PathVariable(value="id") String id)
    {
        logger.info("getCondition {}", id);
        try {
            EncounterCondition encounterCondition = pokeApiClient.getResource(EncounterCondition.class, id).block();
            if (null != encounterCondition) return ResponseEntity.ok(encounterCondition);
            else return ResponseEntity.badRequest().body("Could not find an encounterCondition with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Condition Values
    @GetMapping(value="/encounter-condition-value")
    @ResponseBody
    ResponseEntity<?> getConditionValues()
    {
        logger.info("getConditionValues");
        try {
            NamedApiResourceList<EncounterConditionValue> conditionValues = pokeApiClient.getResource(skaro.pokeapi.resource.encounterconditionvalue.EncounterConditionValue.class).block();
            if (null != conditionValues) return ResponseEntity.ok(conditionValues);
            else return ResponseEntity.badRequest().body("Could not access EncounterConditionValue endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/encounter-condition-value/{id}")
    ResponseEntity<?> getConditionValue(@PathVariable(value="id") String id)
    {
        logger.info("getConditionValue {}", id);
        try {
            EncounterConditionValue conditionValue = pokeApiClient.getResource(EncounterConditionValue.class, id).block();
            if (null != conditionValue) return ResponseEntity.ok(conditionValue);
            else return ResponseEntity.badRequest().body("Could not find an conditionValue with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
