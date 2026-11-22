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
import skaro.pokeapi.resource.stat.Stat;


@RestController
@RequestMapping("/stat")
public class StatApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(StatApi.class);

    StatApi(PokeApiClient client)
    {
        super(client);
    }

    @Operation(summary = "Get all stats", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "")
    ResponseEntity<?> getAllStats(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                  @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllStats limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Stat> stats = pokeApiClient.getResource(Stat.class, new PageQuery(limit, offset * limit)).block();
            if (stats != null) return ResponseEntity.ok(stats);
            return ResponseEntity.badRequest().body("Could not access Stats endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get stat", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{nameOrId}")
    ResponseEntity<?> getStat(@PathVariable("nameOrId") String nameOrId)
    {
        LOGGER.info("getStat {}", nameOrId);
        try {
            Stat stat = pokeApiClient.getResource(Stat.class, nameOrId).block();
            if (stat != null) return ResponseEntity.ok(stat);;
            return ResponseEntity.badRequest().body("Could not find Stat with " + nameOrId);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
