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
import skaro.pokeapi.resource.contesteffect.ContestEffect;
import skaro.pokeapi.resource.contesttype.ContestType;
import skaro.pokeapi.resource.supercontesteffect.SuperContestEffect;

@RestController
@RequestMapping("/contest")
class ContestApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(ContestApi.class);

    ContestApi(PokeApiClient client)
    {
        super(client);
    }

    // ContestType

    @Operation(summary = "Get all contest types", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/contest-type")
    ResponseEntity<?> getAllContestTypes(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                         @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllContestTypes limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<ContestType> contestTypes = pokeApiClient.getResource(ContestType.class, new PageQuery(limit, offset * limit)).block();
            if (contestTypes != null) return ResponseEntity.ok(contestTypes);
            return ResponseEntity.badRequest().body("Could not access ContestType endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get contest type", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/contest-type/{id}")
    ResponseEntity<?> getContestType(@PathVariable String id)
    {
        LOGGER.info("getContestType {}", id);
        try {
            ContestType contestType = pokeApiClient.getResource(ContestType.class, id).block();
            if (contestType != null) return ResponseEntity.ok(contestType);
            return ResponseEntity.badRequest().body("Could not find a ContestType with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // ContestEffect

    @Operation(summary = "Get all contest effect", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/contest-effect")
    ResponseEntity<?> getAllContestEffect(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                          @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllContestEffects limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<ContestEffect> contestEffects = pokeApiClient.getResource(ContestEffect.class, new PageQuery(limit, offset * limit)).block();
            if (contestEffects != null) return ResponseEntity.ok(contestEffects);
            return ResponseEntity.badRequest().body("Could not access ContestEffect endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get contest effect", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/contest-effect/{id}")
    ResponseEntity<?> getContestEffect(@PathVariable String id)
    {
        LOGGER.info("getContestEffect with {}", id);
        try {
            ContestEffect contestEffect = pokeApiClient.getResource(ContestEffect.class, id).block();
            if (contestEffect != null) return ResponseEntity.ok(contestEffect);
            return ResponseEntity.badRequest().body("Could not find ContestEffect with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // SuperContestEffects

    @Operation(summary = "Get all super contest effect", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/super-contest-effect")
    ResponseEntity<?> getAllSuperContestEffect(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                               @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllSuperContestEffects limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<SuperContestEffect> superContestEffects = pokeApiClient.getResource(SuperContestEffect.class, new PageQuery(limit, offset * limit)).block();
            if (superContestEffects != null) return ResponseEntity.ok(superContestEffects);
            return ResponseEntity.badRequest().body("Could not access SuperContestEffect endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get super contest effect", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/super-contest-effect/{id}")
    ResponseEntity<?> getSuperContestEffect(@PathVariable String id)
    {
        LOGGER.info("getSuperContestEffect with {}", id);
        try {
            SuperContestEffect superContestEffect = pokeApiClient.getResource(SuperContestEffect.class, id).block();
            if (superContestEffect != null) return ResponseEntity.ok(superContestEffect);
            return ResponseEntity.badRequest().body("Could not find SuperContestEffect with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
