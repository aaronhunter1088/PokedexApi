package com.example.pokedexapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MyPokeApiReactorCachingConfigurationTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MyPokeApiReactorCachingConfiguration config ;

    @BeforeAll
    public static void setUp() {
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        config = new MyPokeApiReactorCachingConfiguration(objectMapper);
    }


    @Test
    public void testJsonDecoder() {
        Jackson2JsonDecoder decoder = config.jsonDecoder();
        assertNotNull(decoder);
    }

    @Test
    public void testJsonEncoder() {
        Jackson2JsonEncoder encoder = config.jsonEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testConnectionProvider() {
        ConnectionProvider provider = config.connectionProvider();
        assertNotNull(provider);
    }

    @Test
    public void testHttpClient() {
        ConnectionProvider provider = config.connectionProvider();
        HttpClient client = config.httpClient(provider);
        assertNotNull(client);
    }

    @Test
    public void testWebClientBuilder() {
        ConnectionProvider provider = config.connectionProvider();
        HttpClient client = config.httpClient(provider);
        WebClient.Builder builder = config.webClientBuilder(client);
        assertNotNull(builder);
    }
}
