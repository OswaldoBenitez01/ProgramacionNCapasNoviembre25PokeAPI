package ProgramacionNCapasNoviembre25.PokeAPI.Event;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.EmailService;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.VerificacionTokenService;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificacionTokenService tokenService;
            
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private SpringTemplateEngine templateEngine;

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
            String subject = "Verificación de Cuenta - Pokédex API";
            
            Context context = new Context();
            context.setVariable("username", user.getNombre() + " " + user.getApellidoPaterno());
            context.setVariable("token", token);
            context.setVariable("verificationUrl", "http://localhost:8080/verificar-email?token=" + token);

            String htmlContent = templateEngine.process("verificacion", context);
            
            emailService.enviarCorreo(recipientAddress, subject, htmlContent);
        } catch (Exception e) {
            System.err.println("Error enviando el correo: " + e.getMessage());
        }
    }
}
