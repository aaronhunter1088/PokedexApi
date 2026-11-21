package pokedexapi.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseApiController;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.generation.Generation;

@RestController
@RequestMapping("/generation")
public class GenerationApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(GenerationApi.class);

    GenerationApi(PokeApiClient client)
    {
        super(client);
    }

    @Operation(summary = "Get all generations", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "")
    ResponseEntity<?> getAllGenerations(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                        @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllGenerations limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Generation> generations = pokeApiClient.getResource(Generation.class, new PageQuery(limit, offset * limit)).block();
            if (generations != null) return ResponseEntity.ok(generations);
            return ResponseEntity.badRequest().body("Could not access Generation endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get generation", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{id}")
    ResponseEntity<?> getGeneration(@PathVariable("id") String id)
    {
        LOGGER.info("getGeneration {}", id);
        try {
            Generation generation = pokeApiClient.getResource(Generation.class, id).block();
            if (generation != null) return ResponseEntity.ok(generation);
            return ResponseEntity.badRequest().body("Could not find Generation with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
