package pokedexapi.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pokedexapi.controller.BaseController;
import pokedexapi.service.PokemonService;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.ability.Ability;
import tools.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ability")
public class AbilityApi extends BaseController
{
    /* Logging instance */
    private static final Logger LOGGER = LogManager.getLogger(AbilityApi.class);

    @Autowired
    AbilityApi(PokemonService pokemonService, PokeApiClient client, ObjectMapper objectMapper)
    {
        super(pokemonService, client, objectMapper);
    }

    @GetMapping(value = "")
    public ResponseEntity<?> getAllAbilities()
    {
        LOGGER.info("getAllAbilities");
        try {
            NamedApiResourceList<Ability> abilities = pokeApiClient.getResource(Ability.class).block();
            if (null != abilities) return ResponseEntity.ok(abilities);
            else return ResponseEntity.badRequest().body("Could not access Ability endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<?> getAbility(@PathVariable(value="id") String id)
    {
        LOGGER.info("getAbility {}", id);
        try {
            Ability ability = pokeApiClient.getResource(Ability.class, id).block();
            if (null != ability) return ResponseEntity.ok(ability);
            else return ResponseEntity.badRequest().body("Could not find Ability with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
