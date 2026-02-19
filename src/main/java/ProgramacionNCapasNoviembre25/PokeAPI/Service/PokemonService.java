package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import ProgramacionNCapasNoviembre25.PokeAPI.ML.Pokemon;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.PokemonListResponse;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PokemonService {

    private final RestTemplate restTemplate;
    private final String pokeApiBaseUrl;

    public PokemonService(RestTemplate restTemplate, String pokeApiBaseUrl) {
        this.restTemplate = restTemplate;
        this.pokeApiBaseUrl = pokeApiBaseUrl;
    }

    public Result getPokemonList(int limit, int offset) {
        try {
            String url = pokeApiBaseUrl + "pokemon?limit=" + limit + "&offset=" + offset;
            PokemonListResponse response = restTemplate.getForObject(url, PokemonListResponse.class);
            
            Result result = new Result();
            result.setCorrect(true);
            result.setObject(response);
            return result;
            
        } catch (Exception ex) {
            Result result = new Result();
            result.setCorrect(false);
            result.setErrorMessage("Error al obtener lista: " + ex.getMessage());
            result.setEx(ex);
            return result;
        }
    }

    public Result getPokemonByIdOrName(String idOrName) {
        try {
            String url = pokeApiBaseUrl + "pokemon/" + idOrName;
            Pokemon pokemon = restTemplate.getForObject(url, Pokemon.class);
            
            Result result = new Result();
            result.setCorrect(true);
            result.setObject(pokemon);
            return result;
            
        } catch (Exception ex) {
            Result result = new Result();
            result.setCorrect(false);
            result.setErrorMessage("Pokémon no encontrado: " + idOrName);
            result.setEx(ex);
            return result;
        }
    }
}
