package ProgramacionNCapasNoviembre25.PokeAPI.DAO;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Favorito;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface IFavoritos extends JpaRepository<Favorito, Integer>{
    @Query("SELECT f FROM Favorito f WHERE f.usuario.idUsuario = :idUsuario")
    List<Favorito> findByUsuarioIdUsuario(@Param("idUsuario") Integer idUsuario);

    @Query("SELECT f FROM Favorito f JOIN FETCH f.usuario WHERE f.usuario.idUsuario = :idUsuario")
    List<Favorito> findByUsuarioIdFetchUsuario(@Param("idUsuario") Integer idUsuario);
    
    List<Favorito> findByUsuarioIdUsuarioAndPokemon(Integer idUsuario, String pokemon);

    @Modifying
    @Query("DELETE FROM Favorito f WHERE f.usuario.idUsuario = :idUsuario")
    void deleteByUsuarioIdUsuario(@Param("idUsuario") Integer idUsuario);

    Long countByUsuarioIdUsuario(Integer idUsuario);

}
