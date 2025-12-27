package pokedexapi.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MyPokeApiReactorCachingConfigurationTest
{

    @InjectMocks
    private MyPokeApiReactorCachingConfiguration config;

    @BeforeAll
    public static void setUp()
    {
    }

    @BeforeEach
    public void init()
    {
        config = new MyPokeApiReactorCachingConfiguration();
    }


//    @Test
//    public void testJsonDecoder() {
//        JacksonJsonDecoder decoder = config.jsonDecoder(); //config.pokeapiDecoderBean(objectMapper);
//        assertNotNull(decoder);
//    }
//
//    @Test
//    public void testJsonEncoder() {
//        JacksonJsonEncoder encoder = config.jsonEncoder(); //config.pokeapiEncoderBean(objectMapper);
//        assertNotNull(encoder);
//    }

    @Test
    public void testConnectionProvider()
    {
        ConnectionProvider provider = config.connectionProvider();
        assertNotNull(provider);
    }

    @Test
    public void testHttpClient()
    {
        ConnectionProvider provider = config.connectionProvider();
        HttpClient client = config.httpClient(provider);
        assertNotNull(client);
    }

//    @Test
//    public void testWebClientBuilder() {
//        ConnectionProvider provider = config.connectionProvider();
//        HttpClient client = config.httpClient(provider);
//        WebClient.Builder builder = config.webClientBuilder(client);
//        assertNotNull(builder);
//    }
}
