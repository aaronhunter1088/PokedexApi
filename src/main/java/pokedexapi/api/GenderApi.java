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
import skaro.pokeapi.resource.gender.Gender;


@RestController
@RequestMapping("/gender")
public class GenderApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(GenderApi.class);

    GenderApi(PokemonService pokemonService, PokeApiClient client)
    {
        super(pokemonService, client);
    }

    @GetMapping(value = "")
    ResponseEntity<?> getGenders(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getGenders limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Gender> page = pokeApiClient.getResource(Gender.class, new PageQuery(limit, offset)).block();
            if (null != page) return ResponseEntity.ok(page.results());
            else return ResponseEntity.badRequest().body("Could not access Gender endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> getGender(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getGender {}", id);
        try {
            Gender gender = pokeApiClient.getResource(Gender.class, id).block();
            if (null != gender) return ResponseEntity.ok(gender);
            else return ResponseEntity.badRequest().body("Could not find Gender with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
