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
import skaro.pokeapi.resource.berry.Berry;
import skaro.pokeapi.resource.berryfirmness.BerryFirmness;
import skaro.pokeapi.resource.berryflavor.BerryFlavor;

@RestController
@RequestMapping("/berry")
class BerryApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(BerryApi.class);

    BerryApi(PokeApiClient client)
    {
        super(client);
    }

    @Operation(summary = "Get all berries", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "")
    ResponseEntity<?> getAllBerries(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                    @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllBerries limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Berry> berries = pokeApiClient.getResource(Berry.class, new PageQuery(limit, offset * limit)).block();
            if (berries != null) return ResponseEntity.ok(berries);
            return ResponseEntity.badRequest().body("Could not access Berry endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get berry", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{id}")
    ResponseEntity<?> getBerry(@PathVariable String id)
    {
        LOGGER.info("getBerry {}", id);
        try {
            Berry berry = pokeApiClient.getResource(Berry.class, id).block();
            if (berry != null) return ResponseEntity.ok(berry);
            return ResponseEntity.badRequest().body("Could not find a berry with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get berry firmness", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/berry-firmness")
    ResponseEntity<?> getBerryFirmness(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                       @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getBerryFirmness limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<BerryFirmness> berryFirmnesses = pokeApiClient.getResource(BerryFirmness.class, new PageQuery(limit, offset * limit)).block();
            if (berryFirmnesses != null) return ResponseEntity.ok(berryFirmnesses);
            return ResponseEntity.badRequest().body("Could not access Berry-Firmness endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get berry firmness", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/berry-firmness/{id}")
    ResponseEntity<?> getBerryFirmness(@PathVariable String id)
    {
        LOGGER.info("getBerryFirmness: {}", id);
        try {
            BerryFirmness berryFirmness = pokeApiClient.getResource(BerryFirmness.class, id).block();
            if (berryFirmness != null) return ResponseEntity.ok(berryFirmness);
            return ResponseEntity.badRequest().body("Could not find Berry-Firmness with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get berry flavors", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/berry-flavor")
    ResponseEntity<?> getBerryFlavors(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                      @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getBerryFlavors limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<BerryFlavor> berryFlavors = pokeApiClient.getResource(BerryFlavor.class, new PageQuery(limit, offset * limit)).block();
            if (berryFlavors != null) return ResponseEntity.ok(berryFlavors);
            return ResponseEntity.badRequest().body("Could not access Berry-Flavor endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get berry flavor", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/berry-flavor/{id}")
    ResponseEntity<?> getBerryFlavor(@PathVariable String id)
    {
        LOGGER.info("getBerryFlavor: {}", id);
        try {
            BerryFlavor berryFlavor = pokeApiClient.getResource(BerryFlavor.class, id).block();
            if (berryFlavor != null) return ResponseEntity.ok(berryFlavor);
            return ResponseEntity.badRequest().body("Could not find Berry-Flavor with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
