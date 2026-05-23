package pokedexapi.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pokedexapi.service.PokemonApiService;
import reactor.core.publisher.Mono;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.PokeApiResource;
import skaro.pokeapi.resource.item.Item;
import skaro.pokeapi.resource.itemattribute.ItemAttribute;
import skaro.pokeapi.resource.itemcategory.ItemCategory;
import skaro.pokeapi.resource.itemflingeffect.ItemFlingEffect;
import skaro.pokeapi.resource.itempocket.ItemPocket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ItemApiTest
{
    @Autowired
    private ItemApi itemApi;

    @MockitoBean
    private PokeApiClient pokeApiClient;

    @MockitoBean
    private PokemonApiService pokemonApiService;

    @SuppressWarnings("unchecked")
    private <T extends PokeApiResource> NamedApiResourceList<T> mockNamedApiResourceList()
    {
        return (NamedApiResourceList<T>) mock(NamedApiResourceList.class);
    }

    @Test
    @DisplayName("getItems returns ok")
    void testGetItems()
    {
        NamedApiResourceList<Item> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(Item.class)).thenReturn(Mono.just(response));

        var result = itemApi.getItems();

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getItem returns ok")
    void testGetItem()
    {
        Item response = mock(Item.class);
        when(pokeApiClient.getResource(Item.class, "1")).thenReturn(Mono.just(response));

        var result = itemApi.getItem("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getItemAttributes returns ok")
    void testGetItemAttributes()
    {
        NamedApiResourceList<ItemAttribute> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(ItemAttribute.class)).thenReturn(Mono.just(response));

        var result = itemApi.getItemAttributes();

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getItemAttribute returns ok")
    void testGetItemAttribute()
    {
        ItemAttribute response = mock(ItemAttribute.class);
        when(pokeApiClient.getResource(ItemAttribute.class, "1")).thenReturn(Mono.just(response));

        var result = itemApi.getItemAttribute("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getItemCategories returns ok")
    void testGetItemCategories()
    {
        NamedApiResourceList<ItemCategory> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(ItemCategory.class)).thenReturn(Mono.just(response));

        var result = itemApi.getItemCategories();

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getItemCategory returns ok")
    void testGetItemCategory()
    {
        ItemCategory response = mock(ItemCategory.class);
        when(pokeApiClient.getResource(ItemCategory.class, "1")).thenReturn(Mono.just(response));

        var result = itemApi.getItemCategory("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getFlingEffects returns ok")
    void testGetFlingEffects()
    {
        NamedApiResourceList<ItemFlingEffect> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(ItemFlingEffect.class)).thenReturn(Mono.just(response));

        var result = itemApi.getFlingEffects();

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getFlingEffect returns ok")
    void testGetFlingEffect()
    {
        ItemFlingEffect response = mock(ItemFlingEffect.class);
        when(pokeApiClient.getResource(ItemFlingEffect.class, "1")).thenReturn(Mono.just(response));

        var result = itemApi.getFlingEffect("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getItemPockets returns ok")
    void testGetItemPockets()
    {
        NamedApiResourceList<ItemPocket> response = mockNamedApiResourceList();
        when(pokeApiClient.getResource(ItemPocket.class)).thenReturn(Mono.just(response));

        var result = itemApi.getItemPockets();

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }

    @Test
    @DisplayName("getItemPocket returns ok")
    void testGetItemPocket()
    {
        ItemPocket response = mock(ItemPocket.class);
        when(pokeApiClient.getResource(ItemPocket.class, "1")).thenReturn(Mono.just(response));

        var result = itemApi.getItemPocket("1");

        assertThat(result.getStatusCode().value()).isEqualTo(200);
        assertThat(result.getBody()).isSameAs(response);
    }
}

