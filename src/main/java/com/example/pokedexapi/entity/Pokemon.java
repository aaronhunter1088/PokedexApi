package com.example.pokedexapi.entity;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import skaro.pokeapi.resource.FlavorText;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Pokemon extends skaro.pokeapi.resource.pokemon.Pokemon {

    String type;
    String defaultImage;
    String officialImage;
    String gifImage;
    String shinyImage;
    String color;
    List<FlavorText> descriptions;
    String description;
    List<String> locations;
    List<String> pokemonMoves;

    /**
     * Raw implementation of the clone method
     * @param pokemonResource the pokemon to clone
     * @param clone true/false just provided
     */
    public Pokemon(skaro.pokeapi.resource.pokemon.Pokemon pokemonResource, boolean clone) {
        this(pokemonResource);
    }

    /**
     * Main constructor for the Pokemon class
     * @param pokemonResource the skaro pokemon resource
     */
    public Pokemon(skaro.pokeapi.resource.pokemon.Pokemon pokemonResource) {
        super();
        setId(pokemonResource.getId());
        setName(getCapitalizedProperty(pokemonResource.getName()));
        setBaseExperience(pokemonResource.getBaseExperience());
        setHeight(pokemonResource.getHeight());
        setDefault(pokemonResource.getDefault());
        setOrder(pokemonResource.getOrder());
        setWeight(pokemonResource.getWeight());
        setAbilities(pokemonResource.getAbilities());
        setForms(pokemonResource.getForms());
        setGameIndices(pokemonResource.getGameIndices());
        setHeldItems(pokemonResource.getHeldItems());
        setLocationAreaEncounters(pokemonResource.getLocationAreaEncounters());
        setMoves(pokemonResource.getMoves());
        setSprites(pokemonResource.getSprites());
        setSpecies(pokemonResource.getSpecies());
        setStats(pokemonResource.getStats());
        setTypes(pokemonResource.getTypes());
        setPastTypes(pokemonResource.getPastTypes());
    }

    public Integer getId() {
        return super.getId();
    }
    public void setId(Integer id) {
        super.setId(id);
    }

    /**
     * Capitalizes the first letter of the specified property if it exists
     * @param value the value to capitalize
     * @return the capitalized property value
     */
    String getCapitalizedProperty(@NotNull String value) {
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", baseExperience='" + this.getBaseExperience() + '\'' +
                ", height='" + this.getHeight() + '\'' +
                ", isDefault='" + this.getDefault() + '\'' +
                ", order='" + this.getOrder() + '\'' +
                ", weight='" + this.getWeight() + '\'' +
                ", abilities='" + this.getAbilities() + '\'' +
                ", forms='" + this.getForms() + '\'' +
                ", gameIndices='" + this.getGameIndices() + '\'' +
                ", heldItems='" + this.getHeldItems() + '\'' +
                ", locationAreaEncounters='" + this.getLocationAreaEncounters() + '\'' +
                ", moves='" + this.getPokemonMoves() + '\'' +
                ", sprites='" + this.getSprites() + '\'' +
                ", species='" + this.getSpecies() + '\'' +
                ", stats='" + this.getStats() + '\'' +
                ", types='" + this.getType() + '\'' +
                ", pastTypes='" + this.getPastTypes() + '\'' +
                ", type='" + type + '\'' +
                ", defaultImage='" + defaultImage + '\'' +
                ", officialImage='" + officialImage + '\'' +
                ", gifImage='" + gifImage + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}