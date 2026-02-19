package ProgramacionNCapasNoviembre25.PokeAPI.Controller;

import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.PokemonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// Controller/PokemonController.java
@Controller
@RequestMapping("/pokemon")
public class PokeApiController {

    private final PokemonService pokemonService;

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
        
        return "pokemon"; // templates/pokemon/list.html
    }

    @GetMapping("/{idOrName}")
    public String pokemonDetail(@PathVariable String idOrName, Model model) {
        
        Result result = pokemonService.getPokemonByIdOrName(idOrName);
        model.addAttribute("result", result);
        
        return "pokemon";
    }
}
