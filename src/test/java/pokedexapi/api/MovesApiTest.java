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
import skaro.pokeapi.resource.move.Move;
import skaro.pokeapi.resource.moveailment.MoveAilment;
import skaro.pokeapi.resource.movebattlestyle.MoveBattleStyle;
import skaro.pokeapi.resource.movecategory.MoveCategory;
import skaro.pokeapi.resource.movedamageclass.MoveDamageClass;
import skaro.pokeapi.resource.movelearnmethod.MoveLearnMethod;
import skaro.pokeapi.resource.movetarget.MoveTarget;
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
class MovesApiTest extends BaseApiTest
{
    private static final String MOVE_API = "/move";
    private static final String MOVE_AILMENT_API = MOVE_API + "/move-ailment";
    private static final String MOVE_BATTLE_STYLE_API = MOVE_API + "/move-battle-style";
    private static final String MOVE_CATEGORY_API = MOVE_API + "/move-category";
    private static final String MOVE_DAMAGE_CLASS_API = MOVE_API + "/move-damage-class";
    private static final String MOVE_LEARN_METHOD_API = MOVE_API + "/move-learn-method";
    private static final String MOVE_TARGET_API = MOVE_API + "/move-target";
    private static NamedApiResourceList<Move> getAllMovesResponse;
    private static NamedApiResourceList<Move> getNext10MovesResponse;
    private static NamedApiResourceList<MoveAilment> getAllMoveAilmentsResponse;
    private static NamedApiResourceList<MoveAilment> getNext10MoveAilmentsResponse;
    private static NamedApiResourceList<MoveBattleStyle> getAllMoveBattleStylesResponse;
    private static NamedApiResourceList<MoveBattleStyle> getNextMoveBattleStylesResponse;
    private static NamedApiResourceList<MoveCategory> getAllMoveCategoriesResponse;
    private static NamedApiResourceList<MoveCategory> getNext10MoveCategoriesResponse;
    private static NamedApiResourceList<MoveDamageClass> getAllMoveDamageClassesResponse;
    private static NamedApiResourceList<MoveDamageClass> getNextMoveDamageClassesResponse;
    private static NamedApiResourceList<MoveLearnMethod> getAllMoveLearnMethodsResponse;
    private static NamedApiResourceList<MoveLearnMethod> getNext10MoveLearnMethodsResponse;
    private static NamedApiResourceList<MoveTarget> getAllMoveTargetsResponse;
    private static NamedApiResourceList<MoveTarget> getNext10MoveTargetsResponse;
    private static Move move_1;
    private static MoveAilment moveAilment_1;
    private static MoveBattleStyle moveBattleStyle_1;
    private static MoveCategory moveCategory_1;
    private static MoveDamageClass moveDamageClass_1;
    private static MoveLearnMethod moveLearnMethod_1;
    private static MoveTarget moveTarget_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllMovesResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getAllMovesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10MovesResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getNext10MovesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        move_1 = jsonMapper.readValue(
                new ClassPathResource("moveApi/move_1.json").getFile(),
                Move.class
        );
        getAllMoveAilmentsResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getAllMoveAilmentsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10MoveAilmentsResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getNext10MoveAilmentsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        moveAilment_1 = jsonMapper.readValue(
                new ClassPathResource("moveApi/move_ailment_1.json").getFile(),
                MoveAilment.class
        );
        getAllMoveBattleStylesResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getAllMoveBattleStylesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNextMoveBattleStylesResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getNextMoveBattleStylesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        moveBattleStyle_1 = jsonMapper.readValue(
                new ClassPathResource("moveApi/move_battle_style_1.json").getFile(),
                MoveBattleStyle.class
        );
        getAllMoveCategoriesResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getAllMoveCategoriesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10MoveCategoriesResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getNext10MoveCategoriesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        moveCategory_1 = jsonMapper.readValue(
                new ClassPathResource("moveApi/move_category_1.json").getFile(),
                MoveCategory.class
        );
        getAllMoveDamageClassesResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getAllMoveDamageClassesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNextMoveDamageClassesResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getNextMoveDamageClassesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        moveDamageClass_1 = jsonMapper.readValue(
                new ClassPathResource("moveApi/move_damage_class_1.json").getFile(),
                MoveDamageClass.class
        );
        getAllMoveLearnMethodsResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getAllMoveLearnMethodsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10MoveLearnMethodsResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getNext10MoveLearnMethodsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        moveLearnMethod_1 = jsonMapper.readValue(
                new ClassPathResource("moveApi/move_learn_method_1.json").getFile(),
                MoveLearnMethod.class
        );
        getAllMoveTargetsResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getAllMoveTargetsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10MoveTargetsResponse = jsonMapper.readValue(
                new ClassPathResource("moveApi/getNext10MoveTargetsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        moveTarget_1 = jsonMapper.readValue(
                new ClassPathResource("moveApi/move_target_1.json").getFile(),
                MoveTarget.class
        );
    }

    @Nested
    @DisplayName("Get All Moves Positive Cases")
    class GetAllMovesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoves returns the first page")
        void testGetAllMoves() throws Exception
        {
            when(pokeApiClient.getResource(Move.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllMovesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllMovesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Move.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoves returns the second page (offset is a page index)")
        void testGetAllMovesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Move.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10MovesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10MovesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Move.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Moves Negative Cases")
    class GetAllMovesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoves returns no moves")
        void testGetAllMovesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Move.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Move.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoves returns null moves")
        void testGetAllMovesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Move.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(MOVE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Move.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoves throws an exception")
        void testGetAllMovesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Move.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Move.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Move Positive Cases")
    class GetMovePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMove returns the 1st move")
        void testGetMove() throws Exception
        {
            when(pokeApiClient.getResource(Move.class, "1"))
                    .thenReturn(Mono.just(move_1));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(move_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Move.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Move Negative Cases")
    class GetMoveNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMove returns no move")
        void testGetMoveReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Move.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Move.class, "1");
        }

        @Test
        @DisplayName("getMove throws an exception")
        void testGetMoveThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Move.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Move.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All MoveAilments Positive Cases")
    class GetAllMoveAilmentsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveAilments returns the first page")
        void testGetAllMoveAilments() throws Exception
        {
            when(pokeApiClient.getResource(MoveAilment.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllMoveAilmentsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_AILMENT_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllMoveAilmentsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveAilment.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveAilments returns the second page (offset is a page index)")
        void testGetAllMoveAilmentsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(MoveAilment.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10MoveAilmentsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_AILMENT_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10MoveAilmentsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveAilment.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All MoveAilments Negative Cases")
    class GetAllMoveAilmentsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveAilments returns no moveAilments")
        void testGetAllMoveAilmentsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(MoveAilment.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_AILMENT_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveAilment.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveAilments returns null moveAilments")
        void testGetAllMoveAilmentsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(MoveAilment.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(MOVE_AILMENT_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveAilment.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveAilments throws an exception")
        void testGetAllMoveAilmentsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveAilment.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_AILMENT_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveAilment.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get MoveAilment Positive Cases")
    class GetMoveAilmentPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveAilment returns the 1st moveAilment")
        void testGetMoveAilment() throws Exception
        {
            when(pokeApiClient.getResource(MoveAilment.class, "1"))
                    .thenReturn(Mono.just(moveAilment_1));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_AILMENT_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(moveAilment_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveAilment.class, "1");
        }
    }

    @Nested
    @DisplayName("Get MoveAilment Negative Cases")
    class GetMoveAilmentNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveAilment returns no moveAilment")
        void testGetMoveAilmentReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(MoveAilment.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_AILMENT_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveAilment.class, "1");
        }

        @Test
        @DisplayName("getMoveAilment throws an exception")
        void testGetMoveAilmentThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveAilment.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_AILMENT_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveAilment.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All MoveBattleStyles Positive Cases")
    class GetAllMoveBattleStylesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveBattleStyles returns the first page")
        void testGetAllMoveBattleStyles() throws Exception
        {
            when(pokeApiClient.getResource(MoveBattleStyle.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllMoveBattleStylesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_BATTLE_STYLE_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllMoveBattleStylesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveBattleStyle.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveBattleStyles returns the second page (offset is a page index)")
        void testGetAllMoveBattleStylesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(MoveBattleStyle.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNextMoveBattleStylesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_BATTLE_STYLE_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNextMoveBattleStylesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveBattleStyle.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All MoveBattleStyles Negative Cases")
    class GetAllMoveBattleStylesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveBattleStyles returns no moveBattleStyles")
        void testGetAllMoveBattleStylesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(MoveBattleStyle.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_BATTLE_STYLE_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveBattleStyle.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveBattleStyles returns null moveBattleStyles")
        void testGetAllMoveBattleStylesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(MoveBattleStyle.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(MOVE_BATTLE_STYLE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveBattleStyle.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveBattleStyles throws an exception")
        void testGetAllMoveBattleStylesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveBattleStyle.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_BATTLE_STYLE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveBattleStyle.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get MoveBattleStyle Positive Cases")
    class GetMoveBattleStylePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveBattleStyle returns the 1st moveBattleStyle")
        void testGetMoveBattleStyle() throws Exception
        {
            when(pokeApiClient.getResource(MoveBattleStyle.class, "1"))
                    .thenReturn(Mono.just(moveBattleStyle_1));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_BATTLE_STYLE_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(moveBattleStyle_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveBattleStyle.class, "1");
        }
    }

    @Nested
    @DisplayName("Get MoveBattleStyle Negative Cases")
    class GetMoveBattleStyleNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveBattleStyle returns no moveBattleStyle")
        void testGetMoveBattleStyleReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(MoveBattleStyle.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_BATTLE_STYLE_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveBattleStyle.class, "1");
        }

        @Test
        @DisplayName("getMoveBattleStyle throws an exception")
        void testGetMoveBattleStyleThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveBattleStyle.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_BATTLE_STYLE_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveBattleStyle.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All MoveCategories Positive Cases")
    class GetAllMoveCategoriesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveCategories returns the first page")
        void testGetAllMoveCategories() throws Exception
        {
            when(pokeApiClient.getResource(MoveCategory.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllMoveCategoriesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_CATEGORY_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllMoveCategoriesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveCategory.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveCategories returns the second page (offset is a page index)")
        void testGetAllMoveCategoriesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(MoveCategory.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10MoveCategoriesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_CATEGORY_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10MoveCategoriesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveCategory.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All MoveCategories Negative Cases")
    class GetAllMoveCategoriesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveCategories returns no moveCategorys")
        void testGetAllMoveCategoriesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(MoveCategory.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_CATEGORY_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveCategory.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveCategories returns null moveCategorys")
        void testGetAllMoveCategoriesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(MoveCategory.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(MOVE_CATEGORY_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveCategory.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveCategories throws an exception")
        void testGetAllMoveCategoriesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveCategory.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_CATEGORY_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveCategory.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get MoveCategory Positive Cases")
    class GetMoveCategoryPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveCategory returns the 1st moveCategory")
        void testGetMoveCategory() throws Exception
        {
            when(pokeApiClient.getResource(MoveCategory.class, "1"))
                    .thenReturn(Mono.just(moveCategory_1));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_CATEGORY_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(moveCategory_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveCategory.class, "1");
        }
    }

    @Nested
    @DisplayName("Get MoveCategory Negative Cases")
    class GetMoveCategoryNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveCategory returns no moveCategory")
        void testGetMoveCategoryReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(MoveCategory.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_CATEGORY_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveCategory.class, "1");
        }

        @Test
        @DisplayName("getMoveCategory throws an exception")
        void testGetMoveCategoryThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveCategory.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_CATEGORY_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveCategory.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All MoveDamageClasses Positive Cases")
    class GetAllMoveDamageClassesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveDamageClasses returns the first page")
        void testGetAllMoveDamageClasses() throws Exception
        {
            when(pokeApiClient.getResource(MoveDamageClass.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllMoveDamageClassesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_DAMAGE_CLASS_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllMoveDamageClassesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveDamageClass.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveDamageClasses returns the second page (offset is a page index)")
        void testGetAllMoveDamageClassesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(MoveDamageClass.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNextMoveDamageClassesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_DAMAGE_CLASS_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNextMoveDamageClassesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveDamageClass.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All MoveDamageClasses Negative Cases")
    class GetAllMoveDamageClassesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveDamageClasses returns no moveDamageClasss")
        void testGetAllMoveDamageClassesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(MoveDamageClass.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_DAMAGE_CLASS_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveDamageClass.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveDamageClasses returns null moveDamageClasss")
        void testGetAllMoveDamageClassesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(MoveDamageClass.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(MOVE_DAMAGE_CLASS_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveDamageClass.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveDamageClasses throws an exception")
        void testGetAllMoveDamageClassesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveDamageClass.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_DAMAGE_CLASS_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveDamageClass.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get MoveDamageClass Positive Cases")
    class GetMoveDamageClassPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveDamageClass returns the 1st moveDamageClass")
        void testGetMoveDamageClass() throws Exception
        {
            when(pokeApiClient.getResource(MoveDamageClass.class, "1"))
                    .thenReturn(Mono.just(moveDamageClass_1));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_DAMAGE_CLASS_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(moveDamageClass_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveDamageClass.class, "1");
        }
    }

    @Nested
    @DisplayName("Get MoveDamageClass Negative Cases")
    class GetMoveDamageClassNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveDamageClass returns no moveDamageClass")
        void testGetMoveDamageClassReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(MoveDamageClass.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_DAMAGE_CLASS_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveDamageClass.class, "1");
        }

        @Test
        @DisplayName("getMoveDamageClass throws an exception")
        void testGetMoveDamageClassThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveDamageClass.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_DAMAGE_CLASS_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveDamageClass.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All MoveLearnMethods Positive Cases")
    class GetAllMoveLearnMethodsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveLearnMethods returns the first page")
        void testGetAllMoveLearnMethods() throws Exception
        {
            when(pokeApiClient.getResource(MoveLearnMethod.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllMoveLearnMethodsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_LEARN_METHOD_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllMoveLearnMethodsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveLearnMethod.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveLearnMethods returns the second page (offset is a page index)")
        void testGetAllMoveLearnMethodsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(MoveLearnMethod.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10MoveLearnMethodsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_LEARN_METHOD_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10MoveLearnMethodsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveLearnMethod.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All MoveLearnMethods Negative Cases")
    class GetAllMoveLearnMethodsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveLearnMethods returns no moveLearnMethods")
        void testGetAllMoveLearnMethodsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(MoveLearnMethod.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_LEARN_METHOD_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveLearnMethod.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveLearnMethods returns null moveLearnMethods")
        void testGetAllMoveLearnMethodsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(MoveLearnMethod.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(MOVE_LEARN_METHOD_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveLearnMethod.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveLearnMethods throws an exception")
        void testGetAllMoveLearnMethodsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveLearnMethod.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_LEARN_METHOD_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveLearnMethod.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get MoveLearnMethod Positive Cases")
    class GetMoveLearnMethodPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveLearnMethod returns the 1st moveLearnMethod")
        void testGetMoveLearnMethod() throws Exception
        {
            when(pokeApiClient.getResource(MoveLearnMethod.class, "1"))
                    .thenReturn(Mono.just(moveLearnMethod_1));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_LEARN_METHOD_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(moveLearnMethod_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveLearnMethod.class, "1");
        }
    }

    @Nested
    @DisplayName("Get MoveLearnMethod Negative Cases")
    class GetMoveLearnMethodNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveLearnMethod returns no moveLearnMethod")
        void testGetMoveLearnMethodReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(MoveLearnMethod.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_LEARN_METHOD_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveLearnMethod.class, "1");
        }

        @Test
        @DisplayName("getMoveLearnMethod throws an exception")
        void testGetMoveLearnMethodThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveLearnMethod.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_LEARN_METHOD_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveLearnMethod.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All MoveTargets Positive Cases")
    class GetAllMoveTargetsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveTargets returns the first page")
        void testGetAllMoveTargets() throws Exception
        {
            when(pokeApiClient.getResource(MoveTarget.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllMoveTargetsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_TARGET_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllMoveTargetsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveTarget.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveTargets returns the second page (offset is a page index)")
        void testGetAllMoveTargetsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(MoveTarget.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10MoveTargetsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_TARGET_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10MoveTargetsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveTarget.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All MoveTargets Negative Cases")
    class GetAllMoveTargetsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveTargets returns no moveTargets")
        void testGetAllMoveTargetsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(MoveTarget.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_TARGET_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveTarget.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveTargets returns null moveTargets")
        void testGetAllMoveTargetsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(MoveTarget.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(MOVE_TARGET_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveTarget.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMoveTargets throws an exception")
        void testGetAllMoveTargetsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveTarget.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_TARGET_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveTarget.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get MoveTarget Positive Cases")
    class GetMoveTargetPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveTarget returns the 1st moveTarget")
        void testGetMoveTarget() throws Exception
        {
            when(pokeApiClient.getResource(MoveTarget.class, "1"))
                    .thenReturn(Mono.just(moveTarget_1));

            MvcResult mvcResult = this.mockMvc.perform(get(MOVE_TARGET_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(moveTarget_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(MoveTarget.class, "1");
        }
    }

    @Nested
    @DisplayName("Get MoveTarget Negative Cases")
    class GetMoveTargetNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMoveTarget returns no moveTarget")
        void testGetMoveTargetReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(MoveTarget.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MOVE_TARGET_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(MoveTarget.class, "1");
        }

        @Test
        @DisplayName("getMoveTarget throws an exception")
        void testGetMoveTargetThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(MoveTarget.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MOVE_TARGET_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(MoveTarget.class, "1");
        }
    }
}
