package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IUsuario;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificacionTokenService {

    @Autowired
    private IUsuario usuarioRepository;

    public void crearVerificacionToken(Usuario usuario, String token) {
        usuario.setToken(token);
        usuarioRepository.save(usuario);
    }
    
    public String validarVerificacionToken(String token){
    Usuario user= usuarioRepository.findByToken(token).orElse(null);
        if (user==null) {
            return "invalido";            
        }
        user.setEstatus(1);
        usuarioRepository.save(user);
        return "valid";
    }

}
