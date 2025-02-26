package com.example.pokedexapi.entity;

import lombok.*;
import skaro.pokeapi.resource.FlavorText;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Pokemon extends skaro.pokeapi.resource.pokemon.Pokemon implements Comparable<Pokemon> {

    private Long id;
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

    public Pokemon(skaro.pokeapi.resource.pokemon.Pokemon pokemonResource) {
        super();
        setId(pokemonResource.getId());
        setName(pokemonResource.getName().substring(0, 1).toUpperCase() + pokemonResource.getName().substring(1));
        setBaseExperience(pokemonResource.getBaseExperience());
        setHeight(pokemonResource.getHeight());
        setIsDefault(pokemonResource.getIsDefault());
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

    String getType() {
        return type;
    }
    void setType(String type){
        this.type = type;
    }

    String getDefaultImage() {
        return defaultImage;
    }
    void setDefaultImage(String defaultImage){
        this.defaultImage = defaultImage;
    }

    String getOfficialImage() {
        return officialImage;
    }
    void setOfficialImage(String officialImage){
        this.officialImage = officialImage;
    }

    public String getGifImage() {
        return gifImage;
    }
    public void setGifImage(String gifImage){
        this.gifImage = gifImage;
    }

    String getShinyImage() {
        return shinyImage;
    }
    void setShinyImage(String shinyImage) {
        this.shinyImage = shinyImage;
    }

    String getColor() {
        return color;
    }
    void setColor(String color) {
        this.color = color;
    }

    List<FlavorText> getDescriptions() {
        return descriptions;
    }
    void setDescriptions(List<FlavorText> descriptions) {
        this.descriptions = descriptions;
    }

    String getDescription() {
        return description;
    }
    void setDescription(String description) {
        this.description = description;
    }

    List<String> getLocations() {
        return locations;
    }
    void setLocations(List<String> locations) {
        this.locations = locations;
    }

    List<String> getPokemonMoves() {
        return pokemonMoves;
    }
    void setPokemonMoves(List<String> moves) {
        this.pokemonMoves = moves;
    }

    String getCapitalizedColor () {
        return this.getColor().substring(0, 1).toUpperCase()
                + this.getColor().substring(1);
    }

    @Override
    public String toString () {
        return "Pokemon{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.getName() + '\'' +
                ", baseExperience='" + this.getBaseExperience() + '\'' +
                ", height='" + this.getHeight() + '\'' +
                ", isDefault='" + this.getIsDefault() + '\'' +
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

    @Override
    public int compareTo (Pokemon p){
        if (this == p) {
            return 0;
        }
        if (p != null) {
            return this.getId().compareTo(p.getId());
        }
        return 0;
    }
}