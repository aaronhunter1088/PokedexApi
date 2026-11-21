package pokedexapi.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseApiController;
import pokedexapi.service.PokemonLocationEncounterService;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.pokedex.Pokedex;

@RestController
@RequestMapping("/pokedex")
public class PokedexApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(PokedexApi.class);

    PokedexApi(PokemonService pokemonService, PokeApiClient client, PokemonLocationEncounterService pokemonLocationEncounterService)
    {
        super(pokemonService, client, pokemonLocationEncounterService);
    }

    @GetMapping(value = "")
    ResponseEntity<?> getAllPokedexes(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                      @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllPokedexes limit={} offset={}", limit, offset);
        try {
            NamedApiResourceList<Pokedex> pokedexes = pokeApiClient.getResource(Pokedex.class, new PageQuery(limit, offset * limit)).block();
            if (null != pokedexes) return ResponseEntity.ok(pokedexes);
            return ResponseEntity.badRequest().body("Could not access Pokedex endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Returns a Pokedex by name or id", responses = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @GetMapping(value = "{nameOrId}/name")
    ResponseEntity<?> getPokedex(@PathVariable("nameOrId") String nameOrId)
    {
        LOGGER.info("getPokedex {}", nameOrId);
        try {
            Pokedex pokedex = pokeApiClient.getResource(Pokedex.class, nameOrId).block();
            if (null != pokedex) return ResponseEntity.ok(pokedex);
            return ResponseEntity.badRequest().body("Could not find a Pokedex with " + nameOrId);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Returns the number of Pokemon entries in a Pokedex")
    @GetMapping(value = "/{nameOrId}/total")
    ResponseEntity<?> getPokedexTotal(@PathVariable(value = "nameOrId", required = false) String nameOrId)
    {
        LOGGER.info("getPokedexTotal for Pokedex {}", nameOrId);
        try {
            int total = pokemonApiService.getTotalPokemon(nameOrId);
            return ResponseEntity.ok(total);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
