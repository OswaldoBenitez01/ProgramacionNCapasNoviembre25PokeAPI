package ProgramacionNCapasNoviembre25.PokeAPI.Controller;

import ProgramacionNCapasNoviembre25.PokeAPI.DTO.UsuarioRegistroDTO;
import ProgramacionNCapasNoviembre25.PokeAPI.Event.OnRegistrationCompleteEvent;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.IUsuarioJPARepository;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.RolService;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private RolService RolService;

    @Autowired
    private IUsuarioJPARepository iUsuarioJPARepository;

    @GetMapping("/registro")
    public String registroForm(Model model) {
        Result roles = RolService.getAll();
        model.addAttribute("Roles", roles.Objects);
        model.addAttribute("usuario", new UsuarioRegistroDTO());

        return "Registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO usuarioDTO, Model model) {
        Usuario correoRegistrado = iUsuarioJPARepository.findByCorreo(usuarioDTO.getCorreo());
        if (correoRegistrado != null ) {
            Result roles = RolService.getAll();
            model.addAttribute("Roles", roles.Objects);
            model.addAttribute("errorCorreo", "Este correo electrónico ya está vinculado a una cuenta.");
            model.addAttribute("usuario", usuarioDTO);
            return "Registro";
        }      
        if (usuarioDTO.getCorreo()==null) {
             Result roles = RolService.getAll();
            model.addAttribute("Roles", roles.Objects);   
            model.addAttribute("usuario", new UsuarioRegistroDTO());
            return "Registro";
            
        }
        Usuario registro = usuarioService.registerNewUserAccount(usuarioDTO);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registro));
        return "redirect:/login?registered=true";
    }

    @GetMapping("/verificar-email")
    public String verificarEmail(@RequestParam(value = "token", required = false) String token, Model model) {
        String result = usuarioService.validateVerificationToken(token);
        if (result.equals("valid")) {
            model.addAttribute("message", "Se verifico de manera correcta");
            return "redirect:/login?verified=true";
        } else {
            model.addAttribute("message", "token no valido");
            return "Verificar-email";
        }
    }

}
