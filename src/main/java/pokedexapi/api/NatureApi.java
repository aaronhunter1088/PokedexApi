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
import skaro.pokeapi.resource.nature.Nature;


@RestController
@RequestMapping("/nature")
public class NatureApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(NatureApi.class);

    NatureApi(PokeApiClient client)
    {
        super(client);
    }

    @Operation(summary = "Get all natures", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "")
    ResponseEntity<?> getAllNatures(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                    @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllNatures limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Nature> natures = pokeApiClient.getResource(Nature.class, new PageQuery(limit, offset * limit)).block();
            if (natures != null) return ResponseEntity.ok(natures);
            return ResponseEntity.badRequest().body("Could not access Nature endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get nature", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{id}")
    ResponseEntity<?> getNature(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getNature {}", id);
        try {
            Nature nature = pokeApiClient.getResource(Nature.class, id).block();
            if (nature != null) return ResponseEntity.ok(nature);
            return ResponseEntity.badRequest().body("Could not find Nature with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
