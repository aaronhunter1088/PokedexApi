package com.example.pokedexapi.api;

import com.example.pokedexapi.controller.BaseController;
import com.example.pokedexapi.service.PokemonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.location.Location;
import skaro.pokeapi.resource.locationarea.LocationArea;
import skaro.pokeapi.resource.palparkarea.PalParkArea;
import skaro.pokeapi.resource.region.Region;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/location")
class LocationApi extends BaseController {

    private static final Logger logger = LogManager.getLogger(LocationApi.class);

    @Autowired
    LocationApi(PokemonService pokemonService, PokeApiClient client) {
        super(pokemonService, client);
    }

    // Locations
    @GetMapping(value="")
    @ResponseBody
    ResponseEntity<?> getLocations()
    {
        logger.info("getLocations");
        try {
            NamedApiResourceList<Location> locations = pokeApiClient.getResource(Location.class).block();
            if (null != locations) return ResponseEntity.ok(locations);
            else return ResponseEntity.badRequest().body("Could not access Location endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/location/{id}")
    ResponseEntity<?> getLocation(@PathVariable(value="id") String id)
    {
        logger.info("getLocation {}", id);
        try {
            Location location = pokeApiClient.getResource(Location.class, id).block();
            if (null != location) return ResponseEntity.ok(location);
            else return ResponseEntity.badRequest().body("Could not find a location with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Location Areas
    @GetMapping(value="/location-area")
    @ResponseBody
    ResponseEntity<?> getLocationAreas()
    {
        logger.info("getLocationAreas");
        try {
            NamedApiResourceList<LocationArea> locationAreas = pokeApiClient.getResource(LocationArea.class).block();
            if (null != locationAreas) return ResponseEntity.ok(locationAreas);
            else return ResponseEntity.badRequest().body("Could not access LocationArea endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/location-area/{id}")
    ResponseEntity<?> getLocationArea(@PathVariable(value="id") String id)
    {
        logger.info("getLocationArea {}", id);
        try {
            LocationArea locationArea = pokeApiClient.getResource(LocationArea.class, id).block();
            if (null != locationArea) return ResponseEntity.ok(locationArea);
            else return ResponseEntity.badRequest().body("Could not find a locationArea with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Pal Park Areas
    @GetMapping(value="/pal-park-area")
    @ResponseBody
    ResponseEntity<?> getPalParkAreas()
    {
        logger.info("getPalParkAreas");
        try {
            NamedApiResourceList<PalParkArea> palParkAreas = pokeApiClient.getResource(PalParkArea.class).block();
            if (null != palParkAreas) return ResponseEntity.ok(palParkAreas);
            else return ResponseEntity.badRequest().body("Could not access PalParkArea endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/pal-park-area/{id}")
    ResponseEntity<?> getPalParkArea(@PathVariable(value="id") String id)
    {
        logger.info("getPalParkArea {}", id);
        try {
            PalParkArea palParkArea = pokeApiClient.getResource(PalParkArea.class, id).block();
            if (null != palParkArea) return ResponseEntity.ok(palParkArea);
            else return ResponseEntity.badRequest().body("Could not find a palParkArea with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Regions
    @GetMapping(value="/region")
    @ResponseBody
    ResponseEntity<?> getRegions()
    {
        logger.info("getRegions");
        try {
            NamedApiResourceList<Region> regions = pokeApiClient.getResource(Region.class).block();
            if (null != regions) return ResponseEntity.ok(regions);
            else return ResponseEntity.badRequest().body("Could not access Region endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/region/{id}")
    ResponseEntity<?> getRegion(@PathVariable(value="id") String id)
    {
        logger.info("getRegion {}", id);
        try {
            Region region = pokeApiClient.getResource(Region.class, id).block();
            if (null != region) return ResponseEntity.ok(region);
            else return ResponseEntity.badRequest().body("Could not find a region with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
