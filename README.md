# PokedexApi

## Overview
PokedexApi is a RESTful API that provides information about various Pokémon. It allows users to
retrieve details about Pokémon, including their types, abilities, and stats.

## Usage
```xml
<dependency>
    <groupId>com.skvllprodvctions</groupId>
    <artifactId>PokedexApi</artifactId>
    <version>${pokedex.api.version}</version>
    <classifier>code</classifier>
    <exclusions>
        <!-- spring-boot-starter-logging -->
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
        <!-- spring-boot-starter-jdbc: brings in HikariCP -->
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </exclusion>
        <!-- spring-boot-starter-data-jpa -->
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </exclusion>
        <!-- mysql connector -->
        <exclusion>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```