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
import skaro.pokeapi.resource.machine.Machine;


@RestController
@RequestMapping("/machine")
class MachineApi extends BaseApiController
{
    private static final Logger logger = LogManager.getLogger(MachineApi.class);

    MachineApi(PokeApiClient client)
    {
        super(client);
    }

    @Operation(summary = "Get all machines", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "")
    ResponseEntity<?> getAllMachines(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                     @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getAllMachines limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Machine> machines = pokeApiClient.getResource(Machine.class, new PageQuery(limit, offset * limit)).block();
            if (machines != null) return ResponseEntity.ok(machines);
            return ResponseEntity.badRequest().body("Could not access Machine endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get machine", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{id}")
    ResponseEntity<?> getMachine(@PathVariable(value = "id") String id)
    {
        logger.info("getMachine {}", id);
        try {
            Machine machine = pokeApiClient.getResource(Machine.class, id).block();
            if (machine != null) return ResponseEntity.ok(machine);
            return ResponseEntity.badRequest().body("Could not find Machine with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
