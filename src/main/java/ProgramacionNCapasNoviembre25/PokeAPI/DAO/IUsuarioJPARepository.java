package ProgramacionNCapasNoviembre25.PokeAPI.DAO;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IUsuarioJPARepository extends JpaRepository<Usuario, Integer> {

    @Query("SELECT u FROM Usuario u "
            + "WHERE (:nombre IS NULL OR u.nombre LIKE :nombre_1) "
            + "  AND (:apellidoPaterno IS NULL OR u.apellidoPaterno LIKE :apellidoPaterno_1) \n"
            + "  AND (:apellidoMaterno IS NULL OR u.apellidoMaterno LIKE :apellidoMaterno_1) \n"
            + "  AND (:idRol IS NULL OR u.rol.idRol = :idRol)")

    List<Usuario> busqueda(@Param("nombre") String nombre,
            @Param("apellidoPaterno") String apellidoPaterno,
            @Param("apellidoMaterno") String apellidoMaterno,
            @Param("idRol") Integer idRol);

    public Usuario findByUsername(String username);
    
    public Usuario findByCorreo(String correo);

}
