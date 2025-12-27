package pokedexapi.service;

import skaro.pokeapi.resource.NamedApiResourceList;
import skaro.pokeapi.resource.evolutionchain.EvolutionChain;
import skaro.pokeapi.resource.pokemon.Pokemon;
import skaro.pokeapi.resource.pokemonspecies.PokemonSpecies;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public interface PokemonService
{
    Pokemon getPokemonByIdOrName(String nameOrId) throws Exception;

    NamedApiResourceList<Pokemon> getAllPokemons(Integer limit, Integer offset) throws Exception;

    HttpResponse<String> callUrl(String url) throws Exception;

    PokemonSpecies getPokemonSpeciesData(String id) throws Exception;

    List<String> getPokemonLocationAreas(String url) throws Exception;

    EvolutionChain getPokemonEvolutionChain(String chainUrl) throws Exception;

    Map<String, Object> getPokemonChainData(String pokemonChainId);

    List<String> getAllTypes() throws Exception;

    int getTotalPokemon(String pokedexId);

    List<String> getPokemonNamesThatEvolveFromTrading() throws Exception;

    Map<Integer, List<List<Integer>>> getEvolutionsMap();
}
