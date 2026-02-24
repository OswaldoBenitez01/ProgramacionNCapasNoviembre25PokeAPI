package ProgramacionNCapasNoviembre25.PokeAPI.Event;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.EmailService;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.VerificacionTokenService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificacionTokenService tokenService;
        
    @Autowired
    private EmailService emailService;
    
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        try {
        Usuario user = event.getUsuario();
        String token = UUID.randomUUID().toString();
        tokenService.crearVerificacionToken(user, token);

        String recipientAddress = user.getCorreo();
        String subject = "Verificación de Cuenta - PokeAPI";
        String confirmationUrl = "http://localhost:8080/verificar-email?token=" + token;
        String message = "Bienvenido a PokeAPI. Haz clic aquí para activar tu cuenta: " + confirmationUrl;

        emailService.enviarCorreo(recipientAddress, subject, message);
    } catch (Exception e) {
        
        System.err.println("Error enviando el correo: " + e.getMessage());
    }
    }

}
