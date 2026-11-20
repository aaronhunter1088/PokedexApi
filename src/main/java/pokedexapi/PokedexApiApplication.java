package pokedexapi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.Map;

@SpringBootApplication
@PropertySource("classpath:pokedexapi.properties")
class PokedexApiApplication
{
    @Configuration
    @Profile("local")
    @PropertySource("classpath:pokedexapi-local.properties")
    static class LocalProperties {}

    /* Main method */
    static void main(String[] args) throws IOException
    {
        new SpringApplicationBuilder(PokedexApiApplication.class)
                .properties(Map.of(
                        "spring.application.name", "PokedexApi",
                        "spring.application.version", "1.11.0",
                        "spring.jmx.enabled", "true",
                        "spring.main.allow-bean-definition-overriding", "true",
                        "spring.profiles.active", "production"))
                .run(args);
    }

}
