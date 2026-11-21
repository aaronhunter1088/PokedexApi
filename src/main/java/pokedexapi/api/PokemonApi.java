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
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.FlavorText;
import skaro.pokeapi.resource.NamedApiResource;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.pokemon.PokeathlonStat;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemoncolor.PokemonColor;
import skaro.pokeapi.resource.pokemonform.PokemonForm;
import skaro.pokeapi.resource.pokemonhabitat.PokemonHabitat;
import skaro.pokeapi.resource.pokemonshape.PokemonShape;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;

import java.time.Duration;
import java.util.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

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
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
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
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PokemonApi.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
    @GetMapping(value = "/{nameOrId}")
    public ResponseEntity<?> getAPokemon(@PathVariable String nameOrId)
    {
        LOGGER.info("getAPokemon: {}", nameOrId);
        Pokemon pokemon = null;
        try {
            pokemon = retrievePokemon(nameOrId);
            if (pokemon == null) {
                nameOrId = checkSpecificNames(nameOrId);
                pokemon = retrievePokemon(nameOrId);
            }
            if (null != pokemon) {
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

    @RequestMapping(value = "/pokeathlon-stat", method = RequestMethod.GET)
    ResponseEntity<?> getAllPokeathlonStats(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getPokeathlonStats");
        try {
            NamedApiResourceList<PokeathlonStat> pokeathlonStats = pokeApiClient.getResource(PokeathlonStat.class, new PageQuery(limit, offset * limit)).block();
            if (null != pokeathlonStats) return ResponseEntity.ok(pokeathlonStats);
            return ResponseEntity.badRequest().body("Could not access PokeathlonStat endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch all PokeathlonStat because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all PokeathlonStat because " + e.getMessage());
        }
    }

    @RequestMapping(value = "/pokeathlon-stat/{id}", method = RequestMethod.GET)
    ResponseEntity<?> getPokeathlonStat(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getPokeathlonStat {}", id);
        try {
            PokeathlonStat nature = pokeApiClient.getResource(PokeathlonStat.class, id).block();
            if (null != nature) return ResponseEntity.ok(nature);
            return ResponseEntity.badRequest().body("Could not access PokeathlonStat endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * A small method that will attempt to fetch one Pokémon
     * variation versus nothing at all.
     * @param nameOrId the name or id to check
     * @return a name to use instead or an IllegalArgumentException
     */
    private String checkSpecificNames(String nameOrId)
    {
        return switch (nameOrId.toLowerCase()) {
            case "deoxys" -> "deoxys-normal";
            default -> nameOrId;
        };
    }

    // Pokemon Color

    @GetMapping(value = "/pokemon-color")
    ResponseEntity<?> getAllColors(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                   @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllColors limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonColor> colors = pokeApiClient.getResource(PokemonColor.class, new PageQuery(limit, offset * limit)).block();
            if (null != colors) return ResponseEntity.ok(colors);
            return ResponseEntity.badRequest().body("Could not access PokemonColor endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch all colors because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all colors because " + e.getMessage());
        }
    }

    @GetMapping(value = "/pokemon-color/{nameOrId}")
    ResponseEntity<?> getColor(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonColor color = pokeApiClient.getResource(PokemonColor.class, nameOrId).block();
            if (null != color) return ResponseEntity.ok(color);
            return ResponseEntity.badRequest().body("Could not access PokemonColor endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(nameOrId + " doesn\'t have a color!");
        }
    }

    // Pokemon Forms

    @GetMapping(value = "/pokemon-form")
    ResponseEntity<?> getAllForms(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                  @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllForms limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonForm> forms = pokeApiClient.getResource(PokemonForm.class, new PageQuery(limit, offset * limit)).block();
            if (null != forms) return ResponseEntity.ok(forms);
            return ResponseEntity.badRequest().body("Could not access PokemonForm endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch all forms because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all forms because " + e.getMessage());
        }
    }

    @GetMapping(value = "/pokemon-form/{nameOrId}")
    ResponseEntity<?> getForm(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonForm form = pokeApiClient.getResource(PokemonForm.class, nameOrId).block();
            if (null != form) return ResponseEntity.ok(form);
            return ResponseEntity.badRequest().body("Could not access PokemonForm endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(nameOrId + " doesn\'t have a form!");
        }
    }

    // Pokemon Habitats

    @GetMapping(value = "/pokemon-habitat")
    ResponseEntity<?> getAllHabitats(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                     @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllHabitats limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonHabitat> habitats = pokeApiClient.getResource(PokemonHabitat.class, new PageQuery(limit, offset * limit)).block();
            if (null != habitats) return ResponseEntity.ok(habitats);
            return ResponseEntity.badRequest().body("Could not access PokemonHabitat endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch all habitats because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all habitats because " + e.getMessage());
        }
    }

    @GetMapping(value = "/pokemon-habitat/{nameOrId}")
    ResponseEntity<?> getHabitat(@PathVariable String nameOrId)
    {
        try {
            PokemonHabitat habitat = pokeApiClient.getResource(PokemonHabitat.class, nameOrId).block();
            if (null != habitat) return ResponseEntity.ok(habitat);
            return ResponseEntity.badRequest().body("Could not access PokemonHabitat endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch habitat because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch habitat because " + e.getMessage());
        }
    }

    // Pokemon Shapes
    @GetMapping(value = "/pokemon-shape")
    ResponseEntity<?> getAllShapes(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                   @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllShapes limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonShape> shapes = pokeApiClient.getResource(PokemonShape.class, new PageQuery(limit, offset * limit)).block();
            if (null != shapes) return ResponseEntity.ok(shapes);
            return ResponseEntity.badRequest().body("Could not access PokemonShape endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch all shapes because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch all shapes because " + e.getMessage());
        }
    }

    @GetMapping(value = "/pokemon-shape/{nameOrId}")
    ResponseEntity<?> getShape(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonShape shape = pokeApiClient.getResource(PokemonShape.class, nameOrId).block();
            if (null != shape) return ResponseEntity.ok(shape);
            return ResponseEntity.badRequest().body("Could not access PokemonShape endpoint");
        }
        catch (Exception e) {
            LOGGER.error("Could not fetch shape because {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Could not fetch shape because " + e.getMessage());
        }
    }

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

//    @Operation(summary = "Returns the PokemonColor as a NamedApiResource", responses = {
//            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NamedApiResource.class))),
//            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)})
//    @GetMapping(produces = "application/json", path = "/{nameOrId}/color")
//    ResponseEntity<?> getPokemonColor(@PathVariable String nameOrId)
//    {
//        PokemonSpecies speciesInfo;
//        try {
//            speciesInfo = pokeApiClient.getResource(PokemonSpecies.class, nameOrId).block();
//            if (speciesInfo != null) {
//                String colorOfPokemon = speciesInfo.getColor().name();
//                LOGGER.info("color: {}", colorOfPokemon);
//                return ResponseEntity.ok(speciesInfo.getColor());
//            }
//        }
//        catch (Exception e) {
//            LOGGER.warn("No species info found for: {}", nameOrId);
//            return ResponseEntity.badRequest().body("Could not find species info for: " + nameOrId);
////            try { fallbackCallUrl(POKEMON_SPECIES_URL(nameOrId)); }
////            catch (Exception ex)
////            {
////                LOGGER.error("Error during fallback call: {}", ex.getMessage());
////                return ResponseEntity.internalServerError().build();
////            }
//        }
//        return ResponseEntity.notFound().build();
//    }

    // just the names of the Location Areas

    @GetMapping(value = "/{pokemonId}/locations")
    ResponseEntity<?> getPokemonLocations(@PathVariable Integer pokemonId)
    {
        LOGGER.info("getPokemonLocations for: {}", pokemonId);
        final ResponseEntity<String> pokemonLocationEncounters;
        try {
            pokemonLocationEncounters = pokemonLocationEncounterService.getPokemonLocationEncounters(pokemonId);
        }
        catch (Exception e) {
            LOGGER.error("Failed to retrieve location encounters for pokemonId {}: {}", pokemonId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
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
                    .readValue(body, new TypeReference<List<Map<String, Object>>>() {});
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

//    @GetMapping(value = "/{nameOrId}/encounters")
//    ResponseEntity<?> getPokemonEncounters(@PathVariable String nameOrId)
//    {
//        LOGGER.info("getEncounters");
//        HttpResponse<String> encounters;
//        try {
//            encounters = pokemonApiService.callUrl(pokeApiBaseUrl + "pokemon/" + nameOrId + "/encounters");
//        }
//        catch (Exception e) {
//            LOGGER.error("Error retrieving response because {}", e.getMessage());
//            return ResponseEntity.internalServerError().build();
//        }
//        return switch (encounters.statusCode()) {
//            case 200 -> ResponseEntity.ok(encounters.body());
//            case 400 -> ResponseEntity.badRequest().body("Could not access Encounters endpoint");
//            default -> ResponseEntity.internalServerError().body("Could not access Encounters endpoint");
//        };
//    }

//    @GetMapping(value = "/{nameOrId}/evolutionChain")
//    ResponseEntity<?> getEvolutionChain(@PathVariable String nameOrId)
//    {
//        LOGGER.info("getEvolutionChain for {}", nameOrId);
//        PokemonSpecies speciesData;
//        try {
//            speciesData = pokemonService.getPokemonSpeciesData(nameOrId);
//        }
//        catch (Exception e) {
//            LOGGER.error("Error retrieving response because {}", e.getMessage());
//            return ResponseEntity.internalServerError().build();
//        }
//        if (speciesData == null) {
//            return ResponseEntity.badRequest().body("Could not find SpeciesData with value:" + nameOrId);
//        }
//        String chainUrl = speciesData.getEvolutionChain().url();
//        if (chainUrl == null) {
//            return ResponseEntity.badRequest().body("No chainUrl found for {}" + nameOrId);
//        }
//        LOGGER.info("chainUrl: " + chainUrl);
//        EvolutionChain evolutionChain = null;
//        try {
//            evolutionChain = pokemonService.getPokemonEvolutionChain(chainUrl);
//        }
//        catch (Exception e) {
//            LOGGER.error("Error retrieving response because {}", e.getMessage());
//            return ResponseEntity.internalServerError().build();
//        }
//        if (evolutionChain != null) return ResponseEntity.ok(evolutionChain);
//        else return ResponseEntity.badRequest().body("Could not find evolutionChain with: " + nameOrId);
//    }
}
