package pokedexapi.api;

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
import skaro.pokeapi.resource.location.Location;
import skaro.pokeapi.resource.locationarea.LocationArea;
import skaro.pokeapi.resource.palparkarea.PalParkArea;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.region.Region;

@RestController
@RequestMapping("/location")
class LocationApi extends BaseApiController
{
    private static final Logger logger = LogManager.getLogger(LocationApi.class);

    LocationApi(PokemonService pokemonService,
                PokeApiClient client,
                PokemonLocationEncounterService pokemonLocationEncounterService)
    {
        super(pokemonService, client, pokemonLocationEncounterService);
    }

    // Locations

    @GetMapping(value = "")
    ResponseEntity<?> getAllLocations(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                      @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getAllLocations limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Location> locations = pokeApiClient.getResource(Location.class, new PageQuery(limit, offset * limit)).block();
            if (locations != null) return ResponseEntity.ok(locations);
            return ResponseEntity.badRequest().body("Could not access Location endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> getLocation(@PathVariable(value = "id") String id)
    {
        logger.info("getLocation {}", id);
        try {
            Location location = pokeApiClient.getResource(Location.class, id).block();
            if (location != null) return ResponseEntity.ok(location);
            return ResponseEntity.badRequest().body("Could not find a Location with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Location Areas

    @GetMapping(value = "/location-area")
    ResponseEntity<?> getAllLocationAreas(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                          @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getAllLocationAreas limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<LocationArea> locationAreas = pokeApiClient.getResource(LocationArea.class, new PageQuery(limit, offset * limit)).block();
            if (locationAreas != null) return ResponseEntity.ok(locationAreas);
            return ResponseEntity.badRequest().body("Could not access LocationArea endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/location-area/{id}")
    ResponseEntity<?> getLocationArea(@PathVariable(value = "id") String id)
    {
        logger.info("getLocationArea {}", id);
        try {
            LocationArea locationArea = pokeApiClient.getResource(LocationArea.class, id).block();
            if (locationArea != null) return ResponseEntity.ok(locationArea);
            return ResponseEntity.badRequest().body("Could not find a LocationArea with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/location-area/{id}/encounters")
    ResponseEntity<?> getLocationAreaEncounters(@PathVariable(value = "id") String id)
    {
        logger.info("getLocationAreaEncounters {}", id);
        try {
            Pokemon pokemon = retrievePokemon(id);
            if (pokemon != null) return ResponseEntity.ok(pokemonLocationEncounterService.getPokemonLocationEncounters(pokemon.id()).getBody());
            return ResponseEntity.badRequest().body("Could not find a LocationArea with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Pal Park Areas

    @GetMapping(value = "/pal-park-area")
    ResponseEntity<?> getAllPalParkAreas(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                         @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getAllPalParkAreas limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<PalParkArea> palParkAreas = pokeApiClient.getResource(PalParkArea.class, new PageQuery(limit, offset * limit)).block();
            if (palParkAreas != null) return ResponseEntity.ok(palParkAreas);
            return ResponseEntity.badRequest().body("Could not access PalParkArea endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/pal-park-area/{id}")
    ResponseEntity<?> getPalParkArea(@PathVariable(value = "id") String id)
    {
        logger.info("getPalParkArea {}", id);
        try {
            PalParkArea palParkArea = pokeApiClient.getResource(PalParkArea.class, id).block();
            if (palParkArea != null) return ResponseEntity.ok(palParkArea);
            return ResponseEntity.badRequest().body("Could not find a PalParkArea with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Regions

    @GetMapping(value = "/region")
    ResponseEntity<?> getAllRegions(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                    @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        logger.info("getAllRegions limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Region> regions = pokeApiClient.getResource(Region.class, new PageQuery(limit, offset * limit)).block();
            if (regions != null) return ResponseEntity.ok(regions);
            return ResponseEntity.badRequest().body("Could not access Region endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/region/{id}")
    ResponseEntity<?> getRegion(@PathVariable(value = "id") String id)
    {
        logger.info("getRegion {}", id);
        try {
            Region region = pokeApiClient.getResource(Region.class, id).block();
            if (region != null) return ResponseEntity.ok(region);
            return ResponseEntity.badRequest().body("Could not find a Region with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
