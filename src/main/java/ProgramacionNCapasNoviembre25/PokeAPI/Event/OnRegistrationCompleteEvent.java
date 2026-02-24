package ProgramacionNCapasNoviembre25.PokeAPI.Event;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import org.springframework.context.ApplicationEvent;


public class OnRegistrationCompleteEvent extends ApplicationEvent{
    
    private final Usuario usuario;
    
    public OnRegistrationCompleteEvent(Usuario usuario){
    super(usuario);
    this.usuario=usuario;
    }
    
    public Usuario getUsuario(){
    return usuario;
    }
    
    

}
