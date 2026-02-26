package ProgramacionNCapasNoviembre25.PokeAPI.Controller;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Favorito;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/favorito")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Favorito favorito) {
        Result result = favoritoService.addFavorito(favorito);
        if (result.Correct) {
            result.Object = "Se añadio favorito de forma correcta";
        } else {
            result.Object = "Error";
        }
        return result;
    }

    @PostMapping("/getById/{idFavorito}")
    @ResponseBody
    public Result getbyid(@PathVariable("idFavorito") int idFavorito) {
        Result result = favoritoService.getById(idFavorito);
        if (!result.Correct) {
            result.Object = "Error";
        }
        return result;
    }

    @PostMapping("/getById/{idUsuario}/{idPokemon}")
    @ResponseBody
    public Result getbyidUsuarioPokemon(@PathVariable("idUsuario") int idUsuario,@PathVariable("idPokemon") int idPokemon) {
        Result result = favoritoService.getByUsuarioAndPokemon(idUsuario,idPokemon);
        if (!result.Correct) {
            result.Object = "Error";
        }
        return result;
    }

    @PostMapping("/{idUsuario}")
    @ResponseBody
    public Result getallbyid(@PathVariable("idUsuario") int idUsuario) {
        Result result = favoritoService.getAllByUsuario(idUsuario);
        if (!result.Correct) {
            result.Object = "Error";
        }
        return result;
    }

    @DeleteMapping("/delete/{idFavorito}")
    @ResponseBody
    public Result delete(@PathVariable("idFavorito") int idFavorito) {
        Result result = favoritoService.deleteFavorito(idFavorito);

        if (result.Correct) {
            result.Object = "El registro " + idFavorito + " se eliminó de forma correcta";
        } else {
            result.Object = "No fue posible eliminar";
        }

        return result;
    }
}
