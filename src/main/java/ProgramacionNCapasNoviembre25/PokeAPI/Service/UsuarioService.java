package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IUsuario;
import ProgramacionNCapasNoviembre25.PokeAPI.DTO.UsuarioRegistroDTO;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Rol;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private IUsuario usuarioRepository;

    @Autowired
    private EmailService emailService;

    public Usuario registerNewUserAccount(UsuarioRegistroDTO userDto) {
        Usuario user = new Usuario();
        user.setNombre(userDto.getNombre());
        user.setApellidoPaterno(userDto.getApellidoPaterno());
        user.setApellidoMaterno(userDto.getApellidoMaterno());
        user.setUsername(userDto.getUsername());
        user.setCorreo(userDto.getCorreo());
        user.setContrasena(new BCryptPasswordEncoder().encode(userDto.getContrasena()));
        user.setEstatus(0);
        Rol rol = new Rol();
        rol.setIdRol(2);

        user.setRol(rol);

        usuarioRepository.save(user);

        String token = UUID.randomUUID().toString();
        user.setToken(token);
        usuarioRepository.save(user);
//
//        String confirmationUrl = "http://localhost:8080/verificar-email?token=" + token;
//        emailService.enviarCorreo(user.getCorreo(), "Email Verification", "Click the link to verify your email: " + confirmationUrl);

        return user;
    }

    public String validateVerificationToken(String token) {
        Usuario user = usuarioRepository.findByToken(token).orElse(null);
        if (user == null) {
            return "invalid";
        }

        user.setEstatus(1);
        usuarioRepository.save(user);
        return "valid";
    }
}
