package ProgramacionNCapasNoviembre25.PokeAPI.DAO;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface IUsuario extends JpaRepository<Usuario, Integer>{
    
    @Query("SELECT u FROM Usuario u WHERE " +
       "(:username = '' OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
       "(:correo = '' OR LOWER(u.correo) LIKE LOWER(CONCAT('%', :correo, '%')))")
    List<Usuario> buscarPorUsernameOCorreo(
        @Param("username") String username,
        @Param("correo") String correo);
    
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByToken(String token);
    List<Usuario> findAllByOrderByIdUsuarioAsc();
    Optional<Usuario> findByUsername(String username);
}
