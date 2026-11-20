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
import skaro.pokeapi.resource.machine.Machine;


@RestController
@RequestMapping("/machine")
class MachineApi extends BaseApiController
{
    private static final Logger logger = LogManager.getLogger(MachineApi.class);

    MachineApi(PokemonService pokemonService, PokeApiClient client)
    {
        super(pokemonService, client);
    }

    @GetMapping(value = "")
    ResponseEntity<?> getMachines()
    {
        logger.info("getMachines");
        try {
            NamedApiResourceList<Machine> page = pokeApiClient.getResource(Machine.class, new PageQuery(20, 0)).block();
            if (null != page) return ResponseEntity.ok(page.results());
            else return ResponseEntity.badRequest().body("Could not access Machine endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> getMachine(@PathVariable(value = "id") String id)
    {
        logger.info("getMachine {}", id);
        try {
            Machine machine = pokeApiClient.getResource(Machine.class, id).block();
            if (null != machine) return ResponseEntity.ok(machine);
            else return ResponseEntity.badRequest().body("Could not find Machine with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
