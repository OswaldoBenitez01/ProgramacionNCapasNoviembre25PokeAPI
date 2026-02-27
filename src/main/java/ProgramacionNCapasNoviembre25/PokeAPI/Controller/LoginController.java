package ProgramacionNCapasNoviembre25.PokeAPI.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "registered", required = false) String registered,
                        @RequestParam(value = "verified", required = false) String verified,
                        Model model) {
        if ("true".equals(registered)) {
            model.addAttribute("message", "Usuario registrado correctamente, verifica tu bandeja de entrada para verificar tu correo");
        } else if ("true".equals(verified)) {
            model.addAttribute("message", "Email verificado exitosamente, vuelve a iniciar sesion");
        }
        return "login";
    }
    
    @GetMapping("/error")
    public String errorVista() {
        
        return "error";
    }


}
