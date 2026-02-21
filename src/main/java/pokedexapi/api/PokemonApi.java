package pokedexapi.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseApiController;
import pokedexapi.service.PokemonLocationEncounterService;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.FlavorText;
import skaro.pokeapi.resource.NamedApiResource;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.evolutionchain.EvolutionChain;
import skaro.pokeapi.resource.pokemon.PokeathlonStat;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemoncolor.PokemonColor;
import skaro.pokeapi.resource.pokemonform.PokemonForm;
import skaro.pokeapi.resource.pokemonhabitat.PokemonHabitat;
import skaro.pokeapi.resource.pokemonshape.PokemonShape;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;

import java.net.http.HttpResponse;
import java.util.*;

import static pokedexapi.utilities.Constants.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/pokemon")
class PokemonApi extends BaseApiController
{
    /* Logging instance */
    private static final Logger LOGGER = LogManager.getLogger(PokemonApi.class);

    PokemonApi(PokemonService pokemonService, PokeApiClient client, PokemonLocationEncounterService pokemonLocationEncounterService)
    {
        super(pokemonService, client, pokemonLocationEncounterService);
    }

    // Pokeathlon Stat
    @RequestMapping(value = "/pokeathlon-stat", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokeathlonStats(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                         @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getPokeathlonStats");
        try {
            NamedApiResourceList<PokeathlonStat> pokeathlonStats = pokeApiClient.getResource(PokeathlonStat.class, new PageQuery(limit, offset)).block();
            if (null != pokeathlonStats) return ResponseEntity.ok(pokeathlonStats);
            else return ResponseEntity.badRequest().body("Could not access PokeathlonStat endpoint");
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(LOGGER::error);
            return ResponseEntity.badRequest().body("Could not fetch all PokeathlonStat because " + e.getMessage());
        }
    }

    @RequestMapping(value = "/pokeathlon-stat/{id}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokeathlonStat(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getPokeathlonStat {}", id);
        try {
            PokeathlonStat nature = pokeApiClient.getResource(PokeathlonStat.class, id).block();
            if (null != nature) return ResponseEntity.ok(nature);
            else return ResponseEntity.badRequest().body("Could not access PokeathlonStat endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Returns up to 10 Pokemon", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @GetMapping("")
    @ResponseBody
    ResponseEntity<?> getAllPokemon(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                    @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllPokemon limit:{} offset:{}", limit, offset);
        NamedApiResourceList<Pokemon> allPokemon;
        try {
            allPokemon = pokemonService.getAllPokemons(limit, offset);
            if (null != allPokemon) return ResponseEntity.ok(allPokemon);
            else return ResponseEntity.badRequest().body("Could not access Pokemon endpoint");
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(LOGGER::error);
            return ResponseEntity.badRequest().body("Could not fetch all pokemon because " + e.getMessage());
        }
    }

    @Operation(summary = "Returns a Pokemon", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PokemonApi.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @GetMapping("/{nameOrId}")
    @ResponseBody
    ResponseEntity<?> getAPokemon(@PathVariable("nameOrId") String nameOrId)
    {
        LOGGER.info("getAPokemon: {}", nameOrId);
        Pokemon pokemon = retrievePokemon(nameOrId);
        if (null != pokemon) {
            return ResponseEntity.ok(pokemon);
        } else {
            LOGGER.warn("pokemon was not found!");
            return ResponseEntity.badRequest().body(nameOrId + " was not found!");
        }
    }

    // Pokemon Color
    @GetMapping(value = "/pokemon-color")
    @ResponseBody
    ResponseEntity<?> getColors(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getColors limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonColor> colors = pokeApiClient.getResource(PokemonColor.class, new PageQuery(limit, offset)).block();
            if (null != colors) return ResponseEntity.ok(colors);
            else return ResponseEntity.badRequest().body("Could not access PokemonColor endpoint");
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(LOGGER::error);
            return ResponseEntity.badRequest().body("Could not fetch all colors because " + e.getMessage());
        }
    }

    @GetMapping(value = "/pokemon-color/{nameOrId}")
    ResponseEntity<?> getColor(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonColor color = pokeApiClient.getResource(PokemonColor.class, nameOrId).block();
            if (null != color) return ResponseEntity.ok(color);
            else return ResponseEntity.badRequest().body("Could not access PokemonColor endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(nameOrId + " doesn't have a species!");
        }
    }

    // Pokemon Forms
    @GetMapping(value = "/pokemon-form")
    @ResponseBody
    ResponseEntity<?> getForms(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                               @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getForms limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonForm> forms = pokeApiClient.getResource(PokemonForm.class, new PageQuery(limit, offset)).block();
            if (null != forms) return ResponseEntity.ok(forms);
            else return ResponseEntity.badRequest().body("Could not access PokemonForm endpoint");
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(LOGGER::error);
            return ResponseEntity.badRequest().body("Could not fetch all forms because " + e.getMessage());
        }
    }

    @GetMapping(value = "/pokemon-form/{nameOrId}")
    @ResponseBody
    ResponseEntity<?> getForm(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonForm form = pokeApiClient.getResource(PokemonForm.class, nameOrId).block();
            if (null != form) return ResponseEntity.ok(form);
            else return ResponseEntity.badRequest().body("Could not access PokemonForm endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(nameOrId + " doesn't have a form!");
        }
    }

    // Pokemon Habitats
    @GetMapping(value = "/pokemon-habitat")
    @ResponseBody
    ResponseEntity<?> getHabitats(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                  @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getHabitats limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonHabitat> habitats = pokeApiClient.getResource(PokemonHabitat.class, new PageQuery(limit, offset)).block();
            if (null != habitats) return ResponseEntity.ok(habitats);
            else return ResponseEntity.badRequest().body("Could not access PokemonHabitat endpoint");
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(LOGGER::error);
            return ResponseEntity.badRequest().body("Could not fetch all habitats because " + e.getMessage());
        }
    }

    @GetMapping(value = "/pokemon-habitat/{nameOrId}")
    @ResponseBody
    ResponseEntity<?> getHabitat(@PathVariable String nameOrId)
    {
        try {
            PokemonHabitat habitat = pokeApiClient.getResource(PokemonHabitat.class, nameOrId).block();
            if (null != habitat) return ResponseEntity.ok(habitat);
            else return ResponseEntity.badRequest().body("Could not access PokemonHabitat endpoint");
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(LOGGER::error);
            return ResponseEntity.badRequest().body("Could not fetch habitat because " + e.getMessage());
        }
    }

    // Pokemon Shapes
    @GetMapping(value = "/pokemon-shape")
    @ResponseBody
    ResponseEntity<?> getShapes(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getShapes limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonShape> shapes = pokeApiClient.getResource(PokemonShape.class, new PageQuery(limit, offset)).block();
            if (null != shapes) return ResponseEntity.ok(shapes);
            else return ResponseEntity.badRequest().body("Could not access PokemonShape endpoint");
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(LOGGER::error);
            return ResponseEntity.badRequest().body("Could not fetch all shapes because " + e.getMessage());
        }
    }

    @GetMapping(value = "/pokemon-shape/{nameOrId}")
    @ResponseBody
    ResponseEntity<?> getShape(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonShape shape = pokeApiClient.getResource(PokemonShape.class, nameOrId).block();
            if (null != shape) return ResponseEntity.ok(shape);
            else return ResponseEntity.badRequest().body("Could not access PokemonShape endpoint");
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(LOGGER::error);
            return ResponseEntity.badRequest().body("Could not fetch shape because " + e.getMessage());
        }
    }

    // Personal Endpoints
    @GetMapping(value = "/{nameOrId}/validateNameOrId")
    @ResponseBody
    ResponseEntity<Boolean> validateNameOrId(@PathVariable String nameOrId)
    {
        try {
            Pokemon pokemon = retrievePokemon(nameOrId);
            if (null != pokemon) {
                LOGGER.info("valid nameOrId: {}", nameOrId);
                return ResponseEntity.ok().body(true);
            } else {
                LOGGER.warn("invalid nameOrId: {}", nameOrId);
                return ResponseEntity.notFound().build();
            }
        }
        catch (Exception e) {
            LOGGER.warn("There was an error fetching the Pokemon '{}' because {}", nameOrId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/{nameOrId}/description")
    @ResponseBody
    ResponseEntity<?> getPokemonDescription(@PathVariable String nameOrId)
    {
        LOGGER.info("getPkmnDescription: {}", nameOrId);
        List<FlavorText> pokemonDescriptions;
        try {
            Optional<PokemonSpecies> pokemonSpecies = pokeApiClient.getResource(PokemonSpecies.class, nameOrId).blockOptional();
            pokemonDescriptions = pokemonSpecies.map(species -> species
                    .getFlavorTextEntries().stream().filter(entry -> entry.getLanguage().name().equals("en"))
                    .toList()).orElse(null);
            assert pokemonDescriptions != null;
            int randomEntry = new Random().nextInt(pokemonDescriptions.size());
            String description = pokemonDescriptions.get(randomEntry).getFlavorText().replace("\n", " ");
            LOGGER.info("description: {}", description);
            return ResponseEntity.ok(description);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(nameOrId + " text was not found!");
        }
    }

    @Operation(summary = "Returns the PokemonColor as a NamedApiResource", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NamedApiResource.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @GetMapping(produces = "application/json", path = "/{nameOrId}/color")
    ResponseEntity<?> getPokemonColor(@PathVariable String nameOrId)
    {
        PokemonSpecies speciesInfo;
        try {
            speciesInfo = pokeApiClient.getResource(PokemonSpecies.class, nameOrId).block();
            if (speciesInfo != null) {
                String colorOfPokemon = speciesInfo.getColor().name();
                LOGGER.info("color: {}", colorOfPokemon);
                return ResponseEntity.ok(speciesInfo.getColor());
            }
            else {
                LOGGER.warn("No species info found for: {}", nameOrId);
                return ResponseEntity.badRequest().body("Could not find species info for: " + nameOrId);
            }
        }
        catch (Exception e) {
            try { fallbackCallUrl(POKEMON_SPECIES_URL(nameOrId)); }
            catch (Exception ex)
            {
                LOGGER.error("Error during fallback call: {}", ex.getMessage());
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    // just the names of the Location Areas
    @GetMapping(value = "/{pokemonId}/locations")
    @ResponseBody
    ResponseEntity<?> getPokemonLocations(@PathVariable Integer pokemonId)
    {
        LOGGER.info("getPokemonLocations for: {}", pokemonId);
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
            encounters = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(body, new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {});
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

    // Pokemon Location Areas: All Location Areas in LocationApi
    @GetMapping(value = "/{nameOrId}/encounters")
    @ResponseBody
    ResponseEntity<?> getPokemonEncounters(@PathVariable String nameOrId)
    {
        LOGGER.info("getEncounters");
        HttpResponse<String> encounters;
        try {
            encounters = pokemonService.callUrl(pokeApiBaseUrl + "pokemon/" + nameOrId + "/encounters");
        }
        catch (Exception e) {
            LOGGER.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return switch (encounters.statusCode()) {
            case 200 -> ResponseEntity.ok(encounters.body());
            case 400 -> ResponseEntity.badRequest().body("Could not access Encounters endpoint");
            default -> ResponseEntity.internalServerError().body("Could not access Encounters endpoint");
        };
    }

    @GetMapping(value = "/{nameOrId}/evolutionChain")
    @ResponseBody
    ResponseEntity<?> getEvolutionChain(@PathVariable String nameOrId)
    {
        LOGGER.info("getEvolutionChain for {}", nameOrId);
        PokemonSpecies speciesData;
        try {
            speciesData = pokemonService.getPokemonSpeciesData(nameOrId);
        }
        catch (Exception e) {
            LOGGER.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        if (speciesData == null) {
            return ResponseEntity.badRequest().body("Could not find SpeciesData with value:" + nameOrId);
        }
        String chainUrl = speciesData.getEvolutionChain().url();
        if (chainUrl == null) {
            return ResponseEntity.badRequest().body("No chainUrl found for {}" + nameOrId);
        }
        LOGGER.info("chainUrl: " + chainUrl);
        EvolutionChain evolutionChain = null;
        try {
            evolutionChain = pokemonService.getPokemonEvolutionChain(chainUrl);
        }
        catch (Exception e) {
            LOGGER.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        if (evolutionChain != null) return ResponseEntity.ok(evolutionChain);
        else return ResponseEntity.badRequest().body("Could not find evolutionChain with: " + nameOrId);
    }
}
