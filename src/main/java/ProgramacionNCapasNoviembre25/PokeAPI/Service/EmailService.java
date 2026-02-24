package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String destino, String asunto, String contenido) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(destino);
            helper.setSubject(asunto);
            helper.setText(contenido, true);
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando correo HTML", e);
        }
    }

}
