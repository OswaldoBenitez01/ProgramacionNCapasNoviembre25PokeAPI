package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IUsuario;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.IUsuarioJPARepository;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsJPAService implements UserDetailsService {

    private IUsuarioJPARepository iUsuarioJPARepository;

    public UserDetailsJPAService(IUsuarioJPARepository iUsuarioJPARepository) {
        this.iUsuarioJPARepository = iUsuarioJPARepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = iUsuarioJPARepository.findByCorreo(correo);


        return User.withUsername(usuario.getUsername())
                .password(usuario.getContrasena())
                .roles(usuario.rol.getNombre())                
                .build();
    }

}
