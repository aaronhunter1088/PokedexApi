package pokedexapi.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.service.registry.ImportHttpServices;
import pokedexapi.service.PokemonLocationEncounterService;

import static org.assertj.core.api.Assertions.assertThat;

class PokemonExchangeConfigTest
{
    @Test
    @DisplayName("configuration imports PokemonLocationEncounterService")
    void testImportHttpServicesAnnotation()
    {
        ImportHttpServices annotation = PokemonExchangeConfig.class.getAnnotation(ImportHttpServices.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).contains(PokemonLocationEncounterService.class);
    }
}

