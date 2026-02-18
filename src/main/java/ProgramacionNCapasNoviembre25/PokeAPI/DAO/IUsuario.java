package ProgramacionNCapasNoviembre25.PokeAPI.DAO;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IUsuario extends JpaRepository<Usuario, Integer>{
    
}
