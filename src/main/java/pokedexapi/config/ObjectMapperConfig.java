package pokedexapi.config;

import tools.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean({"objectMapper"})
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
