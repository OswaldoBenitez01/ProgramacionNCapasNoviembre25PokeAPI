package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import ProgramacionNCapasNoviembre25.PokeAPI.ML.Pokemon;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.PokemonListResponse;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class PokemonServiceHilos {

    private final RestTemplate restTemplate;
    private final String pokeApiBaseUrl;

    public PokemonServiceHilos(RestTemplate restTemplate, String pokeApiBaseUrl) {
        this.restTemplate = restTemplate;
        this.pokeApiBaseUrl = pokeApiBaseUrl;
    }

    public Result getPokemonListBasic(int limit, int offset) {
        Result result = new Result();
        try {
            String url = pokeApiBaseUrl + "pokemon?limit=" + limit + "&offset=" + offset;
            PokemonListResponse lista = restTemplate.getForObject(url, PokemonListResponse.class);
            
            result.Correct = true;
            result.Object = lista;
            
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = "Error lista básica: " + ex.getMessage();
            result.ex = ex;
        }
        return result;
    }

    public Result getPokemonListFull(int limit, int offset) {
        Result result = new Result();
        
        try {
            String urlBasic = pokeApiBaseUrl + "pokemon?limit=" + limit + "&offset=" + offset;
            PokemonListResponse basicList = restTemplate.getForObject(urlBasic, PokemonListResponse.class);
            
            List<Pokemon> pokemonsCompletos = new ArrayList<>();
            
            List<CompletableFuture<Pokemon>> listaHilos = basicList.getResults().stream()
                .map(pokeBasic -> CompletableFuture.supplyAsync(() -> {
                    String urlCompleto = pokeApiBaseUrl + "pokemon/" + pokeBasic.getName();
                    return restTemplate.getForObject(urlCompleto, Pokemon.class);
                }))
                .collect(Collectors.toList());

            for (CompletableFuture<Pokemon> hilo : listaHilos) {
                Pokemon pokemonCompleto = hilo.get();
                if (pokemonCompleto != null) {
                    pokemonsCompletos.add(pokemonCompleto);
                }
            }

            result.Correct = true;
            result.Objects = new ArrayList<>(pokemonsCompletos);
            result.Object = "nextOffset=" + (offset + limit);
            
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = "Error lista completa: " + ex.getMessage();
            result.ex = ex;
        }
        
        return result;
    }

    public Result getPokemonByIdOrName(String idOrName) {
        Result result = new Result();
        try {
            String url = pokeApiBaseUrl + "pokemon/" + idOrName;
            Pokemon pokemon = restTemplate.getForObject(url, Pokemon.class);
            
            result.Correct = true;
            result.Object = pokemon;
            
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = "No encontrado: " + idOrName;
            result.ex = ex;
        }
        return result;
    }
}
