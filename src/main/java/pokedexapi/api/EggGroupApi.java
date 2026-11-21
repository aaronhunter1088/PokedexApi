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
import skaro.pokeapi.resource.egggroup.EggGroup;

@RestController
@RequestMapping("/egg-group")
public class EggGroupApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(EggGroupApi.class);

    EggGroupApi(PokeApiClient client)
    {
        super(client);
    }

    @Operation(summary = "Get egg groups", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "")
    ResponseEntity<?> getEggGroups(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                   @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getEggGroups limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<EggGroup> eggGroups = pokeApiClient.getResource(EggGroup.class, new PageQuery(limit, offset * limit)).block();
            if (eggGroups != null) return ResponseEntity.ok(eggGroups);
            return ResponseEntity.badRequest().body("Could not access EggGroup endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get egg group", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{id}")
    ResponseEntity<?> getEggGroup(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getEggGroup {}", id);
        try {
            EggGroup eggGroup = pokeApiClient.getResource(EggGroup.class, id).block();
            if (eggGroup != null) return ResponseEntity.ok(eggGroup);
            return ResponseEntity.badRequest().body("Could not find EggGroup with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
