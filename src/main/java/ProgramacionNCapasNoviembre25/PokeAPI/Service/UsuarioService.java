
package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IUsuario;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private IUsuario usuarioRepository;
    
    public Result GetAll() {
        Result result = new Result();
        
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            if (usuarios.isEmpty() || usuarios == null) {
                result.Correct = false;
                result.ErrorMessage = "No se encontraron los usuarios";
                result.Objects = new ArrayList<>();
                return result;
            }
            result.Objects = new ArrayList<>(usuarios);
            result.Correct = true;
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        
        return result;
    }
}
