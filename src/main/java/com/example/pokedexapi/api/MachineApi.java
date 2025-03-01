package com.example.pokedexapi.api;

import com.example.pokedexapi.controller.BaseController;
import com.example.pokedexapi.service.PokemonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skaro.pokeapi.client.PokeApiClient;

import java.net.http.HttpResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/machine")
class MachineApi extends BaseController {

    private static final Logger logger = LogManager.getLogger(MachineApi.class);

    @Autowired
    MachineApi(PokemonService pokemonService, PokeApiClient client) {
        super(pokemonService, client);
    }

    // Machines
    @GetMapping(value="")
    @ResponseBody
    ResponseEntity<?> getMachines()
    {
        logger.info("getMachines");
        HttpResponse<String> response;
        try {
            response = pokemonService.callUrl(pokeApiBaseUrl+"/machine");
        } catch (Exception e) {
            logger.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return switch (response.statusCode()) {
            case 200 -> ResponseEntity.ok(response.body());
            case 400 -> ResponseEntity.badRequest().build();
            default -> ResponseEntity.internalServerError().build();
        };
    }

    @GetMapping(value="/machine/{id}")
    ResponseEntity<?> getMachine(@PathVariable(value="id") String id)
    {
        logger.info("getLocation {}", id);
        HttpResponse<String> response;
        try {
            response = pokemonService.callUrl(pokeApiBaseUrl+"/machine/"+id);
        } catch (Exception e) {
            logger.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return switch (response.statusCode()) {
            case 200 -> ResponseEntity.ok(response.body());
            case 400 -> ResponseEntity.badRequest().build();
            default -> ResponseEntity.internalServerError().build();
        };
    }

}
