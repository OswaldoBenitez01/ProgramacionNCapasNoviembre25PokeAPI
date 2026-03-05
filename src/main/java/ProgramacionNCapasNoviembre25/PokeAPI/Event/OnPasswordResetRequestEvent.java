package ProgramacionNCapasNoviembre25.PokeAPI.Event;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import org.springframework.context.ApplicationEvent;

public class OnPasswordResetRequestEvent extends ApplicationEvent {
    private final Usuario usuario;

    public OnPasswordResetRequestEvent(Usuario usuario) {
        super(usuario);
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}