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
import skaro.pokeapi.resource.machine.Machine;
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
class MachineApiTest extends BaseApiTest
{
    private static final String MACHINE_API = "/machine";
    private static NamedApiResourceList<Machine> getAllMachinesResponse;
    private static NamedApiResourceList<Machine> getNext10MachinesResponse;
    private static Machine machine_1;

    @BeforeAll
    static void setup() throws IOException
    {
        getAllMachinesResponse = jsonMapper.readValue(
                new ClassPathResource("machineApi/getAllMachinesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        getNext10MachinesResponse = jsonMapper.readValue(
                new ClassPathResource("machineApi/getNext10MachinesResponse.json").getFile(),
                new TypeReference<>()
                {}
        );
        machine_1 = jsonMapper.readValue(
                new ClassPathResource("machineApi/machine_1.json").getFile(),
                Machine.class
        );
    }

    @Nested
    @DisplayName("Get All Machines Positive Cases")
    class GetAllMachinesPositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMachines returns the first page")
        void testGetAllMachines() throws Exception
        {
            when(pokeApiClient.getResource(Machine.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.just(getAllMachinesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MACHINE_API))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getAllMachinesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Machine.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMachines returns the second page (offset is a page index)")
        void testGetAllMachinesSecondPage() throws Exception
        {
            when(pokeApiClient.getResource(Machine.class, new PageQuery(10, 10)))
                    .thenReturn(Mono.just(getNext10MachinesResponse));

            MvcResult mvcResult = this.mockMvc.perform(get(MACHINE_API + "?limit=10&offset=1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(getNext10MachinesResponse);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Machine.class, new PageQuery(10, 10));
        }
    }

    @Nested
    @DisplayName("Get All Machines Negative Cases")
    class GetAllMachinesNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMachines returns no machines")
        void testGetAllMachinesReturnsNoResults() throws Exception
        {
            when(pokeApiClient.getResource(Machine.class, new PageQuery(10, 0)))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MACHINE_API))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Machine.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMachines returns null machines")
        void testGetAllMachinesReturnsNull() throws Exception
        {
            when(pokeApiClient.getResource(Machine.class, new PageQuery(10, 0)))
                    .thenReturn(null);

            this.mockMvc.perform(get(MACHINE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Machine.class, new PageQuery(10, 0));
        }

        @Test
        @DisplayName("getMachines throws an exception")
        void testGetAllMachinesThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Machine.class, new PageQuery(10, 0)))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MACHINE_API))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Machine.class, new PageQuery(10, 0));
        }
    }

    @Nested
    @DisplayName("Get Machine Positive Cases")
    class GetMachinePositive
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMachine returns the 1st machine")
        void testGetMachine() throws Exception
        {
            when(pokeApiClient.getResource(Machine.class, "1"))
                    .thenReturn(Mono.just(machine_1));

            MvcResult mvcResult = this.mockMvc.perform(get(MACHINE_API + "/1"))
                    .andExpect(status().isOk())
                    .andReturn();

            String actual = mvcResult.getResponse().getContentAsString();
            String expected = jsonMapper.writeValueAsString(machine_1);

            assertThat(expected).isEqualTo(actual);

            verify(pokeApiClient).getResource(Machine.class, "1");
        }
    }

    @Nested
    @DisplayName("Get Machine Negative Cases")
    class GetMachineNegative
    {
        @Autowired
        private MockMvc mockMvc;

        @Test
        @DisplayName("getMachine returns no machine")
        void testGetMachineReturnsNoResult() throws Exception
        {
            when(pokeApiClient.getResource(Machine.class, "1"))
                    .thenReturn(Mono.empty());

            this.mockMvc.perform(get(MACHINE_API + "/1"))
                    .andExpect(status().isBadRequest());

            verify(pokeApiClient).getResource(Machine.class, "1");
        }

        @Test
        @DisplayName("getMachine throws an exception")
        void testGetMachineThrowsException() throws Exception
        {
            when(pokeApiClient.getResource(Machine.class, "1"))
                    .thenThrow(new RuntimeException("Test Exception"));

            this.mockMvc.perform(get(MACHINE_API + "/1"))
                    .andExpect(status().isInternalServerError());

            verify(pokeApiClient).getResource(Machine.class, "1");
        }
    }
}
