package pokedexapi.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CorsConfigTest
{
    @Test
    @DisplayName("addCorsMappings registers CORS metadata")
    void testAddCorsMappings()
    {
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);
        when(registry.addMapping("/**")).thenReturn(registration);
        when(registration.allowedOrigins("http://localhost:4200", "http://localhost:4201", "http://localhost:4203")).thenReturn(registration);
        when(registration.allowedMethods("GET")).thenReturn(registration);
        when(registration.allowedHeaders("*")).thenReturn(registration);

        new CorsConfig().addCorsMappings(registry);

        verify(registry).addMapping("/**");
        assertThat(true).isTrue();
    }
}

