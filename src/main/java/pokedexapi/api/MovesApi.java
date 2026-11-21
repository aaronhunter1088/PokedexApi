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
import skaro.pokeapi.resource.move.Move;
import skaro.pokeapi.resource.moveailment.MoveAilment;
import skaro.pokeapi.resource.movebattlestyle.MoveBattleStyle;
import skaro.pokeapi.resource.movecategory.MoveCategory;
import skaro.pokeapi.resource.movedamageclass.MoveDamageClass;
import skaro.pokeapi.resource.movelearnmethod.MoveLearnMethod;
import skaro.pokeapi.resource.movetarget.MoveTarget;

@RestController
@RequestMapping("/move")
class MovesApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(MovesApi.class);

    MovesApi(PokeApiClient client)
    {
        super(client);
    }

    // Moves

    @GetMapping(value = "")
    ResponseEntity<?> getAllMoves(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                  @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllMoves limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Move> moves = pokeApiClient.getResource(Move.class, new PageQuery(limit, offset * limit)).block();
            if (moves != null ) return ResponseEntity.ok(moves);
            return ResponseEntity.badRequest().body("Could not access Move endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> getMove(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getMove {}", id);
        try {
            Move move = pokeApiClient.getResource(Move.class, id).block();
            if (move != null) return ResponseEntity.ok(move);
            return ResponseEntity.badRequest().body("Could not find a Move with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Move Ailment

    @GetMapping(value = "/move-ailment")
    ResponseEntity<?> getAllMoveAilments(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                         @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllMoveAilments limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<MoveAilment> moveAilments = pokeApiClient.getResource(MoveAilment.class, new PageQuery(limit, offset * limit)).block();
            if (moveAilments != null) return ResponseEntity.ok(moveAilments);
            return ResponseEntity.badRequest().body("Could not access MoveAilment endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/move-ailment/{id}")
    ResponseEntity<?> getMoveAilment(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getMoveAilment {}", id);
        try {
            MoveAilment moveAilment = pokeApiClient.getResource(MoveAilment.class, id).block();
            if (moveAilment != null) return ResponseEntity.ok(moveAilment);
            return ResponseEntity.badRequest().body("Could not find a MoveAilment with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Battle Styles

    @GetMapping(value = "/move-battle-style")
    ResponseEntity<?> getAllMoveBattleStyles(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                             @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllMoveBattleStyles limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<MoveBattleStyle> moveBattleStyles = pokeApiClient.getResource(MoveBattleStyle.class, new PageQuery(limit, offset * limit)).block();
            if (moveBattleStyles != null) return ResponseEntity.ok(moveBattleStyles);
            return ResponseEntity.badRequest().body("Could not access MoveBattleStyle endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/move-battle-style/{id}")
    ResponseEntity<?> getMoveBattleStyle(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getMoveBattleStyle {}", id);
        try {
            MoveBattleStyle battleStyle = pokeApiClient.getResource(MoveBattleStyle.class, id).block();
            if (battleStyle != null) return ResponseEntity.ok(battleStyle);
            return ResponseEntity.badRequest().body("Could not find a MoveBattleStyle with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Categories

    @GetMapping(value = "/move-category")
    ResponseEntity<?> getAllMoveCategories(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                           @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllMoveCategories limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<MoveCategory> moveCategories = pokeApiClient.getResource(MoveCategory.class, new PageQuery(limit, offset * limit)).block();
            if (moveCategories != null) return ResponseEntity.ok(moveCategories);
            return ResponseEntity.badRequest().body("Could not access MoveCategory endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/move-category/{id}")
    ResponseEntity<?> getMoveCategory(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getMoveCategory {}", id);
        try {
            MoveCategory moveCategory = pokeApiClient.getResource(MoveCategory.class, id).block();
            if (moveCategory != null) return ResponseEntity.ok(moveCategory);
            return ResponseEntity.badRequest().body("Could not find a MoveCategory with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Damage Classes

    @GetMapping(value = "/move-damage-class")
    ResponseEntity<?> getAllMoveDamageClasses(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                              @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllMoveDamageClasses limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<MoveDamageClass> moveDamages = pokeApiClient.getResource(MoveDamageClass.class, new PageQuery(limit, offset * limit)).block();
            if (moveDamages != null) return ResponseEntity.ok(moveDamages);
            return ResponseEntity.badRequest().body("Could not access MoveDamageClass endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/move-damage-class/{id}")
    ResponseEntity<?> getMoveDamageClass(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getMoveDamageClass {}", id);
        try {
            MoveDamageClass moveDamageClass = pokeApiClient.getResource(MoveDamageClass.class, id).block();
            if (moveDamageClass != null) return ResponseEntity.ok(moveDamageClass);
            return ResponseEntity.badRequest().body("Could not find a MoveDamageClass with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Learn Moves

    @GetMapping(value = "/move-learn-method")
    ResponseEntity<?> getAllMoveLearnMethods(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                             @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllMoveLearnMethods limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<MoveLearnMethod> learnMoves = pokeApiClient.getResource(MoveLearnMethod.class, new PageQuery(limit, offset * limit)).block();
            if (learnMoves != null) return ResponseEntity.ok(learnMoves);
            return ResponseEntity.badRequest().body("Could not access MoveLearnMethod endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/move-learn-method/{id}")
    ResponseEntity<?> getMoveLearnMethod(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getMoveLearnMethod {}", id);
        try {
            MoveLearnMethod moveLearnMethod = pokeApiClient.getResource(MoveLearnMethod.class, id).block();
            if (moveLearnMethod != null) return ResponseEntity.ok(moveLearnMethod);
            return ResponseEntity.badRequest().body("Could not find a MoveLearnMethod with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Targets

    @GetMapping(value = "/move-target")
    ResponseEntity<?> getAllMoveTargets(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                        @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllMoveTargets limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<MoveTarget> learnMoves = pokeApiClient.getResource(MoveTarget.class, new PageQuery(limit, offset * limit)).block();
            if (learnMoves != null) return ResponseEntity.ok(learnMoves);
            return ResponseEntity.badRequest().body("Could not access MoveTarget endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/move-target/{id}")
    ResponseEntity<?> getMoveTarget(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getMoveTarget {}", id);
        try {
            MoveTarget moveTarget = pokeApiClient.getResource(MoveTarget.class, id).block();
            if (moveTarget != null) return ResponseEntity.ok(moveTarget);
            return ResponseEntity.badRequest().body("Could not find a MoveTarget with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
