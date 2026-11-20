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
import skaro.pokeapi.resource.characteristic.Characteristic;


@RestController
@RequestMapping("/characteristic")
public class CharacteristicApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(CharacteristicApi.class);

    CharacteristicApi(PokemonService pokemonService, PokeApiClient client)
    {
        super(pokemonService, client);
    }

    @GetMapping(value = "")
    ResponseEntity<?> getCharacteristics(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getCharacteristics limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Characteristic> page = pokeApiClient.getResource(Characteristic.class, new PageQuery(limit, offset)).block();
            if (null != page) return ResponseEntity.ok(page.results());
            else return ResponseEntity.badRequest().body("Could not access Characteristic endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> getCharacteristic(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getCharacteristic {}", id);
        try {
            Characteristic characteristic = pokeApiClient.getResource(Characteristic.class, id).block();
            if (null != characteristic) return ResponseEntity.ok(characteristic);
            else return ResponseEntity.badRequest().body("Could not find Characteristic with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
