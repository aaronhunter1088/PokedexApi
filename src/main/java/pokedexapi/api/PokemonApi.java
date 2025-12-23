package pokedexapi.api;

import pokedexapi.controllers.BaseController;
import pokedexapi.service.PokemonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
import tools.jackson.databind.ObjectMapper;

import java.net.http.HttpResponse;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/pokemon")
class PokemonApi extends BaseController
{
    private static final Logger logger = LogManager.getLogger(PokemonApi.class);

    PokemonApi(PokemonService pokemonService, PokeApiClient client, ObjectMapper objectMapper) {
        super(pokemonService, client, objectMapper);
    }

    // Pokeathlon Stat
    @RequestMapping(value="/pokeathlon-stat", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokeathlonStats(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                         @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getPokeathlonStats");
        try {
            NamedApiResourceList<PokeathlonStat> pokeathlonStats = pokeApiClient.getResource(PokeathlonStat.class, new PageQuery(limit, offset)).block();
            if (null != pokeathlonStats) return ResponseEntity.ok(pokeathlonStats);
            else return ResponseEntity.badRequest().body("Could not access PokeathlonStat endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all PokeathlonStat because " + e.getMessage());
        }
    }

    @RequestMapping(value="/pokeathlon-stat/{id}", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokeathlonStat(@PathVariable(value="id") String id)
    {
        logger.info("getPokeathlonStat {}", id);
        try {
            PokeathlonStat nature = pokeApiClient.getResource(PokeathlonStat.class, id).block();
            if (null != nature) return ResponseEntity.ok(nature);
            else return ResponseEntity.badRequest().body("Could not access PokeathlonStat endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Returns up to 10 Pokemon", responses = {
        @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
    @RequestMapping(value = "", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAllPokemon(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                    @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getAllPokemon limit:{} offset:{}", limit, offset);
        NamedApiResourceList<Pokemon> allPokemon;
        try {
            allPokemon = pokemonService.getPokemonList(limit, offset);
            if (null != allPokemon) return ResponseEntity.ok(allPokemon);
            else return ResponseEntity.badRequest().body("Could not access Pokemon endpoint");
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all pokemon because " + e.getMessage());
        }
    }

    @Operation(summary = "Returns a Pokemon", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PokemonApi.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
    @RequestMapping(value = "/{nameOrId}", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAPokemon(@PathVariable("nameOrId") String nameOrId)
    {
        logger.info("getPokemon: {}", nameOrId);
        Pokemon pokemon = retrievePokemon(nameOrId);
        if (null != pokemon) {
            return ResponseEntity.ok(pokemon);
        } else {
            logger.warn("pokemon was not found!");
            return ResponseEntity.badRequest().body(nameOrId + " was not found!");
        }
    }

    // Pokemon Color
    @RequestMapping(value = "/pokemon-color", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getColors(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                     @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getColors limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonColor> colors = pokeApiClient.getResource(PokemonColor.class, new PageQuery(limit, offset)).block();
            if (null != colors) return ResponseEntity.ok(colors);
            else return ResponseEntity.badRequest().body("Could not access PokemonColor endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all colors because " + e.getMessage());
        }
    }

    @RequestMapping(value = "/pokemon-color/{nameOrId}", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getColor(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonColor color = pokeApiClient.getResource(PokemonColor.class, nameOrId).block();
            if (null != color) return ResponseEntity.ok(color);
            else return ResponseEntity.badRequest().body("Could not access PokemonColor endpoint");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(nameOrId + " doesn't have a species!");
        }
    }

    // Pokemon Forms
    @RequestMapping(value = "/pokemon-form", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getForms(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                    @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getForms limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonForm> forms = pokeApiClient.getResource(PokemonForm.class, new PageQuery(limit, offset)).block();
            if (null != forms) return ResponseEntity.ok(forms);
            else return ResponseEntity.badRequest().body("Could not access PokemonForm endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all forms because " + e.getMessage());
        }
    }

    @RequestMapping(value = "/pokemon-form/{nameOrId}", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getForm(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonForm form = pokeApiClient.getResource(PokemonForm.class, nameOrId).block();
            if (null != form) return ResponseEntity.ok(form);
            else return ResponseEntity.badRequest().body("Could not access PokemonForm endpoint");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(nameOrId + " doesn't have a form!");
        }
    }

    // Pokemon Habitats
    @RequestMapping(value = "/pokemon-habitat", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getHabitats(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                       @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getHabitats limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonHabitat> habitats = pokeApiClient.getResource(PokemonHabitat.class, new PageQuery(limit, offset)).block();
            if (null != habitats) return ResponseEntity.ok(habitats);
            else return ResponseEntity.badRequest().body("Could not access PokemonHabitat endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all habitats because " + e.getMessage());
        }
    }

    @RequestMapping(value = "/pokemon-habitat/{nameOrId}", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getHabitat(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonHabitat habitat = pokeApiClient.getResource(PokemonHabitat.class, nameOrId).block();
            if (null != habitat) return ResponseEntity.ok(habitat);
            else return ResponseEntity.badRequest().body("Could not access PokemonHabitat endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch habitat because " + e.getMessage());
        }
    }

    // Pokemon Shapes
    @RequestMapping(value = "/pokemon-shape", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getShapes(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                            @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getShapes limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonShape> shapes = pokeApiClient.getResource(PokemonShape.class, new PageQuery(limit, offset)).block();
            if (null != shapes) return ResponseEntity.ok(shapes);
            else return ResponseEntity.badRequest().body("Could not access PokemonShape endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all shapes because " + e.getMessage());
        }
    }

    @RequestMapping(value = "/pokemon-shape/{nameOrId}", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getShape(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            PokemonShape shape = pokeApiClient.getResource(PokemonShape.class, nameOrId).block();
            if (null != shape) return ResponseEntity.ok(shape);
            else return ResponseEntity.badRequest().body("Could not access PokemonShape endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch shape because " + e.getMessage());
        }
    }

    // Personal Endpoints
    @RequestMapping(value="/{nameOrId}/validateNameOrId", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<Boolean> validateNameOrId(@PathVariable("nameOrId") String nameOrId)
    {
        try {
            Pokemon pokemon = retrievePokemon(nameOrId);
            if (null != pokemon) {
                logger.info("valid nameOrId: {}", nameOrId);
                return ResponseEntity.ok().body(true);
            } else {
                logger.warn("invalid nameOrId: {}", nameOrId);
                return ResponseEntity.badRequest().body(false);
            }
        } catch (Exception e) {
            logger.warn("There was an error fetching the Pokemon '{}' because {}", nameOrId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @RequestMapping(value="/{nameOrId}/description", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokemonDescription(@PathVariable("nameOrId") String nameOrId)
    {
        logger.info("getPkmnDescription: {}", nameOrId);
        List<FlavorText> pokemonDescriptions;
        try {
            Optional<PokemonSpecies> pokemonSpecies = pokeApiClient.getResource(PokemonSpecies.class, nameOrId).blockOptional();
            pokemonDescriptions = pokemonSpecies.map(species -> species
                    .getFlavorTextEntries().stream().filter(entry -> entry.getLanguage().name().equals("en"))
                    .toList()).orElse(null);
            assert pokemonDescriptions != null;
            int randomEntry = new Random().nextInt(pokemonDescriptions.size());
            String description = pokemonDescriptions.get(randomEntry).getFlavorText().replace("\n", " ");
            logger.info("description: {}", description);
            return ResponseEntity.ok(description);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(nameOrId + " text was not found!");
        }
    }

    @Operation(summary = "Returns the PokemonColor as a NamedApiResource", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NamedApiResource.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
    @GetMapping(produces = "application/json", path = "/{nameOrId}/color")
    ResponseEntity<?> getColorOfPokemon(@PathVariable("nameOrId") String nameOrId)
    {
        PokemonSpecies speciesInfo;
        try {
            speciesInfo = pokeApiClient.getResource(PokemonSpecies.class, nameOrId).block();
            if (speciesInfo != null) {
                String colorOfPokemon = speciesInfo.getColor().name();
                logger.info("color: {}", colorOfPokemon);
                return ResponseEntity.ok(speciesInfo.getColor());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(nameOrId + " doesn't have a species!");
        }
        return ResponseEntity.notFound().build();
    }

    // Same as /{nameOrId}/encounters except just the names of the Location Areas
    @RequestMapping(value="/{nameOrId}/locations", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokemonLocations(@PathVariable("nameOrId") String nameOrId)
    {
        logger.info("getPokemonLocations for: {}", nameOrId);
        Pokemon pokemon = retrievePokemon(nameOrId);
        if (pokemon == null) {
            return ResponseEntity.badRequest().body("Could not find pokemon with value:" + nameOrId);
        }
        String encountersString = pokemon.locationAreaEncounters();
        List<String> namesOfAreas = new ArrayList<>();
        try {
            namesOfAreas = pokemonService.getPokemonLocations(encountersString);
        } catch (Exception e) {
            logger.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        String result = String.join(", ", namesOfAreas);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Pokemon Location Areas: All Location Areas in LocationApi
    @RequestMapping(value = "/{nameOrId}/encounters", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokemonEncounters(@PathVariable("nameOrId") String nameOrId)
    {
        logger.info("getEncounters");
        HttpResponse<String> encounters;
        try {
            encounters = pokemonService.callUrl(pokeApiBaseUrl+"pokemon/"+nameOrId+"/encounters");
        } catch (Exception e) {
            logger.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        return switch (encounters.statusCode()) {
            case 200 -> ResponseEntity.ok(encounters.body());
            case 400 -> ResponseEntity.badRequest().body("Could not access Encounters endpoint");
            default -> ResponseEntity.internalServerError().body("Could not access Encounters endpoint");
        };
    }
    
    @RequestMapping(value= "/{nameOrId}/evolutionChain", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getEvolutionChain(@PathVariable("nameOrId") String nameOrId)
    {
        logger.info("getEvolutionChain for {}", nameOrId);
        PokemonSpecies speciesData = pokemonService.getPokemonSpeciesData(nameOrId);
        if (speciesData == null) {
            return ResponseEntity.badRequest().body("Could not find SpeciesData with value:" + nameOrId);
        }
        String chainUrl = speciesData.getEvolutionChain().url();
        if (chainUrl == null) {
            return ResponseEntity.badRequest().body("No chainUrl found for {}" + nameOrId);
        }
        logger.info("chainUrl: " + chainUrl);
        EvolutionChain evolutionChain = null;
        try {
            evolutionChain = pokemonService.getPokemonEvolutionChain(chainUrl);
        }
        catch (Exception e) {
            logger.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        if (evolutionChain != null) return ResponseEntity.ok(evolutionChain);
        else return ResponseEntity.badRequest().body("Could not find evolutionChain with: " + nameOrId);
    }
}
