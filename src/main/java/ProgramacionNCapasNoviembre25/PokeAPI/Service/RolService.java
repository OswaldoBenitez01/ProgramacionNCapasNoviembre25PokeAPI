package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IRol;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Rol;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolService {
    
    @Autowired
    private IRol rolJPARepository;
    
     public Result getAll() {
         Result result=new Result();
        try {
            List<Rol> roles = rolJPARepository.findAll();

             result.Objects = new ArrayList<>();

            for (Rol rol : roles) {
                result.Objects.add(rol);
            }

        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        result.Correct = true;
        return result;
    }

}
