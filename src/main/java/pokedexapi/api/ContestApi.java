package pokedexapi.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controllers.BaseApiController;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.contesttype.ContestType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("/contest")
class ContestApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(ContestApi.class);

    ContestApi(PokemonService pokemonService, PokeApiClient client)
    {
        super(pokemonService, client);
    }

    // ContestType
    @GetMapping(value = "/contest-type")
    ResponseEntity<?> getAllContests()
    {
        LOGGER.info("getAllContests");
        try {
            NamedApiResourceList<ContestType> contests = pokeApiClient.getResource(skaro.pokeapi.resource.contesttype.ContestType.class).block();
            if (null != contests) return ResponseEntity.ok(contests.results());
            else return ResponseEntity.badRequest().body("Could not access ContestType endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/contest-type/{id}")
    ResponseEntity<?> getContestType(@PathVariable String id)
    {
        LOGGER.info("getContestType {}", id);
        try {
            ContestType contestType = pokeApiClient.getResource(ContestType.class, id).block();
            if (null != contestType) return ResponseEntity.ok(contestType);
            else return ResponseEntity.badRequest().body("Could not find a contestType with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // ContestEffect
    @GetMapping(value = "/contest-effect")
    ResponseEntity<?> getAllContestEffect()
    {
        LOGGER.info("getAllContestEffects");
        HttpResponse<String> response;
        JSONParser jsonParser;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(pokeApiBaseUrl + "contest-effect/"))
                    .GET()
                    .build();
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.debug("response: {}", response.body());
            jsonParser = new JSONParser(response.body());
            Map<String, Object> results = (Map<String, Object>) jsonParser.parse();
            if (null != results && !results.isEmpty()) return ResponseEntity.ok(results);
            else return ResponseEntity.badRequest().body("Could not access ContestEffect endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/contest-effect/{id}")
    ResponseEntity<?> getContestEffect(@PathVariable String id)
    {
        LOGGER.info("getContestEffect with {}", id);
        HttpResponse<String> response;
        JSONParser jsonParser;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(pokeApiBaseUrl + "contest-effect/" + id))
                    .GET()
                    .build();
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.info("response: {}", response.body());
            jsonParser = new JSONParser(response.body());
            Map<String, Object> results = (Map<String, Object>) jsonParser.parse();
            if (null != results && !results.isEmpty()) return ResponseEntity.ok(results);
            else return ResponseEntity.badRequest().body("Could not find a ContestEffect with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // SuperContestEffects
    @GetMapping(value = "/super-contest-effect")
    ResponseEntity<?> getAllSuperContestEffect()
    {
        LOGGER.info("getAllSuperContestEffects");
        HttpResponse<String> response;
        JSONParser jsonParser;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(pokeApiBaseUrl + "super-contest-effect/"))
                    .GET()
                    .build();
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.debug("response: {}", response.body());
            jsonParser = new JSONParser(response.body());
            Map<String, Object> results = (Map<String, Object>) jsonParser.parse();
            if (null != results && !results.isEmpty()) return ResponseEntity.ok(results);
            else return ResponseEntity.badRequest().body("Could not access SuperContestEffect endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/super-contest-effect/{id}")
    ResponseEntity<?> getSuperContestEffect(@PathVariable String id)
    {
        LOGGER.info("getSuperContestEffect with {}", id);
        HttpResponse<String> response;
        JSONParser jsonParser;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(pokeApiBaseUrl + "super-contest-effect/" + id))
                    .GET()
                    .build();
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            LOGGER.info("response: {}", response.body());
            jsonParser = new JSONParser(response.body());
            Map<String, Object> results = (Map<String, Object>) jsonParser.parse();
            if (null != results && !results.isEmpty()) return ResponseEntity.ok(results);
            else return ResponseEntity.badRequest().body("Could not find a SuperContestEffect with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
