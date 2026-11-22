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
import skaro.pokeapi.resource.ability.Ability;

@RestController
@RequestMapping("/ability")
public class AbilityApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(AbilityApi.class);

    AbilityApi(PokeApiClient client)
    {
        super(client);
    }

    @Operation(summary = "Get all abilities", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "")
    public ResponseEntity<?> getAllAbilities(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                             @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllAbilities limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Ability> abilities = pokeApiClient.getResource(Ability.class, new PageQuery(limit, offset * limit)).block();
            if (abilities != null) return ResponseEntity.ok(abilities);
            return ResponseEntity.badRequest().body("Could not access Ability endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get ability", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAbility(@PathVariable String id)
    {
        LOGGER.info("getAbility {}", id);
        try {
            Ability ability = pokeApiClient.getResource(Ability.class, id).block();
            if (ability != null) return ResponseEntity.ok(ability);
            return ResponseEntity.badRequest().body("Could not find Ability with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
