package pokedexapi.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseApiController;
import pokedexapi.service.PokemonLocationEncounterService;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.FlavorText;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.pokemon.PokeathlonStat;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemoncolor.PokemonColor;
import skaro.pokeapi.resource.pokemonform.PokemonForm;
import skaro.pokeapi.resource.pokemonhabitat.PokemonHabitat;
import skaro.pokeapi.resource.pokemonshape.PokemonShape;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.*;

@RestController
@RequestMapping("/pokemon")
public class PokemonApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(PokemonApi.class);

    PokemonApi(PokeApiClient client, PokemonLocationEncounterService pokemonLocationEncounterService)
    {
        super(null, client, pokemonLocationEncounterService);
    }

    // Pokemon

    @Operation(summary = "Returns up to 10 Pokemon", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "")
    public ResponseEntity<?> getAllPokemon(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                           @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllPokemon limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Pokemon> allPokemon = pokeApiClient.getResource(Pokemon.class, new PageQuery(limit, offset * limit))
                                      .timeout(Duration.ofSeconds(15))
                                      .block();
            if (allPokemon != null) return ResponseEntity.ok(allPokemon);
            return ResponseEntity.badRequest().body("Could not access Pokemon endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch all Pokemon: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all Pokemon because " + e.getMessage());
        }
    }

    @Operation(summary = "Returns a Pokemon", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pokemon.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{nameOrId}")
    public ResponseEntity<?> getAPokemon(@PathVariable String nameOrId)
    {
        LOGGER.info("getAPokemon: {}", nameOrId);
        try {
            if (nameOrId.equals("deoxys")) {
                nameOrId = "deoxys-normal";
            }
            Pokemon pokemon = pokeApiClient.getResource(Pokemon.class, nameOrId).block();
            if (pokemon != null) {
                LOGGER.info("Valid nameOrId: {}", nameOrId);
                return ResponseEntity.ok(pokemon);
            } else {
                LOGGER.warn("Pokemon with nameOrId: {} was not found!", nameOrId);
                return ResponseEntity.badRequest().body(nameOrId + " was not found!");
            }
        }
        catch (Exception e) {
            LOGGER.error("There was an error fetching the Pokemon '{}' because {}", nameOrId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Pokeathlon Stat

    @Operation(summary = "Get all pokeathlon stats", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/pokeathlon-stat")
    ResponseEntity<?> getAllPokeathlonStats(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getPokeathlonStats");
        try {
            NamedApiResourceList<PokeathlonStat> pokeathlonStats = pokeApiClient.getResource(PokeathlonStat.class, new PageQuery(limit, offset * limit)).block();
            if (pokeathlonStats != null) return ResponseEntity.ok(pokeathlonStats);
            return ResponseEntity.badRequest().body("Could not access PokeathlonStat endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch all PokeathlonStat because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all PokeathlonStat because " + e.getMessage());
        }
    }

    @Operation(summary = "Get pokeathlon stat", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/pokeathlon-stat/{id}")
    ResponseEntity<?> getPokeathlonStat(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getPokeathlonStat {}", id);
        try {
            PokeathlonStat pokeathlonStat = pokeApiClient.getResource(PokeathlonStat.class, id).block();
            if (pokeathlonStat != null) return ResponseEntity.ok(pokeathlonStat);
            return ResponseEntity.badRequest().body("Could not access PokeathlonStat endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Pokemon Color

    @Operation(summary = "Get all colors", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/pokemon-color")
    ResponseEntity<?> getAllColors(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                   @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllColors limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonColor> colors = pokeApiClient.getResource(PokemonColor.class, new PageQuery(limit, offset * limit)).block();
            if (colors != null) return ResponseEntity.ok(colors);
            return ResponseEntity.badRequest().body("Could not access PokemonColor endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch all colors because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all colors because " + e.getMessage());
        }
    }

    @Operation(summary = "Get color", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/pokemon-color/{nameOrId}")
    ResponseEntity<?> getColor(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonColor color = pokeApiClient.getResource(PokemonColor.class, nameOrId).block();
            if (color != null) return ResponseEntity.ok(color);
            return ResponseEntity.badRequest().body("Could not access PokemonColor endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(nameOrId + " doesn\'t have a color!");
        }
    }

    // Pokemon Forms

    @Operation(summary = "Get all forms", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/pokemon-form")
    ResponseEntity<?> getAllForms(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                  @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllForms limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonForm> forms = pokeApiClient.getResource(PokemonForm.class, new PageQuery(limit, offset * limit)).block();
            if (forms != null) return ResponseEntity.ok(forms);
            return ResponseEntity.badRequest().body("Could not access PokemonForm endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch all forms because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all forms because " + e.getMessage());
        }
    }

    @Operation(summary = "Get form", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/pokemon-form/{nameOrId}")
    ResponseEntity<?> getForm(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonForm form = pokeApiClient.getResource(PokemonForm.class, nameOrId).block();
            if (form != null) return ResponseEntity.ok(form);
            return ResponseEntity.badRequest().body("Could not access PokemonForm endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(nameOrId + " doesn\'t have a form!");
        }
    }

    // Pokemon Habitats

    @Operation(summary = "Get all habitats", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/pokemon-habitat")
    ResponseEntity<?> getAllHabitats(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                     @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllHabitats limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonHabitat> habitats = pokeApiClient.getResource(PokemonHabitat.class, new PageQuery(limit, offset * limit)).block();
            if (habitats != null) return ResponseEntity.ok(habitats);
            return ResponseEntity.badRequest().body("Could not access PokemonHabitat endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch all habitats because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all habitats because " + e.getMessage());
        }
    }

    @Operation(summary = "Get habitat", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/pokemon-habitat/{nameOrId}")
    ResponseEntity<?> getHabitat(@PathVariable String nameOrId)
    {
        try {
            PokemonHabitat habitat = pokeApiClient.getResource(PokemonHabitat.class, nameOrId).block();
            if (habitat != null) return ResponseEntity.ok(habitat);
            return ResponseEntity.badRequest().body("Could not access PokemonHabitat endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch habitat because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch habitat because " + e.getMessage());
        }
    }

    // Pokemon Shapes
    @Operation(summary = "Get all shapes", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/pokemon-shape")
    ResponseEntity<?> getAllShapes(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                   @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllShapes limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonShape> shapes = pokeApiClient.getResource(PokemonShape.class, new PageQuery(limit, offset * limit)).block();
            if (shapes != null) return ResponseEntity.ok(shapes);
            return ResponseEntity.badRequest().body("Could not access PokemonShape endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch all shapes because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all shapes because " + e.getMessage());
        }
    }

    @Operation(summary = "Get shape", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/pokemon-shape/{nameOrId}")
    ResponseEntity<?> getShape(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonShape shape = pokeApiClient.getResource(PokemonShape.class, nameOrId).block();
            if (shape != null) return ResponseEntity.ok(shape);
            return ResponseEntity.badRequest().body("Could not access PokemonShape endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch shape because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch shape because " + e.getMessage());
        }
    }

    @Operation(summary = "Get a pokemon description", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{nameOrId}/description")
    ResponseEntity<?> getAPokemonDescription(@PathVariable String nameOrId)
    {
        LOGGER.info("getAPokemonDescription: {}", nameOrId);
        List<FlavorText> pokemonDescriptions;
        try {
            Optional<PokemonSpecies> pokemonSpecies = pokeApiClient.getResource(PokemonSpecies.class, nameOrId).blockOptional();
            pokemonDescriptions = pokemonSpecies.map(species -> species
                    .getFlavorTextEntries().stream().filter(entry -> entry.getLanguage().name().equals("en"))
                    .toList()).orElse(null);
            if (pokemonDescriptions == null || pokemonDescriptions.isEmpty()) {
                return ResponseEntity.badRequest().body(nameOrId + " text was not found!");
            }
            int randomEntry = new Random().nextInt(pokemonDescriptions.size());
            String description = pokemonDescriptions.get(randomEntry).getFlavorText().replace("\n", " ");
            LOGGER.info("description: {}", description);
            return ResponseEntity.ok(description);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(nameOrId + " text was not found!");
        }
    }

    @Operation(summary = "Get pokemon location encounters", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Encounters not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{pokemonId}/encounters")
    ResponseEntity<?> getPokemonLocationEncounters(@PathVariable Integer pokemonId)
    {
        LOGGER.info("getPokemonLocationEncounters for: {}", pokemonId);
        try {
            final ResponseEntity<String> encounters = pokemonLocationEncounterService.getPokemonLocationEncounters(pokemonId);
            if (encounters.getStatusCode() != HttpStatus.OK || encounters.getBody() == null || encounters.getBody().isBlank())
            {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(encounters.getBody());
        }
        catch (Exception e) {
            LOGGER.error("Failed to retrieve location encounters for pokemonId {}: {}", pokemonId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // just the names of the Location Areas

    @Operation(summary = "Get pokemon locations", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{pokemonId}/locations")
    ResponseEntity<?> getPokemonLocations(@PathVariable Integer pokemonId)
    {
        LOGGER.info("getPokemonLocations for: {}", pokemonId);
        try {
            final ResponseEntity<String> pokemonLocationEncounters = pokemonLocationEncounterService.getPokemonLocationEncounters(pokemonId);
            if (pokemonLocationEncounters.getStatusCode() != HttpStatus.OK)
            {
                LOGGER.error("Failed to retrieve location encounters for pokemonId {}: Status code {}", pokemonId, pokemonLocationEncounters.getStatusCode());
                return ResponseEntity.status(pokemonLocationEncounters.getStatusCode()).build();
            }
            String body = pokemonLocationEncounters.getBody();
            if (body == null || body.isBlank()) {
                return ResponseEntity.notFound().build();
            }

            List<Map<String, Object>> encounters;
            try {
                encounters = JsonMapper.builder().build()
                        .readValue(body, new TypeReference<>() {});
            } catch (Exception e) {
                LOGGER.error("Failed to parse encounters JSON: {}", e.getMessage());
                return ResponseEntity.internalServerError().build();
            }

            final List<String> result = encounters.stream()
                    .map(e -> (Map<String, Object>) e.get("location_area"))
                    .filter(Objects::nonNull)
                    .map(la -> String.valueOf(((Map<?, ?>) la).get("name")))
                    .filter(Objects::nonNull)
                    .toList();

            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception e) {
            LOGGER.error("Failed to retrieve location encounters for pokemonId {}: {}", pokemonId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
