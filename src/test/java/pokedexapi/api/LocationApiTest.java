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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pokedexapi.controllers.BaseApiTest;
import pokedexapi.service.PokemonLocationEncounterService;
import reactor.core.publisher.Mono;
import skaro.pokeapi.query.PageQuery;
import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.location.Location;
import skaro.pokeapi.resource.locationarea.LocationArea;
import skaro.pokeapi.resource.palparkarea.PalParkArea;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.region.Region;
import tools.jackson.core.type.TypeReference;

import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LocationApiTest extends BaseApiTest
{
    private static final String LOCATION_API = "/location";
    private static final String LOCATION_AREA_API = LOCATION_API + "/location-area";
    private static final String PAL_PARK_AREA_API = LOCATION_API + "/pal-park-area";
    private static final String LOCATION_AREA_ENCOUNTERS_API = LOCATION_AREA_API + "/25/encounters";
    private static final String REGION_API = LOCATION_API + "/region";
    private static Pokemon pikachu;
    private static String pikachuLocationEncountersResponse;
    private static NamedApiResourceList<Location> getAllLocationsResponse;
    private static NamedApiResourceList<Location> getNext10LocationsResponse;
    private static NamedApiResourceList<LocationArea> getAllLocationAreasResponse;
    private static NamedApiResourceList<LocationArea> getNext10LocationAreasResponse;
    private static NamedApiResourceList<PalParkArea> getAllPalParkAreasResponse;
    private static NamedApiResourceList<PalParkArea> getNextPalParkAreasResponse;
    private static NamedApiResourceList<Region> getAllRegionsResponse;
    private static NamedApiResourceList<Region> getNext10RegionsResponse;
    private static Location location_1;
    private static LocationArea locationArea_1;
    private static PalParkArea palParkArea_1;
    private static Region region_1;

    @MockitoBean
    private PokemonLocationEncounterService pokemonLocationEncounterService;

    @BeforeAll
    static void setup() throws IOException
    {
        pikachu = jsonMapper.readValue(
                new ClassPathResource("pokemonApi/pikachu.json").getFile(),
                Pokemon.class
        );
        pikachuLocationEncountersResponse = Files.readString(
                new ClassPathResource("locationApi/getPikachuLocationEncountersResponse.json").getFile().toPath());
        getAllLocationsResponse = jsonMapper.readValue(
                new ClassPathResource("locationApi/getAllLocationsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10LocationsResponse = jsonMapper.readValue(
                new ClassPathResource("locationApi/getNext10LocationsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        location_1 = jsonMapper.readValue(
                new ClassPathResource("locationApi/location_1.json").getFile(),
                Location.class
        );
        getAllLocationAreasResponse = jsonMapper.readValue(
                new ClassPathResource("locationApi/getAllLocationAreasResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10LocationAreasResponse = jsonMapper.readValue(
                new ClassPathResource("locationApi/getNext10LocationAreasResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        locationArea_1 = jsonMapper.readValue(
                new ClassPathResource("locationApi/location_area_1.json").getFile(),
                LocationArea.class
        );
        getAllPalParkAreasResponse = jsonMapper.readValue(
                new ClassPathResource("locationApi/getAllPalParkAreasResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNextPalParkAreasResponse = jsonMapper.readValue(
                new ClassPathResource("locationApi/getNextPalParkAreasResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        palParkArea_1 = jsonMapper.readValue(
                new ClassPathResource("locationApi/pal_park_area_1.json").getFile(),
                PalParkArea.class
        );
        getAllRegionsResponse = jsonMapper.readValue(
                new ClassPathResource("locationApi/getAllRegionsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10RegionsResponse = jsonMapper.readValue(
                new ClassPathResource("locationApi/getNext10RegionsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        region_1 = jsonMapper.readValue(
                new ClassPathResource("locationApi/region_1.json").getFile(),
                Region.class
        );
    }

    @Nested
    @DisplayName("Get All Locations Positive Cases")
    class GetAllLocationsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getLocations returns the first page")
        void testGetAllLocations() throws Exception
        {
            when(pokeApiClient.getResource(Location.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllLocationsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(LOCATION_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllLocationsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Location.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getLocations returns the second page (offset is a page index)")
        void testGetAllLocationsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Location.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10LocationsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(LOCATION_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10LocationsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Location.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Locations Negative Cases")
    class GetAllLocationsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getLocations returns no locations")
        void testGetAllLocationsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Location.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(LOCATION_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Location.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getLocations returns null locations")
        void testGetAllLocationsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Location.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(LOCATION_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Location.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getLocations throws an exception")
        void testGetAllLocationsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Location.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(LOCATION_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Location.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Location Positive Cases")
    class GetLocationPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getLocation returns the 1st location")
        void testGetLocation() throws Exception
        {
            when(pokeApiClient.getResource(Location.class, "1"))
                    .thenReturn(Mono.just(location_1));

            MvcResult mvcResult = this.mockMvc.perform(get(LOCATION_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(location_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Location.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Location Negative Cases")
    class GetLocationNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getLocation returns no location")
        void testGetLocationReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Location.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(LOCATION_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Location.class, "1");
        }

        @Test
        @DisplayName("getLocation throws an exception")
        void testGetLocationThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Location.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(LOCATION_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Location.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All LocationAreas Positive Cases")
    class GetAllLocationAreasPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getLocationAreas returns the first page")
        void testGetAllLocationAreas() throws Exception
        {
            when(pokeApiClient.getResource(LocationArea.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllLocationAreasResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(LOCATION_AREA_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllLocationAreasResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(LocationArea.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getLocationAreas returns the second page (offset is a page index)")
        void testGetAllLocationAreasSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(LocationArea.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10LocationAreasResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(LOCATION_AREA_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10LocationAreasResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(LocationArea.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All LocationAreas Negative Cases")
    class GetAllLocationAreasNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getLocationAreas returns no locationAreas")
        void testGetAllLocationAreasReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(LocationArea.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(LOCATION_AREA_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(LocationArea.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getLocationAreas returns null locationAreas")
        void testGetAllLocationAreasReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(LocationArea.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(LOCATION_AREA_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(LocationArea.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getLocationAreas throws an exception")
        void testGetAllLocationAreasThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(LocationArea.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(LOCATION_AREA_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(LocationArea.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get LocationArea Positive Cases")
    class GetLocationAreaPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getLocationArea returns the 1st locationArea")
        void testGetLocationArea() throws Exception
        {
            when(pokeApiClient.getResource(LocationArea.class, "1"))
                    .thenReturn(Mono.just(locationArea_1));

            MvcResult mvcResult = this.mockMvc.perform(get(LOCATION_AREA_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(locationArea_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(LocationArea.class, "1");
        }
    }

    @Nested
    @DisplayName("Get LocationArea Negative Cases")
    class GetLocationAreaNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getLocationArea returns no locationArea")
        void testGetLocationAreaReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(LocationArea.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(LOCATION_AREA_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(LocationArea.class, "1");
        }

        @Test
        @DisplayName("getLocationArea throws an exception")
        void testGetLocationAreaThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(LocationArea.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(LOCATION_AREA_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(LocationArea.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All PalParkAreas Positive Cases")
    class GetAllPalParkAreasPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPalParkAreas returns the first page")
        void testGetAllPalParkAreas() throws Exception
        {
            when(pokeApiClient.getResource(PalParkArea.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllPalParkAreasResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(PAL_PARK_AREA_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllPalParkAreasResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PalParkArea.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getPalParkAreas returns the second page (offset is a page index)")
        void testGetAllPalParkAreasSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(PalParkArea.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNextPalParkAreasResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(PAL_PARK_AREA_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNextPalParkAreasResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PalParkArea.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All PalParkAreas Negative Cases")
    class GetAllPalParkAreasNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPalParkAreas returns no palParkAreas")
        void testGetAllPalParkAreasReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(PalParkArea.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(PAL_PARK_AREA_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PalParkArea.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getPalParkAreas returns null palParkAreas")
        void testGetAllPalParkAreasReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(PalParkArea.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(PAL_PARK_AREA_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PalParkArea.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getPalParkAreas throws an exception")
        void testGetAllPalParkAreasThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(PalParkArea.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(PAL_PARK_AREA_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PalParkArea.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get PalParkArea Positive Cases")
    class GetPalParkAreaPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPalParkArea returns the 1st palParkArea")
        void testGetPalParkArea() throws Exception
        {
            when(pokeApiClient.getResource(PalParkArea.class, "1"))
                    .thenReturn(Mono.just(palParkArea_1));

            MvcResult mvcResult = this.mockMvc.perform(get(PAL_PARK_AREA_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(palParkArea_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(PalParkArea.class, "1");
        }
    }

    @Nested
    @DisplayName("Get PalParkArea Negative Cases")
    class GetPalParkAreaNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getPalParkArea returns no palParkArea")
        void testGetPalParkAreaReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(PalParkArea.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(PAL_PARK_AREA_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(PalParkArea.class, "1");
        }

        @Test
        @DisplayName("getPalParkArea throws an exception")
        void testGetPalParkAreaThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(PalParkArea.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(PAL_PARK_AREA_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(PalParkArea.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All Regions Positive Cases")
    class GetAllRegionsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getRegions returns the first page")
        void testGetAllRegions() throws Exception
        {
            when(pokeApiClient.getResource(Region.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllRegionsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(REGION_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllRegionsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Region.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getRegions returns the second page (offset is a page index)")
        void testGetAllRegionsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Region.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10RegionsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(REGION_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10RegionsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Region.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Regions Negative Cases")
    class GetAllRegionsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getRegions returns no regions")
        void testGetAllRegionsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Region.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(REGION_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Region.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getRegions returns null regions")
        void testGetAllRegionsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Region.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(REGION_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Region.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getRegions throws an exception")
        void testGetAllRegionsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Region.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(REGION_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Region.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Region Positive Cases")
    class GetRegionPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getRegion returns the 1st region")
        void testGetRegion() throws Exception
        {
            when(pokeApiClient.getResource(Region.class, "1"))
                    .thenReturn(Mono.just(region_1));

            MvcResult mvcResult = this.mockMvc.perform(get(REGION_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(region_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Region.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Region Negative Cases")
    class GetRegionNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getRegion returns no region")
        void testGetRegionReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Region.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(REGION_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Region.class, "1");
        }

        @Test
        @DisplayName("getRegion throws an exception")
        void testGetRegionThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Region.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(REGION_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Region.class, "1");
        }
    }

    @Nested
    @DisplayName("Get LocationArea Encounters Positive Cases")
    class GetLocationAreaEncountersPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getLocationAreaEncounters returns the encounters for id 25")
        void testGetLocationAreaEncounters() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, "25"))
                    .thenReturn(Mono.just(pikachu));
            when(pokemonLocationEncounterService.getPokemonLocationEncounters(25))
                    .thenReturn(ResponseEntity.ok(pikachuLocationEncountersResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(LOCATION_AREA_ENCOUNTERS_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();

            assertThat(actual).isEqualTo(pikachuLocationEncountersResponse);

            verify(pokeApiClient).getResource(Pokemon.class, "25");
            verify(pokemonLocationEncounterService).getPokemonLocationEncounters(25);
        }
    }

    @Nested
    @DisplayName("Get LocationArea Encounters Negative Cases")
    class GetLocationAreaEncountersNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getLocationAreaEncounters returns no pokemon")
        void testGetLocationAreaEncountersReturnsNoPokemon() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, "25"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(LOCATION_AREA_ENCOUNTERS_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Pokemon.class, "25");
            verifyNoInteractions(pokemonLocationEncounterService);
        }

        @Test
        @DisplayName("getLocationAreaEncounters fails to retrieve the pokemon")
        void testGetLocationAreaEncountersPokemonRetrievalFails() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, "25"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(LOCATION_AREA_ENCOUNTERS_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Pokemon.class, "25");
            verifyNoInteractions(pokemonLocationEncounterService);
        }

        @Test
        @DisplayName("getLocationAreaEncounters throws an exception from the encounter service")
        void testGetLocationAreaEncountersThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Pokemon.class, "25"))
                    .thenReturn(Mono.just(pikachu));
            when(pokemonLocationEncounterService.getPokemonLocationEncounters(25))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(LOCATION_AREA_ENCOUNTERS_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Pokemon.class, "25");
            verify(pokemonLocationEncounterService).getPokemonLocationEncounters(25);
        }
    }
}
