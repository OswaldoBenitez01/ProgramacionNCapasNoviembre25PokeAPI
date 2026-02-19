// Controller/PokemonController.java
package ProgramacionNCapasNoviembre25.PokeAPI.Controller;

import ProgramacionNCapasNoviembre25.PokeAPI.ML.Pokemon;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.PokemonListResponse;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controller/PokemonController.java
@Controller
@RequestMapping("/pokemon")
public class PokeApiController {

    private PokemonService pokemonService;

    public PokeApiController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/list")
    public String pokemonList(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset,
            Model model) {

        Result result = pokemonService.getPokemonList(limit, offset);
        
        model.addAttribute("result", result);
        model.addAttribute("nextOffset", offset + limit);
        
        return "pokemon/list";
    }

    @GetMapping("/{idOrName}")
    public String pokemonDetail(@PathVariable String idOrName, Model model) {
        
        Result result = pokemonService.getPokemonByIdOrName(idOrName);
        model.addAttribute("result", result);
        
        return "pokemon/detail";
    }
}
