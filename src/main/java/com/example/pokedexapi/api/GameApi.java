package com.example.pokedexapi.api;

import com.example.pokedexapi.controller.BaseController;
import com.example.pokedexapi.service.PokemonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.generation.Generation;
import skaro.pokeapi.resource.pokedex.Pokedex;
import skaro.pokeapi.resource.version.Version;
import skaro.pokeapi.resource.versiongroup.VersionGroup;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/game")
class GameApi extends BaseController {

    private static final Logger logger = LogManager.getLogger(GameApi.class);

    @Autowired
    GameApi(PokemonService pokemonService, PokeApiClient client, ObjectMapper objectMapper) {
        super(pokemonService, client, objectMapper);
    }

    // Games
    @GetMapping(value="/generation")
    @ResponseBody
    ResponseEntity<?> getGenerations(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                     @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getGenerations");
        NamedApiResourceList<Generation> generations = pokeApiClient.getResource(Generation.class, new PageQuery(limit, offset)).block();
        if (null != generations) return ResponseEntity.ok(generations);
        else return ResponseEntity.badRequest().body("Could not access Generation endpoint");
    }

    @GetMapping(value="/generation/{id}")
    @ResponseBody
    ResponseEntity<?> getGeneration(@PathVariable("id") int id)
    {
        logger.info("getGeneration {}", id);
        Generation generation = pokeApiClient.getResource(Generation.class, String.valueOf(id)).block();
        if (null != generation) return ResponseEntity.ok(generation);
        else return ResponseEntity.badRequest().body("Could not access Generation endpoint");
    }

    // Pokedex
    @GetMapping(value="/pokedex")
    @ResponseBody
    ResponseEntity<?> getPokedexes(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                   @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getPokedexes");
        NamedApiResourceList<Pokedex> pokedexes = pokeApiClient.getResource(Pokedex.class, new PageQuery(limit, offset)).block();
        if (null != pokedexes) return ResponseEntity.ok(pokedexes);
        else return ResponseEntity.badRequest().body("Could not access Pokedex endpoint");
    }

    @GetMapping(value="/pokedex/{id}")
    @ResponseBody
    ResponseEntity<?> getPokedex(@PathVariable("id") int id)
    {
        logger.info("getPokedex {}", id);
        Pokedex pokedex = pokeApiClient.getResource(Pokedex.class, String.valueOf(id)).block();
        if (null != pokedex) return ResponseEntity.ok(pokedex);
        else return ResponseEntity.badRequest().body("Could not access Pokedex endpoint");
    }

    // Version
    @GetMapping(value="/version")
    @ResponseBody
    ResponseEntity<?> getVersions(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                  @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getVersions");
        NamedApiResourceList<Version> versions = pokeApiClient.getResource(Version.class, new PageQuery(limit, offset)).block();
        if (null != versions) return ResponseEntity.ok(versions);
        else return ResponseEntity.badRequest().body("Could not access Version endpoint");
    }

    @GetMapping(value="/version/{id}")
    @ResponseBody
    ResponseEntity<?> getVersion(@PathVariable("id") int id)
    {
        logger.info("getVersion {}", id);
        Version version = pokeApiClient.getResource(Version.class, String.valueOf(id)).block();
        if (null != version) return ResponseEntity.ok(version);
        else return ResponseEntity.badRequest().body("Could not access Version endpoint");
    }

    // Version Groups
    @GetMapping(value="/version-group")
    @ResponseBody
    ResponseEntity<?> getVersionGroups(@RequestParam(value="limit", required=false, defaultValue="10") int limit,
                                       @RequestParam(value="offset", required=false, defaultValue="0") int offset)
    {
        logger.info("getVersionGroups");
        NamedApiResourceList<VersionGroup> versionGroups = pokeApiClient.getResource(VersionGroup.class, new PageQuery(limit, offset)).block();
        if (null != versionGroups) return ResponseEntity.ok(versionGroups);
        else return ResponseEntity.badRequest().body("Could not access VersionGroup endpoint");
    }

    @GetMapping(value="/version-group/{id}")
    @ResponseBody
    ResponseEntity<?> getVersionGroup(@PathVariable("id") int id)
    {
        logger.info("getVersionGroup {}", id);
        VersionGroup versionGroup = pokeApiClient.getResource(VersionGroup.class, String.valueOf(id)).block();
        if (null != versionGroup) return ResponseEntity.ok(versionGroup);
        else return ResponseEntity.badRequest().body("Could not access VersionGroup endpoint");
    }

}
