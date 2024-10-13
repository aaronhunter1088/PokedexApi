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
        HttpResponse<String> response = pokemonService.callUrl(pokeApiBaseUrl+"/machine/");
        if (response.statusCode() == 200) {
            return ResponseEntity.ok(response.body());
        } else if (response.statusCode() == 400) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value="/machine/{id}")
    ResponseEntity<?> getMachine(@PathVariable(value="id") String id)
    {
        logger.info("getLocation {}", id);
        HttpResponse<String> response = (HttpResponse<String>) pokemonService.callUrl(pokeApiBaseUrl+"/machine/"+id);
        if (response.statusCode() == 200) {
            return ResponseEntity.ok(response.body());
        } else if (response.statusCode() == 400) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

}
