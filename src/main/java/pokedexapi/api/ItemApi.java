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
import skaro.pokeapi.resource.item.Item;
import skaro.pokeapi.resource.itemattribute.ItemAttribute;
import skaro.pokeapi.resource.itemcategory.ItemCategory;
import skaro.pokeapi.resource.itemflingeffect.ItemFlingEffect;
import skaro.pokeapi.resource.itempocket.ItemPocket;

@RestController
@RequestMapping("/item")
class ItemApi extends BaseApiController
{
    private static final Logger LOGGER = LogManager.getLogger(ItemApi.class);

    ItemApi(PokeApiClient client)
    {
        super(client);
    }

    @Operation(summary = "Get all items", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "")
    ResponseEntity<?> getAllItems(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                  @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllItems limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<Item> items = pokeApiClient.getResource(Item.class, new PageQuery(limit, offset * limit)).block();
            if (items != null) return ResponseEntity.ok(items);
            return ResponseEntity.badRequest().body("Could not access Item endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get item", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/{id}")
    ResponseEntity<?> getItem(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getItem {}", id);
        try {
            Item item = pokeApiClient.getResource(Item.class, id).block();
            if (item != null) return ResponseEntity.ok(item);
            return ResponseEntity.badRequest().body("Could not find an item with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Attributes

    @Operation(summary = "Get all item attributes", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/item-attribute")
    ResponseEntity<?> getAllItemAttributes(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                           @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllItemAttributes limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<ItemAttribute> itemAttributes = pokeApiClient.getResource(ItemAttribute.class, new PageQuery(limit, offset * limit)).block();
            if (itemAttributes != null) return ResponseEntity.ok(itemAttributes);
            return ResponseEntity.badRequest().body("Could not access ItemAttribute endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get item attribute", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/item-attribute/{id}")
    ResponseEntity<?> getItemAttribute(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getItemAttribute {}", id);
        try {
            ItemAttribute itemAttribute = pokeApiClient.getResource(ItemAttribute.class, id).block();
            if (itemAttribute != null) return ResponseEntity.ok(itemAttribute);
            return ResponseEntity.badRequest().body("Could not find an itemAttribute with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Categories

    @Operation(summary = "Get all item categories", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/item-category")
    ResponseEntity<?> getAllItemCategories(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                           @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllItemCategories limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<ItemCategory> itemCategories = pokeApiClient.getResource(ItemCategory.class, new PageQuery(limit, offset * limit)).block();
            if (itemCategories != null) return ResponseEntity.ok(itemCategories);
            return ResponseEntity.badRequest().body("Could not access ItemCategory endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get item category", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/item-category/{id}")
    ResponseEntity<?> getItemCategory(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getItemCategory {}", id);
        try {
            ItemCategory itemCategory = pokeApiClient.getResource(ItemCategory.class, id).block();
            if (itemCategory != null) return ResponseEntity.ok(itemCategory);
            return ResponseEntity.badRequest().body("Could not find an itemCategory with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Fling Effects

    @Operation(summary = "Get all fling effects", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/item-fling-effect")
    ResponseEntity<?> getAllFlingEffects(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                         @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllFlingEffects limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<ItemFlingEffect> flingEffects = pokeApiClient.getResource(ItemFlingEffect.class, new PageQuery(limit, offset * limit)).block();
            if (flingEffects != null) return ResponseEntity.ok(flingEffects);
            return ResponseEntity.badRequest().body("Could not access ItemFlingEffect endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get fling effect", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/item-fling-effect/{id}")
    ResponseEntity<?> getFlingEffect(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getFlingEffect {}", id);
        try {
            ItemFlingEffect flingEffect = pokeApiClient.getResource(ItemFlingEffect.class, id).block();
            if (flingEffect != null) return ResponseEntity.ok(flingEffect);
            return ResponseEntity.badRequest().body("Could not find a flingEffect with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Pockets

    @Operation(summary = "Get all item pockets", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/item-pocket")
    ResponseEntity<?> getAllItemPockets(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                                        @RequestParam(value = "offset", required = false, defaultValue = "0") int offset)
    {
        LOGGER.info("getAllItemPockets limit:{} offset:{}", limit, offset);
        try {
            NamedApiResourceList<ItemPocket> itemPockets = pokeApiClient.getResource(ItemPocket.class, new PageQuery(limit, offset * limit)).block();
            if (itemPockets != null) return ResponseEntity.ok(itemPockets);
            return ResponseEntity.badRequest().body("Could not access ItemPocket endpoint");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Get item pocket", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)})
    @GetMapping(value = "/item-pocket/{id}")
    ResponseEntity<?> getItemPocket(@PathVariable(value = "id") String id)
    {
        LOGGER.info("getItemPocket {}", id);
        try {
            ItemPocket itemPocket = pokeApiClient.getResource(ItemPocket.class, id).block();
            if (itemPocket != null) return ResponseEntity.ok(itemPocket);
            return ResponseEntity.badRequest().body("Could not find a itemPocket with " + id);
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
