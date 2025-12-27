package pokedexapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;
import pokedexapi.service.PokemonLocationEncounterService;

@Configuration(proxyBeanMethods = false)
@ImportHttpServices({PokemonLocationEncounterService.class})
public class PokemonExchangeConfig
{
}
