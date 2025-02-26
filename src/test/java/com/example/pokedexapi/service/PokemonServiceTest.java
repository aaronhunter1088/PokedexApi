package com.example.pokedexapi.service;

import com.example.pokedexapi.controller.BaseApiTest;
import com.example.pokedexapi.entity.Pokemon;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import skaro.pokeapi.client.PokeApiClient;
import skaro.pokeapi.resource.PokeApiResource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Used to integrated JUnit 5's Spring TestContext
 * Framework into JUnit 5's Jupiter programming model.
 * It provides support for loading a Spring ApplicationContext
 * and having beans @Autowired into your test instance.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class PokemonServiceTest {

    @Mock
    private PokeApiClient pokeApiClient;
    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private PokemonService pokemonService;

    skaro.pokeapi.resource.pokemon.Pokemon pikachu;

    @BeforeEach
    void setUp() throws IOException {
        pikachu = objectMapper.readValue(
                new ClassPathResource("pikachu.json").getFile(),
                skaro.pokeapi.resource.pokemon.Pokemon.class
        );
    }

    @Test
    @DisplayName("Test getPokemonByName using pikachu's name")
    void testGetPokemonById() {
        Mono<PokeApiResource> just = Mono.just(pikachu);
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(just);

        Pokemon result = new com.example.pokedexapi.entity.Pokemon(pokemonService.getPokemonByName("pikachu"));
        assertNotNull(result);
        verify(pokeApiClient, times(1)).getResource(any(), anyString());
    }

    @Test
    @DisplayName("Test getPokemonByName using pikachu's id")
    void testGetPokemonByName2() {
        Mono<PokeApiResource> just = Mono.just(pikachu);
        when(pokeApiClient.getResource(any(), anyString())).thenReturn(just);

        Pokemon result = new com.example.pokedexapi.entity.Pokemon(pokemonService.getPokemonByName("25"));
        assertNotNull(result);
        verify(pokeApiClient, times(1)).getResource(any(), anyString());
    }
}