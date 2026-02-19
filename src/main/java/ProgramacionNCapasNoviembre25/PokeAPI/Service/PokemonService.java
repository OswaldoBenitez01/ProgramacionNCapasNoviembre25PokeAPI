package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Pokemon;

@Service
public class PokemonService {

    private RestTemplate restTemplate = new RestTemplate();

    private String UrlBase = "https://pokeapi.co/api/v2/";

    public Pokemon getPokemon() {
        int cantidadPokemon = 12;
//        String url = UrlBase + "pokemon?limit=" + cantidadPokemon;
        String url = UrlBase + "pokemon/25";
        return restTemplate.getForObject(url, Pokemon.class);
    }

}
