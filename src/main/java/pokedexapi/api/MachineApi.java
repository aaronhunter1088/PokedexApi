package pokedexapi.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseController;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import tools.jackson.databind.json.JsonMapper;

import java.net.http.HttpResponse;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/machine")
class MachineApi extends BaseController {

    private static final Logger logger = LogManager.getLogger(MachineApi.class);

    @Autowired
    MachineApi(PokemonService pokemonService, PokeApiClient client, @Qualifier("jsonMapper") JsonMapper jsonMapper) {
        super(pokemonService, client, jsonMapper);
    }

    // Machines
    @GetMapping(value = "")
    @ResponseBody
    ResponseEntity<?> getMachines() {
        logger.info("getMachines");
        HttpResponse<String> response;
        try {
            response = pokemonService.callUrl(pokeApiBaseUrl + "/machine");
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

    @GetMapping(value = "/{id}")
    ResponseEntity<?> getMachine(@PathVariable(value = "id") String id) {
        logger.info("getLocation {}", id);
        HttpResponse<String> response;
        try {
            response = pokemonService.callUrl(pokeApiBaseUrl + "/machine/" + id);
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
