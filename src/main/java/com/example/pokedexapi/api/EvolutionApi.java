package com.example.pokedexapi.api;

import com.example.pokedexapi.controller.BaseController;
import com.example.pokedexapi.service.PokemonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skaro.pokeapi.client.PokeApiClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/evolution")
class EvolutionApi extends BaseController {

    private static final Logger logger = LogManager.getLogger(EvolutionApi.class);

    @Autowired
    EvolutionApi(PokemonService pokemonService, PokeApiClient client) {
        super(pokemonService, client);
    }

    // Evolution Chains
    @GetMapping(value="/evolution-chain")
    @ResponseBody
    ResponseEntity<?> getEvolutionChains(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                         @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getEvolutionChains");
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(pokeApiBaseUrl+"/evolution-chain?offset="+offset+"&limit="+limit))
                    .GET()
                    .build();
            response = HttpClient.newBuilder()
                    .build()
                    .send(request,  HttpResponse.BodyHandlers.ofString());
            logger.info("response: {}", response.body());
            return ResponseEntity.ok(response.body());
        } catch (Exception e) {
            logger.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value="/evolution-chain/{id}")
    @ResponseBody
    ResponseEntity<?> getEvolutionChain(@PathVariable("id") int id)
    {
        logger.info("getEvolutionChain {}", id);
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(pokeApiBaseUrl+"/evolution-chain/"+id))
                    .GET()
                    .build();
            response = HttpClient.newBuilder()
                    .build()
                    .send(request,  HttpResponse.BodyHandlers.ofString());
            logger.info("response: {}", response.body());
            return ResponseEntity.ok(response.body());
        } catch (Exception e) {
            logger.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Evolution Triggers
    @GetMapping(value="/evolution-trigger")
    @ResponseBody
    ResponseEntity<?> getEvolutionTriggers(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                           @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getEvolutionTriggers");
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(pokeApiBaseUrl+"/evolution-trigger?offset="+offset+"&limit="+limit))
                    .GET()
                    .build();
            response = HttpClient.newBuilder()
                    .build()
                    .send(request,  HttpResponse.BodyHandlers.ofString());
            logger.info("response: {}", response.body());
            return ResponseEntity.ok(response.body());
        } catch (Exception e) {
            logger.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value="/evolution-trigger/{id}")
    @ResponseBody
    ResponseEntity<?> getEvolutionTrigger(@PathVariable("id") int id)
    {
        logger.info("getEvolutionTrigger {}", id);
        HttpResponse<String> response = pokemonService.callUrl(pokeApiBaseUrl+"/evolution-trigger/"+id);
        if (response.statusCode() == 200) {
            return ResponseEntity.ok(response.body());
        } else if (response.statusCode() == 400) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.internalServerError().build();
        }
    }

}
