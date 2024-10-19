package com.example.pokedexapi.config;

import io.swagger.v3.oas.models.PathItem;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Servlet Filter implementation class CORSFilter
 */
// Enable it for Servlet 3.x implementations
/* @ WebFilter(asyncSupported = true, urlPatterns = { "/*" }) */

//In order for Spring Boot to be able to recognize a filter, we just
// needed to define it as a bean with the @Component annotation.
@Component
class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200") // Replace with your frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

//    @Bean
//    CorsFilter corsFilter() {
//        CorsConfiguration cors = new CorsConfiguration();
//        /*
//        whether the browser should send credentials such as
//        cookies along with cross domain requests
//         */
//        cors.setAllowCredentials(true);
//        /*
//        set permitted origins that can access our resources
//         */
//        cors.setAllowedOrigins(
//                List.of("http://localhost:4200", "http://localhost:8080"));
//        /*
//        allows us to configure the list of headers permitted
//        in the HTTP request
//         */
//        cors.setAllowedHeaders(
//                List.of("Origin",
//                        "Access-Control-Allow-Origin",
//                        "Content-Type",
//                        "Accept",
//                        "Authorization",
//                        "Origin, Accept",
//                        "X-Requested-With",
//                        "Access-Control-Request-Method",
//                        "Access-Control-Request-Headers"));
//        /*
//        allows to specify the list of response headers from
//        the server
//         */
//        cors.setExposedHeaders(
//                List.of("Origin",
//                        "Content-Type",
//                        "Accept",
//                        "Authorization",
//                        "Access-Control-Allow-Origin",
//                        "Access-Control-Allow-Credentials"));
//        /*
//        allows us to configure the list of HTTP methods authorized
//        to be used to access resources
//         */
//        cors.setAllowedMethods(
//                List.of(HttpMethod.GET.name(), HttpMethod.POST.name(),
//                        HttpMethod.OPTIONS.name()));
//
//        /*
//        register the cors config
//        '/**' indicates the configuration applies to all methods
//        found in the application
//         */
//        var urlBasedCorsConfigSource = new UrlBasedCorsConfigurationSource();
//        urlBasedCorsConfigSource.registerCorsConfiguration("/**", cors);
//
//        return new CorsFilter(urlBasedCorsConfigSource);
//    }

}
