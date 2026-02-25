package pokedexapi.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseApiController;
import pokedexapi.service.PokemonLocationEncounterService;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/pokedex")

public class PokedexApi extends BaseApiController
{
    /* Logging instance */
    private static final Logger LOGGER = LogManager.getLogger(PokedexApi.class);

    PokedexApi(PokemonService pokemonService, PokeApiClient client, PokemonLocationEncounterService pokemonLocationEncounterService)
    {
        super(pokemonService, client, pokemonLocationEncounterService);
    }

    @Operation(summary = "Returns the Pokedex", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @GetMapping("/{pokedex}")
    @ResponseBody
    ResponseEntity<?> getPokedex(@PathVariable(value = "pokedex") String pokedex)
    {
        LOGGER.info("getPokedex {}", pokedex);
        int allPokemon;
        try {
            allPokemon = pokemonService.getTotalPokemon(pokedex);
            if (allPokemon != 0) return ResponseEntity.ok(allPokemon);
            else return ResponseEntity.badRequest().body("Could not return Pokédex total");
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(LOGGER::error);
            return ResponseEntity.badRequest().body("Could not fetch Pokédex because " + e.getMessage());
        }
    }
}
