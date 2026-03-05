package ProgramacionNCapasNoviembre25.PokeAPI.Event;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.EmailService;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.VerificacionTokenService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class PasswordResetListener implements ApplicationListener<OnPasswordResetRequestEvent> {

    @Autowired
    private VerificacionTokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void onApplicationEvent(OnPasswordResetRequestEvent event) {
        try {
            Usuario user = event.getUsuario();
            String token = UUID.randomUUID().toString();
            
            tokenService.crearVerificacionToken(user, token);

            String recipientAddress = user.getCorreo();
            String subject = "Restablecer Contraseña - Pokédex API";

            Context context = new Context();
            context.setVariable("username", user.getNombre());
            context.setVariable("token", token);            
            context.setVariable("resetUrl", "http://localhost:8080/reset-password?token=" + token);

            String htmlContent = templateEngine.process("email-reset-password", context);

            emailService.enviarCorreo(recipientAddress, subject, htmlContent);
        } catch (Exception e) {
            System.err.println("Error en PasswordResetListener: " + e.getMessage());
        }

    }

}
