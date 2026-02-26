package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IFavoritos;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Favorito;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoritoService {

    @Autowired
    private IFavoritos iFavoritos;

    public Result getById(Integer idFavorito) {
        Result result = new Result();
        try {
            Favorito favorito = iFavoritos.findById(idFavorito).orElse(null);
            if (favorito != null) {
                result.Object = favorito;
                result.Correct = true;
            } else {
                result.Correct = false;
                result.ErrorMessage = "no se encontro un favorito con ese id";
            }
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;

    }

    public Result getByUsuarioAndPokemon(Integer idUsuario, Integer idPokemon) {
        Result result = new Result();
//        Integer idUsuario=usuario.getIdUsuario();
        try {
            
            Favorito favorito = iFavoritos.findByUsuarioYPokemon(idUsuario, idPokemon).orElse(null);

            if (favorito != null) {
                result.Object = favorito;
                result.Correct = true;
            } else {
                result.Correct = false;
                result.ErrorMessage = "No se encontró el Pokémon en los favoritos de este usuario.";
            }
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    public Result getAllByUsuario(Integer idUsuario) {
        Result result = new Result();
        try {
            List<Favorito> favoritos = iFavoritos.findByUsuarioIdUsuario(idUsuario);
            favoritos.sort(Comparator.comparing(Favorito::getPokemon));
            if (favoritos != null && !favoritos.isEmpty()) {
                result.Object = favoritos;
                result.Correct = true;
            } else {
                result.Correct = false;
                result.Object = new ArrayList<>();
                result.ErrorMessage = "El usuario no tiene favoritos registrados.";
            }
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    public Result addFavorito(Favorito favorito) {
        Result result = new Result();
        try {
            if (favorito != null) {
                iFavoritos.save(favorito);
                result.Correct = true;
            }

        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;

    }

    public Result deleteFavorito(Integer idFavorito) {
        Result result = new Result();
        try {
            Favorito favorito = iFavoritos.findById(idFavorito).orElse(null);
            if (favorito != null) {
                iFavoritos.deleteById(idFavorito);
                result.Correct = true;
            } else {
                result.Correct = false;
                result.ErrorMessage = "no se encontro un favorito con ese id";
            }
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

}
