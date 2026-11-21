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
import skaro.pokeapi.resource.version.Version;
import skaro.pokeapi.resource.versiongroup.VersionGroup;
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
class VersionApiTest extends BaseApiTest
{
    private static final String VERSION_API = "/version";
    private static final String VERSION_GROUP_API = VERSION_API + "/version-group";
    private static NamedApiResourceList<Version> getAllVersionsResponse;
    private static NamedApiResourceList<Version> getNext10VersionsResponse;
    private static Version version_1;
    private static NamedApiResourceList<VersionGroup> getAllVersionGroupsResponse;
    private static NamedApiResourceList<VersionGroup> getNext10VersionGroupsResponse;
    private static VersionGroup versionGroup_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllVersionsResponse = jsonMapper.readValue(
                new ClassPathResource("versionApi/getAllVersionsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10VersionsResponse = jsonMapper.readValue(
                new ClassPathResource("versionApi/getNext10VersionsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        version_1 = jsonMapper.readValue(
                new ClassPathResource("versionApi/version_1.json").getFile(),
                Version.class
        );
        getAllVersionGroupsResponse = jsonMapper.readValue(
                new ClassPathResource("versionApi/getAllVersionGroupsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10VersionGroupsResponse = jsonMapper.readValue(
                new ClassPathResource("versionApi/getNext10VersionGroupsResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        versionGroup_1 = jsonMapper.readValue(
                new ClassPathResource("versionApi/version_group_1.json").getFile(),
                VersionGroup.class
        );
    }

    @Nested
    @DisplayName("Get All Versions Positive Cases")
    class GetAllVersionsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getVersions returns the first page")
        void testGetAllVersions() throws Exception
        {
            when(pokeApiClient.getResource(Version.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllVersionsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(VERSION_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllVersionsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Version.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getVersions returns the second page (offset is a page index)")
        void testGetAllVersionsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Version.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10VersionsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(VERSION_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10VersionsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Version.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Versions Negative Cases")
    class GetAllVersionsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getVersions returns no versions")
        void testGetAllVersionsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Version.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(VERSION_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Version.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getVersions returns null versions")
        void testGetAllVersionsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Version.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(VERSION_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Version.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getVersions throws an exception")
        void testGetAllVersionsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Version.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(VERSION_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Version.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Version Positive Cases")
    class GetVersionPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getVersion returns the 1st version")
        void testGetVersion() throws Exception
        {
            when(pokeApiClient.getResource(Version.class, "1"))
                    .thenReturn(Mono.just(version_1));

            MvcResult mvcResult = this.mockMvc.perform(get(VERSION_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(version_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Version.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Version Negative Cases")
    class GetVersionNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getVersion returns no version")
        void testGetVersionReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Version.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(VERSION_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Version.class, "1");
        }

        @Test
        @DisplayName("getVersion throws an exception")
        void testGetVersionThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Version.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(VERSION_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Version.class, "1");
        }
    }

    @Nested
    @DisplayName("Get All VersionGroups Positive Cases")
    class GetAllVersionGroupsPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getVersionGroups returns the first page")
        void testGetAllVersionGroups() throws Exception
        {
            when(pokeApiClient.getResource(VersionGroup.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllVersionGroupsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(VERSION_GROUP_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllVersionGroupsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(VersionGroup.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getVersionGroups returns the second page (offset is a page index)")
        void testGetAllVersionGroupsSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(VersionGroup.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10VersionGroupsResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(VERSION_GROUP_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10VersionGroupsResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(VersionGroup.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All VersionGroups Negative Cases")
    class GetAllVersionGroupsNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getVersionGroups returns no versionGroups")
        void testGetAllVersionGroupsReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(VersionGroup.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(VERSION_GROUP_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(VersionGroup.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getVersionGroups returns null versionGroups")
        void testGetAllVersionGroupsReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(VersionGroup.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(VERSION_GROUP_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(VersionGroup.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getVersionGroups throws an exception")
        void testGetAllVersionGroupsThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(VersionGroup.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(VERSION_GROUP_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(VersionGroup.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get VersionGroup Positive Cases")
    class GetVersionGroupPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getVersionGroup returns the 1st versionGroup")
        void testGetVersionGroup() throws Exception
        {
            when(pokeApiClient.getResource(VersionGroup.class, "1"))
                    .thenReturn(Mono.just(versionGroup_1));

            MvcResult mvcResult = this.mockMvc.perform(get(VERSION_GROUP_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(versionGroup_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(VersionGroup.class, "1");
        }
    }

    @Nested
    @DisplayName("Get VersionGroup Negative Cases")
    class GetVersionGroupNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getVersionGroup returns no versionGroup")
        void testGetVersionGroupReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(VersionGroup.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(VERSION_GROUP_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(VersionGroup.class, "1");
        }

        @Test
        @DisplayName("getVersionGroup throws an exception")
        void testGetVersionGroupThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(VersionGroup.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(VERSION_GROUP_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(VersionGroup.class, "1");
        }
    }
}
