package pokedexapi.api;

import pokedexapi.controller.BaseController;
import pokedexapi.service.PokemonService;
import tools.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.item.Item;
import skaro.pokeapi.resource.itemattribute.ItemAttribute;
import skaro.pokeapi.resource.itemcategory.ItemCategory;
import skaro.pokeapi.resource.itemflingeffect.ItemFlingEffect;
import skaro.pokeapi.resource.itempocket.ItemPocket;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/item")
class ItemApi extends BaseController
{
    /* Logging instance */
    private static final Logger LOGGER = LogManager.getLogger(ItemApi.class);

    @Autowired
    ItemApi(PokemonService pokemonService, PokeApiClient client, ObjectMapper objectMapper)
    {
        super(pokemonService, client, objectMapper);
    }

    @GetMapping(value="")
    @ResponseBody
    ResponseEntity<?> getItems()
    {
        LOGGER.info("getItems");
        try {
            NamedApiResourceList<Item> items = pokeApiClient.getResource(Item.class).block();
            if (null != items) return ResponseEntity.ok(items);
            else return ResponseEntity.badRequest().body("Could not access Item endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/item/{id}")
    ResponseEntity<?> getItem(@PathVariable(value="id") String id)
    {
        LOGGER.info("getItem {}", id);
        try {
            Item item = pokeApiClient.getResource(Item.class, id).block();
            if (null != item) return ResponseEntity.ok(item);
            else return ResponseEntity.badRequest().body("Could not find an item with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Attributes
    @GetMapping(value="/item-attribute")
    @ResponseBody
    ResponseEntity<?> getItemAttributes()
    {
        LOGGER.info("getItemAttributes");
        try {
            NamedApiResourceList<ItemAttribute> itemAttributes = pokeApiClient.getResource(ItemAttribute.class).block();
            if (null != itemAttributes) return ResponseEntity.ok(itemAttributes);
            else return ResponseEntity.badRequest().body("Could not access ItemAttribute endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/item-attribute/{id}")
    ResponseEntity<?> getItemAttribute(@PathVariable(value="id") String id)
    {
        LOGGER.info("getItemAttribute {}", id);
        try {
            ItemAttribute itemAttribute = pokeApiClient.getResource(ItemAttribute.class, id).block();
            if (null != itemAttribute) return ResponseEntity.ok(itemAttribute);
            else return ResponseEntity.badRequest().body("Could not find an itemAttribute with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Categories
    @GetMapping(value="/item-category")
    @ResponseBody
    ResponseEntity<?> getItemCategories()
    {
        LOGGER.info("getItemCategories");
        try {
            NamedApiResourceList<ItemCategory> itemCategories = pokeApiClient.getResource(ItemCategory.class).block();
            if (null != itemCategories) return ResponseEntity.ok(itemCategories);
            else return ResponseEntity.badRequest().body("Could not access ItemCategory endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/item-category/{id}")
    ResponseEntity<?> getItemCategory(@PathVariable(value="id") String id)
    {
        LOGGER.info("getItemCategory {}", id);
        try {
            ItemCategory itemCategory = pokeApiClient.getResource(ItemCategory.class, id).block();
            if (null != itemCategory) return ResponseEntity.ok(itemCategory);
            else return ResponseEntity.badRequest().body("Could not find an itemCategory with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Fling Effects
    @GetMapping(value="/item-fling-effect")
    @ResponseBody
    ResponseEntity<?> getFlingEffects()
    {
        LOGGER.info("getFlingEffects");
        try {
            NamedApiResourceList<ItemFlingEffect> flingEffects = pokeApiClient.getResource(ItemFlingEffect.class).block();
            if (null != flingEffects) return ResponseEntity.ok(flingEffects);
            else return ResponseEntity.badRequest().body("Could not access ItemFlingEffect endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/item-fling-effect/{id}")
    ResponseEntity<?> getFlingEffect(@PathVariable(value="id") String id)
    {
        LOGGER.info("getFlingEffect {}", id);
        try {
            ItemFlingEffect flingEffect = pokeApiClient.getResource(ItemFlingEffect.class, id).block();
            if (null != flingEffect) return ResponseEntity.ok(flingEffect);
            else return ResponseEntity.badRequest().body("Could not find a flingEffect with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Pockets
    @GetMapping(value="/item-pocket")
    @ResponseBody
    ResponseEntity<?> getItemPockets()
    {
        LOGGER.info("getItemPockets");
        try {
            NamedApiResourceList<ItemPocket> itemPocket = pokeApiClient.getResource(ItemPocket.class).block();
            if (null != itemPocket) return ResponseEntity.ok(itemPocket);
            else return ResponseEntity.badRequest().body("Could not access ItemPocket endpoint");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value="/item-pocket/{id}")
    ResponseEntity<?> getItemPocket(@PathVariable(value="id") String id)
    {
        LOGGER.info("getItemPocket {}", id);
        try {
            ItemPocket itemPocket = pokeApiClient.getResource(ItemPocket.class, id).block();
            if (null != itemPocket) return ResponseEntity.ok(itemPocket);
            else return ResponseEntity.badRequest().body("Could not find a itemPocket with " + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
