package ProgramacionNCapasNoviembre25.PokeAPI.Controller;

import ProgramacionNCapasNoviembre25.PokeAPI.DTO.UsuarioRegistroDTO;
import ProgramacionNCapasNoviembre25.PokeAPI.Event.OnRegistrationCompleteEvent;
import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IUsuarioJPARepository;
import ProgramacionNCapasNoviembre25.PokeAPI.Event.OnPasswordResetRequestEvent;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO usuarioDTO,
            RedirectAttributes redirectAttributes, Model model) {
        Usuario correoRegistrado = iUsuarioJPARepository.findByCorreo(usuarioDTO.getCorreo());
        if (correoRegistrado != null) {
            Result roles = RolService.getAll();
            model.addAttribute("Roles", roles.Objects);
            model.addAttribute("errorCorreo", "Este correo electrónico ya está vinculado a una cuenta.");
            model.addAttribute("usuario", usuarioDTO);
            return "Registro";
        }
        if (usuarioDTO.getCorreo() == null) {
            Result roles = RolService.getAll();
            model.addAttribute("Roles", roles.Objects);
            model.addAttribute("usuario", new UsuarioRegistroDTO());
            return "Registro";
        }
        Usuario registro = usuarioService.registerNewUserAccount(usuarioDTO);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registro));

        redirectAttributes.addAttribute("registered", "true");
        redirectAttributes.addAttribute("user", usuarioDTO.getCorreo());
        return "redirect:/login";
    }

    @GetMapping("/verificar-email")
    public String verificarEmail(@RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "redirectTo", defaultValue = "login") String redirectTo,
            RedirectAttributes redirectAttributes, Model model) {
        String result = usuarioService.validateVerificationToken(token);
        if (result.equals("valid")) {
            redirectAttributes.addAttribute("verified", "true");
            return "redirect:/" + redirectTo;
        } else {
            model.addAttribute("message", "token no valido");
            return "Verificar-email";
        }
    }

    @GetMapping("/recuperacion")
    public String enviarCorreoRecupecion() {
        return "recuperacionCuenta";
    }

    @PostMapping("/recuperacion")
    public String enviarCorreoRecupecion(@RequestParam("email") String email,
            RedirectAttributes redirectAttributes) {
        Usuario usuario = iUsuarioJPARepository.findByCorreo(email);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "El correo no está registrado en la base.");
            return "redirect:/recuperacion";
        }

        eventPublisher.publishEvent(new OnPasswordResetRequestEvent(usuario));

        redirectAttributes.addFlashAttribute("success", "¡Enlace enviado! Revisa tu bandeja de entrada.");
        return "redirect:/recuperacion";
    }

    @GetMapping("/reset-password")
    public String mostrarFormularioNuevaClave(@RequestParam("token") String token, Model model) {

        String resultado = usuarioService.validateVerificationToken(token);

        if (resultado.equals("valid")) {
            model.addAttribute("token", token);
            return "verificar";
        } else {

            return "redirect:/login?errorToken=true";
        }
    }

    @PostMapping("/reset-password")
    public String actualizarContra(@RequestParam("token") String token,
            @RequestParam("password") String password,
            RedirectAttributes redirectAttributes) {

        
        Result result = usuarioService.updatePasswordByToken(token, password);

        if (result.Correct) {
            
            redirectAttributes.addFlashAttribute("success", "¡Tu contraseña ha sido actualizada con éxito, Entrenador!");
            return "redirect:/login";
        } else {
            // 3. Si hubo error (token expirado o inválido), lo regresamos al formulario con error
            return "redirect:/reset-password?token=" + token + "&error=true";
        }
    }

}
