package com.example.pokedexapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MyPokeApiReactorCachingConfigurationTest {

    private final MyPokeApiReactorCachingConfiguration config = new MyPokeApiReactorCachingConfiguration();

    @Test
    public void testJsonDecoder() {
        ObjectMapper objectMapper = new ObjectMapper();
        config.setObjectMapper(objectMapper);
        Jackson2JsonDecoder decoder = config.jsonDecoder();
        assertNotNull(decoder);
    }

    @Test
    public void testJsonEncoder() {
        ObjectMapper objectMapper = new ObjectMapper();
        config.setObjectMapper(objectMapper);
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
