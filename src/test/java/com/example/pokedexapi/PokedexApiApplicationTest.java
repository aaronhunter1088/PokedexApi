package com.example.pokedexapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PokedexApiApplicationTest {

    @Autowired
    private PokedexApiApplication application;
    /*
     * Fixture: Executed before every test
     */
    protected void setUp() {}
    /*
        * Fixture: Executed after every test
     */
    protected void tearDown() {}

    @Test
    void contextLoads() {
        assertThat(application).isNotNull();
    }

}
