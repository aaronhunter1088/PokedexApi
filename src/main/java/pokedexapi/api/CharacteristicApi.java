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
import skaro.pokeapi.resource.characteristic.Characteristic;

@RestController
@RequestMapping("/characteristic")
public class CharacteristicApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(CharacteristicApi.class);

    CharacteristicApi(PokeApiClient client)
    {
        super(client);
    }

    @Operation(summary = "Get characteristics", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "")
    ResponseEntity<?> getCharacteristics(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                         @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getCharacteristics limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Characteristic> characteristics = pokeApiClient.getResource(Characteristic.class, new PageQuery(limit, offset * limit)).block();
            if (characteristics != null) return ResponseEntity.ok(characteristics);
            return ResponseEntity.badRequest().body("Could not access Characteristic endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get characteristic", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{id}")
    ResponseEntity<?> getCharacteristic(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getCharacteristic {}", id);
        try {
            Characteristic characteristic = pokeApiClient.getResource(Characteristic.class, id).block();
            if (characteristic != null) return ResponseEntity.ok(characteristic);
            return ResponseEntity.badRequest().body("Could not find Characteristic with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
