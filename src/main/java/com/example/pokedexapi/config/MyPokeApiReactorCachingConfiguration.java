package com.example.pokedexapi.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import skaro.pokeapi.PokeApiReactorCachingConfiguration;

import java.time.Duration;

@Configuration
@Import(PokeApiReactorCachingConfiguration.class)
@EnableCaching
class MyPokeApiReactorCachingConfiguration {

    @Bean
    ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("Auto refresh & no connection limit")
                .maxIdleTime(Duration.ofSeconds(10))
                .maxConnections(500)
                .pendingAcquireMaxCount(-1)
                .build();
    }

    @Bean
    HttpClient httpClient(ConnectionProvider connectionProvider) {
        return HttpClient.create(connectionProvider)
                .compress(true)
                .resolver(DefaultAddressResolverGroup.INSTANCE)
                .tcpConfiguration(tcpClient ->
                        tcpClient.option(ChannelOption.SO_RCVBUF, 1048576)  // Set receive buffer size
                                .option(ChannelOption.SO_SNDBUF, 1048576)  // Set send buffer size
                                .doOnConnected(connection ->
                                        connection.addHandlerLast(new HttpObjectAggregator(10485760)))); // Set max message size
    }

    @Bean
    WebClient.Builder webClientBuilder(HttpClient httpClient) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(clientCodecConfigurer ->
                        clientCodecConfigurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)); // Set max in-memory size
    }
}