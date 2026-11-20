package pokedexapi.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseApiController;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.pokedex.Pokedex;
import skaro.pokeapi.resource.version.Version;
import skaro.pokeapi.resource.versiongroup.VersionGroup;

@RestController
@RequestMapping("/game")
class GameApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(GameApi.class);

    GameApi(PokemonService pokemonService, PokeApiClient client)
    {
        super(pokemonService, client);
    }

    // Version

    @GetMapping(value = "/version")
    ResponseEntity<?> getVersions(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                  @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getVersions");
        NamedApiResourceList<Version> versions = pokeApiClient.getResource(Version.class, new PageQuery(limit, offset)).block();
        if (null != versions) return ResponseEntity.ok(versions.results());
        else return ResponseEntity.badRequest().body("Could not access Version endpoint");
    }

    @GetMapping(value = "/version/{id}")
    ResponseEntity<?> getVersion(@PathVariable("id") int id)
    {
        LOGGER.info("getVersion {}", id);
        Version version = pokeApiClient.getResource(Version.class, String.valueOf(id)).block();
        if (null != version) return ResponseEntity.ok(version);
        else return ResponseEntity.badRequest().body("Could not access Version endpoint");
    }

    // Version Groups

    @GetMapping(value = "/version-group")
    ResponseEntity<?> getVersionGroups(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                       @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getVersionGroups");
        NamedApiResourceList<VersionGroup> versionGroups = pokeApiClient.getResource(VersionGroup.class, new PageQuery(limit, offset)).block();
        if (null != versionGroups) return ResponseEntity.ok(versionGroups.results());
        else return ResponseEntity.badRequest().body("Could not access VersionGroup endpoint");
    }

    @GetMapping(value = "/version-group/{id}")
    ResponseEntity<?> getVersionGroup(@PathVariable("id") int id)
    {
        LOGGER.info("getVersionGroup {}", id);
        VersionGroup versionGroup = pokeApiClient.getResource(VersionGroup.class, String.valueOf(id)).block();
        if (null != versionGroup) return ResponseEntity.ok(versionGroup);
        else return ResponseEntity.badRequest().body("Could not access VersionGroup endpoint");
    }

}
