package ProgramacionNCapasNoviembre25.PokeAPI.Controller;

import ProgramacionNCapasNoviembre25.PokeAPI.ML.Pokemon;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.PokemonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pokemon")
public class PokeApiController {

    private PokemonService pokemonService;

    public PokeApiController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/mostrar")
    public Pokemon obtenerPokemon() {

        Pokemon pokemon = pokemonService.getPokemon();
        
        System.out.println("El Pokemon obtenido es: " + pokemon);

        
        return pokemon;
    }

}
