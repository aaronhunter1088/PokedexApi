package pokedexapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class JsonMapperConfig
{
    @Bean({"jsonMapper"})
    JsonMapper jsonMapper() {
        return JsonMapper.builder().build();
    }
}
