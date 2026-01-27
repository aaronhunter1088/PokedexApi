package pokedexapi;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
class PokedexApiApplication //extends SpringBootServletInitializer
{
    /* Main method */
    static void main(String[] args)
    {
        SpringApplication.run(PokedexApiApplication.class, args);
    }

//    @NonNull
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
//    {
//        return builder.sources(PokedexApiApplication.class);
//    }

}
