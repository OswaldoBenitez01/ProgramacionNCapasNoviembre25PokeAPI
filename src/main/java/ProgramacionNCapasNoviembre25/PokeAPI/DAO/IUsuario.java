package ProgramacionNCapasNoviembre25.PokeAPI.DAO;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IUsuario extends JpaRepository<Usuario, Integer>{
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByToken(String token);
}
