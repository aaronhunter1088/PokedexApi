package pokedexapi.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseApiController;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.version.Version;
import skaro.pokeapi.resource.versiongroup.VersionGroup;

@RestController
@RequestMapping("/version")
class VersionApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(VersionApi.class);

    VersionApi(PokeApiClient client)
    {
        super(client);
    }

    // Version

    @Operation(summary = "Get all versions", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "")
    ResponseEntity<?> getAllVersions(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                     @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllVersions limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Version> versions = pokeApiClient.getResource(Version.class, new PageQuery(limit, offset * limit)).block();
            if (versions != null) return ResponseEntity.ok(versions);
            return ResponseEntity.badRequest().body("Could not access Version endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get version", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{id}")
    ResponseEntity<?> getVersion(@PathVariable("id") String id)
    {
        LOGGER.info("getVersion {}", id);
        try {
            Version version = pokeApiClient.getResource(Version.class, id).block();
            if (version != null) return ResponseEntity.ok(version);
            return ResponseEntity.badRequest().body("Could not find a Version with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Version Groups

    @Operation(summary = "Get all version groups", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/version-group")
    ResponseEntity<?> getAllVersionGroups(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                          @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllVersionGroups limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<VersionGroup> versionGroups = pokeApiClient.getResource(VersionGroup.class, new PageQuery(limit, offset * limit)).block();
            if (versionGroups != null) return ResponseEntity.ok(versionGroups);
            return ResponseEntity.badRequest().body("Could not access VersionGroup endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get version group", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/version-group/{id}")
    ResponseEntity<?> getVersionGroup(@PathVariable("id") String id)
    {
        LOGGER.info("getVersionGroup {}", id);
        try {
            VersionGroup versionGroup = pokeApiClient.getResource(VersionGroup.class, id).block();
            if (versionGroup != null) return ResponseEntity.ok(versionGroup);
            return ResponseEntity.badRequest().body("Could not find a VersionGroup with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
