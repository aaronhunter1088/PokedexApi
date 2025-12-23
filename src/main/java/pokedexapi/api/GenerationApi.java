package pokedexapi.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseController;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.generation.Generation;
import tools.jackson.databind.ObjectMapper;

// TODO: Validate
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/generation")
public class GenerationApi extends BaseController
{
    /* Logging instance */
    private static final Logger LOGGER = LogManager.getLogger(GenerationApi.class);

    @Autowired
    GenerationApi(PokemonService pokemonService, PokeApiClient client, ObjectMapper objectMapper)
    {
        super(pokemonService, client, objectMapper);
    }

    @GetMapping(value="")
    @ResponseBody
    ResponseEntity<?> getGenerations(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                     @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        LOGGER.info("getGenerations");
        NamedApiResourceList<Generation> generations = pokeApiClient.getResource(Generation.class, new PageQuery(limit, offset)).block();
        if (null != generations) return ResponseEntity.ok(generations);
        else return ResponseEntity.badRequest().body("Could not access Generation endpoint");
    }

    @GetMapping(value="/{id}")
    @ResponseBody
    ResponseEntity<?> getGeneration(@PathVariable("id") int id)
    {
        LOGGER.info("getGeneration {}", id);
        Generation generation = pokeApiClient.getResource(Generation.class, String.valueOf(id)).block();
        if (null != generation) return ResponseEntity.ok(generation);
        else return ResponseEntity.badRequest().body("Could not access Generation endpoint");
    }

//    @RequestMapping(value="", method= RequestMethod.GET)
//    @ResponseBody
//    ResponseEntity<?> getGenerations(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
//                                 @RequestParam(value="offset", required=false, defaultValue="0") int offset)
//    {
//        LOGGER.info("getGenerations");
//        HttpResponse<String> generations;
//        try {
//            generations = pokemonService.callUrl(pokeApiBaseUrl+"generation?limit="+limit+"&offset="+offset);
//        } catch (Exception e) {
//            LOGGER.error("Error retrieving response because {}", e.getMessage());
//            return ResponseEntity.internalServerError().build();
//        }
//        return switch (generations.statusCode()) {
//            case 200 -> ResponseEntity.ok(generations.body());
//            case 400 -> ResponseEntity.badRequest().body("Could not access Gender endpoint");
//            default -> ResponseEntity.internalServerError().body("Could not access Gender endpoint");
//        };
//    }
//
//    @RequestMapping(value="/{id}", method=RequestMethod.GET)
//    @ResponseBody
//    ResponseEntity<?> getGeneration(@PathVariable(value="id") String id)
//    {
//        LOGGER.info("getGeneration {}", id);
//        HttpResponse<String> generation;
//        try {
//            generation = pokemonService.callUrl(pokeApiBaseUrl+"generation/"+id);
//        } catch (Exception e) {
//            LOGGER.error("Error retrieving response because {}", e.getMessage());
//            return ResponseEntity.internalServerError().build();
//        }
//        return switch (generation.statusCode()) {
//            case 200 -> ResponseEntity.ok(generation.body());
//            case 400 -> ResponseEntity.badRequest().body("Could not access Gender endpoint");
//            default -> ResponseEntity.internalServerError().body("Could not access Gender endpoint");
//        };
//    }
}
