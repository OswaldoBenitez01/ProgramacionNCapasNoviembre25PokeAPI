
package ProgramacionNCapasNoviembre25.PokeAPI.Controller;

import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.UsuarioService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("usuario")
public class PruebaController {
    @Autowired
    private UsuarioService usuarioService;
//    @Autowired
//    private RolService rolService;
    
    @GetMapping
    public String GetAll(Model model) {
        
        try {

            Result result = usuarioService.GetAll();
            model.addAttribute("Usuarios", result != null ? result.Objects : new ArrayList<>());
//            Result resultRoles = rolService.GetAll();
//            model.addAttribute("Roles", resultRoles != null ? resultRoles.Objects : new ArrayList<>());
        } catch (Exception ex) {
            model.addAttribute("Usuarios", new ArrayList<>());
            model.addAttribute("Roles", new ArrayList<>());
        }
        return "Index";
    }
}
