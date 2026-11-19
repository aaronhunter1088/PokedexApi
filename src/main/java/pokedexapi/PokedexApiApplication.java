package pokedexapi;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/*
 * PokedexApi's settings live in pokedexapi.properties (and pokedexapi-local.properties
 * for the "local" profile) instead of application*.properties, so they no longer
 * collide with the application*.properties of any app that includes this library.
 */
@SpringBootApplication
@PropertySource("classpath:pokedexapi.properties")
class PokedexApiApplication //extends SpringBootServletInitializer
{
    @Configuration
    @Profile("local")
    @PropertySource("classpath:pokedexapi-local.properties")
    static class LocalProperties {}

    /* Main method */
    static void main(String[] args) throws IOException
    {
        final Properties apiProperties = PropertiesLoaderUtils.loadAllProperties("pokedexapi.properties");
        // Read before @PropertySource files are processed, so they cannot live in pokedexapi.properties
        new SpringApplicationBuilder(PokedexApiApplication.class)
                .properties(Map.of(
                        "spring.application.name", "PokedexApi",
                        "spring.application.version", apiProperties.getProperty("spring.application.version"),
                        "spring.jmx.enabled", "true",
                        "spring.main.allow-bean-definition-overriding", "true",
                        "spring.profiles.active", "production"))
                .run(args);
    }

}
