package pokedexapi.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controller.BaseController;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import tools.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/type")
public class TypeApi extends BaseController
{
    /* Logging instance */
    private static final Logger LOGGER = LogManager.getLogger(TypeApi.class);

    @Autowired
    TypeApi(PokemonService pokemonService, PokeApiClient client, ObjectMapper objectMapper)
    {
        super(pokemonService, client, objectMapper);
    }

//    @RequestMapping(value = "", method= RequestMethod.GET)
//    @ResponseBody
//    ResponseEntity<?> getTypes()
//    {
//        LOGGER.info("getAllTypes");
//        List<String> types;
//        try {
//            types = pokemonService.getAllTypes();
//            LOGGER.debug("# of types: {}", types.size());
//        } catch (Exception e) {
//            LOGGER.error("Error retrieving response because {}", e.getMessage());
//            return ResponseEntity.internalServerError().build();
//        }
//        if (types != null) return ResponseEntity.ok(types);
//        else return ResponseEntity.badRequest().body("Could not access Types endpoint");
//    }

    @RequestMapping(value = "", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getTypes(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                              @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        LOGGER.info("getType limit:{} offset:{}", limit, offset);
        HttpResponse<String> types;
        try {
            types = pokemonService.callUrl(pokeApiBaseUrl+"type?limit="+limit+"&offset="+offset);
        } catch (Exception e) {
            LOGGER.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return switch (types.statusCode()) {
            case 200 -> ResponseEntity.ok(types.body());
            case 400 -> ResponseEntity.badRequest().body("Could not access Types endpoint");
            default -> ResponseEntity.internalServerError().body("Could not access Types endpoint");
        };
    }

    @RequestMapping(value = "/{nameOrId}", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAPokemonsType(@PathVariable("nameOrId") String nameOrId)
    {
        LOGGER.info("getType {}", nameOrId);
        HttpResponse<String> type;
        try {
            type = pokemonService.callUrl(pokeApiBaseUrl+"type/"+nameOrId);
        } catch (Exception e) {
            LOGGER.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return switch (type.statusCode()) {
            case 200 -> ResponseEntity.ok(type.body());
            case 400 -> ResponseEntity.badRequest().body("Could not access Type endpoint");
            default -> ResponseEntity.internalServerError().body("Could not access Type endpoint");
        };
    }
}
