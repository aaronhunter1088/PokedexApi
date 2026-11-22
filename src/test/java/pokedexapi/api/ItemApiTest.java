package pokedexapi.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pokedexapi.controllers.BaseApiTest;
import reactor.core.publisher.Mono;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.item.Item;
import skaro.pokeapi.resource.itemattribute.ItemAttribute;
import skaro.pokeapi.resource.itemcategory.ItemCategory;
import skaro.pokeapi.resource.itemflingeffect.ItemFlingEffect;
import skaro.pokeapi.resource.itempocket.ItemPocket;
import tools.jackson.core.type.TypeReference;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ItemApiTest extends BaseApiTest
{
    private static final String ITEM_API = "/item";
    private static final String ITEM_ATTRIBUTE_API = ITEM_API + "/item-attribute";
    private static final String ITEM_CATEGORY_API = ITEM_API + "/item-category";
    private static final String FLING_EFFECT_API = ITEM_API + "/item-fling-effect";
    private static final String ITEM_POCKET_API = ITEM_API + "/item-pocket";
    private static NamedApiResourceList<Item> getAllItemsResponse;
    private static NamedApiResourceList<Item> getNext10ItemsResponse;
    private static Item item_1;
    private static NamedApiResourceList<ItemAttribute> getAllItemAttributesResponse;
    private static NamedApiResourceList<ItemAttribute> getNextItemAttributesResponse;
    private static ItemAttribute itemAttribute_1;
    private static NamedApiResourceList<ItemCategory> getAllItemCategoriesResponse;
    private static NamedApiResourceList<ItemCategory> getNext10ItemCategoriesResponse;
    private static ItemCategory itemCategory_1;
    private static NamedApiResourceList<ItemFlingEffect> getAllFlingEffectsResponse;
    private static NamedApiResourceList<ItemFlingEffect> getNextFlingEffectsResponse;
    private static ItemFlingEffect flingEffect_1;
    private static NamedApiResourceList<ItemPocket> getAllItemPocketsResponse;
    private static NamedApiResourceList<ItemPocket> getNextItemPocketsResponse;
    private static ItemPocket itemPocket_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllItemsResponse = jsonMapper.readValue(
                new ClassPathResource("itemApi/getAllItemsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10ItemsResponse = jsonMapper.readValue(
                new ClassPathResource("itemApi/getNext10ItemsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        item_1 = jsonMapper.readValue(
                new ClassPathResource("itemApi/item_1.json").getFile(),
                Item.class
        );
        getAllItemAttributesResponse = jsonMapper.readValue(
                new ClassPathResource("itemApi/getAllItemAttributesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNextItemAttributesResponse = jsonMapper.readValue(
                new ClassPathResource("itemApi/getNextItemAttributesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        itemAttribute_1 = jsonMapper.readValue(
                new ClassPathResource("itemApi/item_attribute_1.json").getFile(),
                ItemAttribute.class
        );
        getAllItemCategoriesResponse = jsonMapper.readValue(
                new ClassPathResource("itemApi/getAllItemCategoriesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10ItemCategoriesResponse = jsonMapper.readValue(
                new ClassPathResource("itemApi/getNext10ItemCategoriesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        itemCategory_1 = jsonMapper.readValue(
                new ClassPathResource("itemApi/item_category_1.json").getFile(),
                ItemCategory.class
        );
        getAllFlingEffectsResponse = jsonMapper.readValue(
                new ClassPathResource("itemApi/getAllFlingEffectsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNextFlingEffectsResponse = jsonMapper.readValue(
                new ClassPathResource("itemApi/getNextFlingEffectsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        flingEffect_1 = jsonMapper.readValue(
                new ClassPathResource("itemApi/fling_effect_1.json").getFile(),
                ItemFlingEffect.class
        );
        getAllItemPocketsResponse = jsonMapper.readValue(
                new ClassPathResource("itemApi/getAllItemPocketsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNextItemPocketsResponse = jsonMapper.readValue(
                new ClassPathResource("itemApi/getNextItemPocketsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        itemPocket_1 = jsonMapper.readValue(
                new ClassPathResource("itemApi/item_pocket_1.json").getFile(),
                ItemPocket.class
        );
    }

    @Nested
    @DisplayName("Get All Items Positive Cases")
    class GetAllItemsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItems returns the first page")
        void testGetAllItems() throws Exception
        {
            when(pokeApiClient.getResource(Item.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllItemsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllItemsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Item.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItems returns the second page (offset is a page index)")
        void testGetAllItemsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Item.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10ItemsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10ItemsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Item.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Items Negative Cases")
    class GetAllItemsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItems returns no items")
        void testGetAllItemsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Item.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ITEM_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Item.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItems returns null items")
        void testGetAllItemsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Item.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(ITEM_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Item.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItems throws an exception")
        void testGetAllItemsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Item.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ITEM_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Item.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Item Positive Cases")
    class GetItemPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItem returns the 1st item")
        void testGetItem() throws Exception
        {
            when(pokeApiClient.getResource(Item.class, "1"))
                    .thenReturn(Mono.just(item_1));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(item_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Item.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Item Negative Cases")
    class GetItemNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItem returns no item")
        void testGetItemReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Item.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ITEM_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Item.class, "1");
        }

        @Test
        @DisplayName("getItem throws an exception")
        void testGetItemThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Item.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ITEM_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Item.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All ItemAttributes Positive Cases")
    class GetAllItemAttributesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemAttributes returns the first page")
        void testGetAllItemAttributes() throws Exception
        {
            when(pokeApiClient.getResource(ItemAttribute.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllItemAttributesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_ATTRIBUTE_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllItemAttributesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemAttribute.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItemAttributes returns the second page (offset is a page index)")
        void testGetAllItemAttributesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(ItemAttribute.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNextItemAttributesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_ATTRIBUTE_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNextItemAttributesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemAttribute.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All ItemAttributes Negative Cases")
    class GetAllItemAttributesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemAttributes returns no itemAttributes")
        void testGetAllItemAttributesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(ItemAttribute.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ITEM_ATTRIBUTE_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ItemAttribute.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItemAttributes returns null itemAttributes")
        void testGetAllItemAttributesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(ItemAttribute.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(ITEM_ATTRIBUTE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemAttribute.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItemAttributes throws an exception")
        void testGetAllItemAttributesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ItemAttribute.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ITEM_ATTRIBUTE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemAttribute.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get ItemAttribute Positive Cases")
    class GetItemAttributePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemAttribute returns the 1st itemAttribute")
        void testGetItemAttribute() throws Exception
        {
            when(pokeApiClient.getResource(ItemAttribute.class, "1"))
                    .thenReturn(Mono.just(itemAttribute_1));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_ATTRIBUTE_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(itemAttribute_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemAttribute.class, "1");
        }
    }

    @Nested
    @DisplayName("Get ItemAttribute Negative Cases")
    class GetItemAttributeNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemAttribute returns no itemAttribute")
        void testGetItemAttributeReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(ItemAttribute.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ITEM_ATTRIBUTE_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ItemAttribute.class, "1");
        }

        @Test
        @DisplayName("getItemAttribute throws an exception")
        void testGetItemAttributeThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ItemAttribute.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ITEM_ATTRIBUTE_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemAttribute.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All ItemCategories Positive Cases")
    class GetAllItemCategoriesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemCategories returns the first page")
        void testGetAllItemCategories() throws Exception
        {
            when(pokeApiClient.getResource(ItemCategory.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllItemCategoriesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_CATEGORY_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllItemCategoriesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemCategory.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItemCategories returns the second page (offset is a page index)")
        void testGetAllItemCategoriesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(ItemCategory.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10ItemCategoriesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_CATEGORY_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10ItemCategoriesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemCategory.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All ItemCategories Negative Cases")
    class GetAllItemCategoriesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemCategories returns no itemCategorys")
        void testGetAllItemCategoriesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(ItemCategory.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ITEM_CATEGORY_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ItemCategory.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItemCategories returns null itemCategorys")
        void testGetAllItemCategoriesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(ItemCategory.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(ITEM_CATEGORY_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemCategory.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItemCategories throws an exception")
        void testGetAllItemCategoriesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ItemCategory.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ITEM_CATEGORY_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemCategory.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get ItemCategory Positive Cases")
    class GetItemCategoryPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemCategory returns the 1st itemCategory")
        void testGetItemCategory() throws Exception
        {
            when(pokeApiClient.getResource(ItemCategory.class, "1"))
                    .thenReturn(Mono.just(itemCategory_1));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_CATEGORY_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(itemCategory_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemCategory.class, "1");
        }
    }

    @Nested
    @DisplayName("Get ItemCategory Negative Cases")
    class GetItemCategoryNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemCategory returns no itemCategory")
        void testGetItemCategoryReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(ItemCategory.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ITEM_CATEGORY_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ItemCategory.class, "1");
        }

        @Test
        @DisplayName("getItemCategory throws an exception")
        void testGetItemCategoryThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ItemCategory.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ITEM_CATEGORY_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemCategory.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All FlingEffects Positive Cases")
    class GetAllFlingEffectsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getFlingEffects returns the first page")
        void testGetAllFlingEffects() throws Exception
        {
            when(pokeApiClient.getResource(ItemFlingEffect.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllFlingEffectsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(FLING_EFFECT_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllFlingEffectsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemFlingEffect.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getFlingEffects returns the second page (offset is a page index)")
        void testGetAllFlingEffectsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(ItemFlingEffect.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNextFlingEffectsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(FLING_EFFECT_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNextFlingEffectsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemFlingEffect.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All FlingEffects Negative Cases")
    class GetAllFlingEffectsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getFlingEffects returns no flingEffects")
        void testGetAllFlingEffectsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(ItemFlingEffect.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(FLING_EFFECT_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ItemFlingEffect.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getFlingEffects returns null flingEffects")
        void testGetAllFlingEffectsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(ItemFlingEffect.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(FLING_EFFECT_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemFlingEffect.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getFlingEffects throws an exception")
        void testGetAllFlingEffectsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ItemFlingEffect.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(FLING_EFFECT_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemFlingEffect.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get FlingEffect Positive Cases")
    class GetFlingEffectPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getFlingEffect returns the 1st flingEffect")
        void testGetFlingEffect() throws Exception
        {
            when(pokeApiClient.getResource(ItemFlingEffect.class, "1"))
                    .thenReturn(Mono.just(flingEffect_1));

            MvcResult mvcResult = this.mockMvc.perform(get(FLING_EFFECT_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(flingEffect_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemFlingEffect.class, "1");
        }
    }

    @Nested
    @DisplayName("Get FlingEffect Negative Cases")
    class GetFlingEffectNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getFlingEffect returns no flingEffect")
        void testGetFlingEffectReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(ItemFlingEffect.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(FLING_EFFECT_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ItemFlingEffect.class, "1");
        }

        @Test
        @DisplayName("getFlingEffect throws an exception")
        void testGetFlingEffectThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ItemFlingEffect.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(FLING_EFFECT_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemFlingEffect.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All ItemPockets Positive Cases")
    class GetAllItemPocketsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemPockets returns the first page")
        void testGetAllItemPockets() throws Exception
        {
            when(pokeApiClient.getResource(ItemPocket.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllItemPocketsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_POCKET_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllItemPocketsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemPocket.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItemPockets returns the second page (offset is a page index)")
        void testGetAllItemPocketsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(ItemPocket.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNextItemPocketsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_POCKET_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNextItemPocketsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemPocket.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All ItemPockets Negative Cases")
    class GetAllItemPocketsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemPockets returns no itemPockets")
        void testGetAllItemPocketsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(ItemPocket.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ITEM_POCKET_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ItemPocket.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItemPockets returns null itemPockets")
        void testGetAllItemPocketsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(ItemPocket.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(ITEM_POCKET_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemPocket.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getItemPockets throws an exception")
        void testGetAllItemPocketsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ItemPocket.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ITEM_POCKET_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemPocket.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get ItemPocket Positive Cases")
    class GetItemPocketPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemPocket returns the 1st itemPocket")
        void testGetItemPocket() throws Exception
        {
            when(pokeApiClient.getResource(ItemPocket.class, "1"))
                    .thenReturn(Mono.just(itemPocket_1));

            MvcResult mvcResult = this.mockMvc.perform(get(ITEM_POCKET_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(itemPocket_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(ItemPocket.class, "1");
        }
    }

    @Nested
    @DisplayName("Get ItemPocket Negative Cases")
    class GetItemPocketNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getItemPocket returns no itemPocket")
        void testGetItemPocketReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(ItemPocket.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(ITEM_POCKET_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(ItemPocket.class, "1");
        }

        @Test
        @DisplayName("getItemPocket throws an exception")
        void testGetItemPocketThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(ItemPocket.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(ITEM_POCKET_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(ItemPocket.class, "1");
        }
    }
}
