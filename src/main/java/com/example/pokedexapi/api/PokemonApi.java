package com.example.pokedexapi.api;

import com.example.pokedexapi.controller.BaseController;
import com.example.pokedexapi.service.PokemonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.FlavorText;
import skaro.pokeapi.resource.NamedApiResource;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.egggroup.EggGroup;
import skaro.pokeapi.resource.growthrate.GrowthRate;
import skaro.pokeapi.resource.nature.Nature;
import skaro.pokeapi.resource.pokemon.PokeathlonStat;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemoncolor.PokemonColor;
import skaro.pokeapi.resource.pokemonform.PokemonForm;
import skaro.pokeapi.resource.pokemonhabitat.PokemonHabitat;
import skaro.pokeapi.resource.pokemonshape.PokemonShape;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@RestController
@RequestMapping("/pokemon")
class PokemonApi extends BaseController {

    private static final Logger logger = LogManager.getLogger(PokemonApi.class);

    @Autowired
    PokemonApi(PokemonService pokemonService, PokeApiClient client)
    { super(pokemonService, client); }

    // Abilities
    @RequestMapping(value="/ability", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getAbilities(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                   @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getAbilities");
        HttpResponse<String> abilities = pokemonService.callUrl(pokeApiBaseUrl+"ability?limit="+limit+"&offset="+offset);
        if (abilities.statusCode() == 200) return ResponseEntity.ok(abilities.body());
        else if (abilities.statusCode() == 400) return ResponseEntity.badRequest().body("Could not access PokemonAbility endpoint");
        return ResponseEntity.internalServerError().body("Could not access PokemonAbility endpoint");
    }

    @GetMapping(value="/ability/{id}")
    ResponseEntity<?> getAbility(@PathVariable(value="id") String id)
    {
        logger.info("getAbility {}", id);
        try {
            HttpResponse<String> ability = pokemonService.callUrl(pokeApiBaseUrl+"ability/"+id);
            if (null != ability) return ResponseEntity.ok(ability.body());
            else return ResponseEntity.badRequest().body("Could not find an ability with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Characteristics
    @RequestMapping(value="/characteristic", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getCharacteristics(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                         @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getCharacteristics");
        HttpResponse<String> characteristics = pokemonService.callUrl(pokeApiBaseUrl+"characteristic?limit="+limit+"&offset="+offset);
        if (characteristics.statusCode() == 200) return ResponseEntity.ok(characteristics.body());
        else if (characteristics.statusCode() == 400) return ResponseEntity.badRequest().body("Could not access Characteristic endpoint");
        return ResponseEntity.internalServerError().body("Could not access Characteristic endpoint");
    }

    @GetMapping(value="/characteristic/{id}")
    ResponseEntity<?> getCharacteristic(@PathVariable(value="id") String id)
    {
        logger.info("getCharacteristic {}", id);
        try {
            HttpResponse<String> characteristic = pokemonService.callUrl(pokeApiBaseUrl+"characteristic/"+id);
            if (null != characteristic) return ResponseEntity.ok(characteristic.body());
            else return ResponseEntity.badRequest().body("Could not find an characteristic with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Egg Groups
    @RequestMapping(value="/egg-group", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getEggGroups(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                   @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getEggGroups");
        try {
            NamedApiResourceList<EggGroup> eggGroups = pokeApiClient.getResource(EggGroup.class, new PageQuery(limit, offset)).block();
            if (null != eggGroups) return ResponseEntity.ok(eggGroups);
            else return ResponseEntity.badRequest().body("Could not access EggGroup endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all eggGroups because " + e.getMessage());
        }
    }

    @GetMapping(value="/egg-group/{id}")
    ResponseEntity<?> getEggGroup(@PathVariable(value="id") String id)
    {
        logger.info("getEggGroup {}", id);
        try {
            EggGroup eggGroup = pokeApiClient.getResource(EggGroup.class, id).block();
            if (null != eggGroup) return ResponseEntity.ok(eggGroup);
            else return ResponseEntity.badRequest().body("Could not access EggGroup endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Genders
    @RequestMapping(value="/gender", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getGenders(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                 @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getGenders");
        HttpResponse<String> characteristics = pokemonService.callUrl(pokeApiBaseUrl+"gender?limit="+limit+"&offset="+offset);
        if (characteristics.statusCode() == 200) return ResponseEntity.ok(characteristics.body());
        else if (characteristics.statusCode() == 400) return ResponseEntity.badRequest().body("Could not access Gender endpoint");
        return ResponseEntity.internalServerError().body("Could not access Gender endpoint");
    }

    @GetMapping(value="/gender/{id}")
    ResponseEntity<?> getGender(@PathVariable(value="id") String id)
    {
        logger.info("getGender {}", id);
        HttpResponse<String> gender = pokemonService.callUrl(pokeApiBaseUrl+"gender/"+id);
        if (gender.statusCode() == 200) return ResponseEntity.ok(gender.body());
        else if (gender.statusCode() == 400) return ResponseEntity.badRequest().body("Could not access Gender endpoint");
        return ResponseEntity.internalServerError().body("Could not access Gender endpoint");
    }

    // Growth Rate
    @RequestMapping(value="/growth-rate", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getGrowthRates(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                     @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getGrowthRates");
        try {
            NamedApiResourceList<GrowthRate> growthRates = pokeApiClient.getResource(GrowthRate.class, new PageQuery(limit, offset)).block();
            if (null != growthRates) return ResponseEntity.ok(growthRates);
            else return ResponseEntity.badRequest().body("Could not access GrowthRate endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all GrowthRate because " + e.getMessage());
        }
    }

    @GetMapping(value="/growth-rate/{id}")
    ResponseEntity<?> getGrowthRate(@PathVariable(value="id") String id)
    {
        logger.info("getGrowthRate {}", id);
        try {
            GrowthRate growthRate = pokeApiClient.getResource(GrowthRate.class, id).block();
            if (null != growthRate) return ResponseEntity.ok(growthRate);
            else return ResponseEntity.badRequest().body("Could not access GrowthRate endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Natures
    @RequestMapping(value="/nature", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getNatures(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                 @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getNatures");
        try {
            NamedApiResourceList<Nature> natures = pokeApiClient.getResource(Nature.class, new PageQuery(limit, offset)).block();
            if (null != natures) return ResponseEntity.ok(natures);
            else return ResponseEntity.badRequest().body("Could not access Nature endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all Nature because " + e.getMessage());
        }
    }

    @GetMapping(value="/nature/{id}")
    ResponseEntity<?> getNature(@PathVariable(value="id") String id)
    {
        logger.info("getNature {}", id);
        try {
            Nature nature = pokeApiClient.getResource(Nature.class, id).block();
            if (null != nature) return ResponseEntity.ok(nature);
            else return ResponseEntity.badRequest().body("Could not access Nature endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Pokeathlon Stat
    @RequestMapping(value="/list-pokeathlon-stat", method=RequestMethod.GET)
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

    @GetMapping(value="/pokeathlon-stat/{id}")
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
            //"https://pokeapi.co/api/v2/pokemon/?limit=10&offset=0"
            allPokemon = pokeApiClient.getResource(Pokemon.class, new PageQuery(limit, offset)).block();
            if (null != allPokemon) return ResponseEntity.ok(allPokemon);
            else return ResponseEntity.badRequest().body("Could not access Pokemon endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all pokemon because " + e.getMessage());
        }
    }

    @Operation(summary = "Returns a Pokemon", responses = {
            @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PokemonApi.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
    @RequestMapping(value = "/{nameOrId}", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokemon(@PathVariable("nameOrId") String nameOrId)
    {
        logger.info("getPokemon: {}", nameOrId);
        Pokemon pokemon = retrievePokemon(nameOrId);
        if (null != pokemon) {
            pokemon = pokeApiClient.getResource(Pokemon.class, nameOrId).block();
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
    ResponseEntity<?> getPokemonColor(@PathVariable("nameOrId") String nameOrId)
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
    ResponseEntity<?> getPokemonForm(@PathVariable("nameOrId") String nameOrId)
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
    ResponseEntity<?> getPokemonShapes(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
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
    ResponseEntity<?> getPokemonShape(@PathVariable("nameOrId") String nameOrId)
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

    // Pokemon Species
    @RequestMapping(value="/pokemon-species")
    @ResponseBody
    ResponseEntity<?> getAllSpeciesData(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                             @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getAllSpeciesData limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PokemonSpecies> allSpeciesData = pokeApiClient.getResource(PokemonSpecies.class, new PageQuery(limit, offset)).block();
            if (null != allSpeciesData) return ResponseEntity.ok(allSpeciesData);
            else return ResponseEntity.badRequest().body("Could not access PokemonSpecies endpoint");
        } catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.badRequest().body("Could not fetch all species because " + e.getMessage());
        }
    }

    @RequestMapping(value="/pokemon-species/{nameOrId}")
    @ResponseBody
    ResponseEntity<?> getSpeciesData(@PathVariable("nameOrId") String nameOrId)
    {
        logger.info("getSpeciesData: {}", nameOrId);
        try {
            PokemonSpecies speciesData = pokeApiClient.getResource(PokemonSpecies.class, nameOrId).block();
            if (null != speciesData) return ResponseEntity.ok(speciesData);
            else {
                Pokemon pokemonResource = (Pokemon) getPokemon(nameOrId).getBody();
                assert pokemonResource != null;
                NamedApiResource<PokemonSpecies> speciesResource = pokemonResource.getSpecies();
                if (null == speciesResource) {
                    return ResponseEntity.noContent().build();
                }
                HttpResponse<String> response = pokemonService.callUrl(speciesResource.getUrl());
                // TODO: Convert response.body() to PokemonSpecies
                if (response.statusCode() == 200) return ResponseEntity.ok(response.body());
                else return ResponseEntity.badRequest().body("Could not find PokemonSpecies with: " + nameOrId);
            }
        }
        catch (Exception e) {
            Arrays.stream(e.getStackTrace()).forEach(logger::error);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Pokemon Stats
    @RequestMapping(value = "/pokemon-stat", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokemonStats(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                           @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getPokemonStats limit:{} offset:{}", limit, offset);
        HttpResponse<String> stats = pokemonService.callUrl(pokeApiBaseUrl+"stat?limit="+limit+"&offset="+offset);
        if (stats.statusCode() == 200) return ResponseEntity.ok(stats.body());
        else if (stats.statusCode() == 400) return ResponseEntity.badRequest().body("Could not access Stat endpoint");
        else return ResponseEntity.internalServerError().body("Could not access Stat endpoint");
    }

    @RequestMapping(value = "/pokemon-stat/{nameOrId}", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getStat(@PathVariable("nameOrId") String nameOrId)
    {
        logger.info("getPokemonStat {}", nameOrId);
        HttpResponse<String> stats = pokemonService.callUrl(pokeApiBaseUrl+"stat/"+nameOrId);
        if (stats.statusCode() == 200) return ResponseEntity.ok(stats.body());
        else if (stats.statusCode() == 400) return ResponseEntity.badRequest().body("Could not access Stat endpoint");
        else return ResponseEntity.internalServerError().body("Could not access Stat endpoint");
    }

    // Pokemon Types
    @RequestMapping(value = "/type", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getTypes(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                    @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getTypes limit:{} offset:{}", limit, offset);
        HttpResponse<String> stats = pokemonService.callUrl(pokeApiBaseUrl+"type?limit="+limit+"&offset="+offset);
        if (stats.statusCode() == 200) return ResponseEntity.ok(stats.body());
        else if (stats.statusCode() == 400) return ResponseEntity.badRequest().body("Could not access Type endpoint");
        else return ResponseEntity.internalServerError().body("Could not access Type endpoint");
    }

    @RequestMapping(value = "/type/{nameOrId}", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getType(@PathVariable("nameOrId") String nameOrId)
    {
        logger.info("getType {}", nameOrId);
        HttpResponse<String> stats = pokemonService.callUrl(pokeApiBaseUrl+"type/"+nameOrId);
        if (stats.statusCode() == 200) return ResponseEntity.ok(stats.body());
        else if (stats.statusCode() == 400) return ResponseEntity.badRequest().body("Could not access Type endpoint");
        else return ResponseEntity.internalServerError().body("Could not access Type endpoint");
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
                    .getFlavorTextEntries().stream().filter(entry -> entry.getLanguage().getName().equals("en"))
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

    @RequestMapping(value = "/{nameOrId}/color", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getColorOfPokemon(@PathVariable("nameOrId") String nameOrId)
    {
        PokemonSpecies speciesInfo;
        try {
            speciesInfo = pokeApiClient.getResource(PokemonSpecies.class, nameOrId).block();
            if (speciesInfo != null) {
                String colorOfPokemon = speciesInfo.getColor().getName();
                logger.info("color: {}", colorOfPokemon);
                return ResponseEntity.ok(colorOfPokemon);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(nameOrId + " doesn't have a species!");
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value="/{nameOrId}/locations", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokemonLocations(@PathVariable("nameOrId") String nameOrId)
    {
        logger.info("getPokemonLocations for: {}", nameOrId);
        Pokemon pokemon = retrievePokemon(nameOrId);
        if (null == pokemon) {
            return ResponseEntity.badRequest().body("Could not find pokemon with value:" + nameOrId);
        }
        String encountersString = pokemon.getLocationAreaEncounters();
        HttpResponse<String> response;
        List<String> namesOfAreas = new ArrayList<>();
        JSONParser jsonParser;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(encountersString))
                    .GET()
                    .build();
            response = HttpClient.newBuilder()
                    .build()
                    .send(request,  HttpResponse.BodyHandlers.ofString());
            logger.debug("response: " + response.body());
            jsonParser = new JSONParser(response.body());
            List<LinkedHashMap<String, String>> map = (List<LinkedHashMap<String, String>>) jsonParser.parse();
            for(Map m : map) {
                LinkedHashMap<String, String> area = (LinkedHashMap<String, String>) m.get("location_area");
                namesOfAreas.add(area.get("name"));
            }
            namesOfAreas.forEach(area -> logger.debug("area: {}", area));
        } catch (Exception e) {
            logger.error("Error retrieving response because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        StringBuilder sb = new StringBuilder();
        namesOfAreas.forEach(area -> sb.append(area).append(", "));
        return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
    }

    // Pokemon Location Areas: All Location Areas in LocationApi
    @RequestMapping(value = "/{nameOrId}/encounters", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getPokemonEncounters(@PathVariable("nameOrId") String nameOrId)
    {
        logger.info("getEncounters");
        HttpResponse<String> encounters = pokemonService.callUrl(pokeApiBaseUrl+"pokemon/"+nameOrId+"/encounters");
        if (encounters.statusCode() == 200) return ResponseEntity.ok(encounters.body());
        else if (encounters.statusCode() == 400) return ResponseEntity.badRequest().body("Could not access Gender endpoint");
        return ResponseEntity.internalServerError().body("Could not access Gender endpoint");
    }
    
    @RequestMapping(value= "/{nameOrId}/evolutionChain", method=RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getEvolutionChain(@PathVariable("nameOrId") String nameOrId)
    {
        PokemonSpecies speciesData = (PokemonSpecies) getSpeciesData(nameOrId).getBody();
        try {
            assert speciesData != null;
            String chainUrl = speciesData.getEvolutionChain().getUrl();
            logger.info("chainUrl: " + chainUrl);
            HttpResponse<String> response = pokemonService.callUrl(chainUrl);
            if (response.statusCode() == 200) return ResponseEntity.ok(response.body());
            else return ResponseEntity.badRequest().body("Could not find evolutionChain with: " + nameOrId);
        } catch (Exception e) {
            logger.error("Error parsing species data because {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
