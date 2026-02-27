package ProgramacionNCapasNoviembre25.PokeAPI.Controller;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Favorito;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Pokemon;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.FavoritoService;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.PokemonService;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.UsuarioService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pokemon")
public class PokeApiController {

    @Autowired
    private FavoritoService favoritoService;

    @Autowired
    private UsuarioService usuarioService;

    private final PokemonService pokemonService;

    public PokeApiController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping
    public String listPokemon(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String region,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset,
            Model model) {

        Result result = pokemonService.getPokemonFiltered(query, type, region, limit, offset);

        Usuario logueado = obtenerUsuarioLogueado();
        int idUsuario = logueado.getIdUsuario();

        model.addAttribute("Favoritos",     FavoritosPokemonId(idUsuario));
        model.addAttribute("UsuarioL",      logueado);
        model.addAttribute("result",        result);
        model.addAttribute("currentOffset", offset);
        model.addAttribute("limit",         limit);
        model.addAttribute("currentType",   type);
        model.addAttribute("currentRegion", region);
        model.addAttribute("currentQuery",  query);

        Result tiposResult = pokemonService.getAllTypes();
        model.addAttribute("tipos", tiposResult.Objects);

        Map<String, Long> conteos = favoritoService.obtenerConteos();
        model.addAttribute("conteos", conteos);

        int totalPages;
        try {
            int totalPokemon = Integer.parseInt(result.ErrorMessage.trim());
            totalPages = Math.max(1, (int) Math.ceil((double) totalPokemon / limit));
        } catch (Exception e) {
            int paginaActual = (offset / limit) + 1;
            boolean esUltima = result.Objects == null || result.Objects.size() < limit;
            totalPages = esUltima ? paginaActual : paginaActual + 999;
        }
        model.addAttribute("totalPages", totalPages);

        return "index";
    }

    @GetMapping("/favoritos")
    public String pokemonFav(Model model) {
        Usuario logueado = obtenerUsuarioLogueado();
        int idUsuario = logueado.getIdUsuario();
        Result resultFavoritosAll = favoritoService.getAllByUsuario(idUsuario);
        model.addAttribute("Favoritos", resultFavoritosAll);
        model.addAttribute("UsuarioL",  logueado);
        return "pokemonfavoritos";
    }

    @GetMapping("/ranking")
    public String pokemonRankin(Model model) {
        Usuario logueado = obtenerUsuarioLogueado();
        Map resultFavoritosAll = favoritoService.obtenerConteos();
        model.addAttribute("Favoritos", resultFavoritosAll);
        model.addAttribute("UsuarioL",  logueado);
        return "Ranking";
    }

    @GetMapping("/{idOrName}")
    public String pokemonDetail(@PathVariable String idOrName, Model model) {
        Result result = pokemonService.getPokemonByIdOrName(idOrName);
        if (result.Correct) {
            Pokemon pokemon = (Pokemon) result.Object;
            model.addAttribute("pokemon",       pokemon);
            model.addAttribute("Color",         obtenerTipo(pokemon));
            model.addAttribute("coloresTipos",  obtenerColoresTipos(pokemon));
        } else {
            model.addAttribute("error", result.ErrorMessage);
        }
        return "pokemon";
    }


    public Usuario obtenerUsuarioLogueado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Result resultUserActivo = usuarioService.getByUsername(name);
        return (Usuario) resultUserActivo.Object;
    }

    public List<String> FavoritosPokemonId(int idUsuario) {
        Result resultFavoritos = favoritoService.getAllByUsuario(idUsuario);
        List<String> favoritosPokemonIds = new ArrayList<>();
        if (resultFavoritos.Correct) {
            List<Favorito> favoritos = (List<Favorito>) resultFavoritos.Object;
            favoritosPokemonIds = favoritos.stream()
                    .map(Favorito::getPokemon)
                    .collect(Collectors.toList());
        }
        return favoritosPokemonIds;
    }

    public Pokemon obtenerPokemon(String idOrName) {
        Result result = pokemonService.getPokemonByIdOrName(idOrName);
        return (Pokemon) result.Object;
    }

    public List<String> obtenerColoresTipos(Pokemon pokemon) {
        return pokemon.getTypes()
                .stream()
                .map(t -> obtenerColorPorTipo(t.getType().getName()))
                .toList();
    }

    public String obtenerTipo(Pokemon pokemon) {
        List<String> colores = pokemon.getTypes()
                .stream()
                .map(t -> obtenerColorPorTipo(t.getType().getName()))
                .toList();
        if (colores.size() == 1) return colores.get(0);
        return "linear-gradient(135deg, " + colores.get(0) + ", " + colores.get(1) + ")";
    }

    public String obtenerColorPorTipo(String tipo) {
        switch (tipo.toLowerCase()) {
            case "fire":     return "#F08030";
            case "water":    return "#6890F0";
            case "grass":    return "#78C850";
            case "electric": return "#F8D030";
            case "ice":      return "#98D8D8";
            case "fighting": return "#C03028";
            case "poison":   return "#A040A0";
            case "ground":   return "#E0C068";
            case "flying":   return "#A890F0";
            case "psychic":  return "#F85888";
            case "bug":      return "#A8B820";
            case "rock":     return "#B8A038";
            case "ghost":    return "#705898";
            case "dragon":   return "#7038F8";
            case "steel":    return "#B8B8D0";
            case "fairy":    return "#EE99AC";
            case "dark":     return "#705848";
            case "normal":   return "#A8A878";
            default:         return "#A8A77A";
        }
    }
}
