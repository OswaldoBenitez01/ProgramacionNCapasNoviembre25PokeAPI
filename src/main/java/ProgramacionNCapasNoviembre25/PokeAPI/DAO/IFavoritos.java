package ProgramacionNCapasNoviembre25.PokeAPI.DAO;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IFavoritos extends JpaRepository<Favorito, Integer>{

}
