
package ProgramacionNCapasNoviembre25.PokeAPI.Controller;

import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.PokemonServiceHilos;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pokemonlist")
public class PokemonController {

    private final PokemonServiceHilos pokemonService;

    public PokemonController(PokemonServiceHilos pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/basic")
    public String listBasic(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset,
            Model model) {
        
        Result result = pokemonService.getPokemonListBasic(limit, offset);
        model.addAttribute("result", result);
        model.addAttribute("currentOffset", offset);
        model.addAttribute("limit", limit);
        
        return "pokemonbasic";
    }

    @GetMapping("/full")
    public String listFull(
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "0") int offset,
            Model model) {
        
        Result result = pokemonService.getPokemonListFull(limit, offset);
        model.addAttribute("result", result);
        model.addAttribute("currentOffset", offset);
        model.addAttribute("limit", limit);
        
        return "index";
    }
}
