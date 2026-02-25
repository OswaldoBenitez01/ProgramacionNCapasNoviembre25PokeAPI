package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IUsuario;
import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IRol;
import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IFavoritos;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.*;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Pokemon;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired private IUsuario usuarioRepository;
    @Autowired private IRol rolRepository;
    @Autowired private IFavoritos favoritoRepository;
    @Autowired private PokemonService pokemonService;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // ===================== USUARIOS =====================
    public Result GetAll() {
        Result result = new Result();
        try {
            List<Usuario> usuarios = usuarioRepository.findAllByOrderByIdUsuarioAsc();
            result.Objects = new ArrayList<>(usuarios);
            result.Correct = true;
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }
    
    public Result GetById(Integer idUsuario) {
        Result result = new Result();
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
            result.Object = usuario.orElse(null);
            result.Correct = usuario.isPresent();
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }
    
    public Result BuscarPorUsernameOCorreo(String username, String correo) {
        Result result = new Result();
        try {
            List<Usuario> usuarios = usuarioRepository.buscarPorUsernameOCorreo(
                username != null ? username : "",
                correo != null ? correo : ""
            );
            result.Objects = new ArrayList<>(usuarios);
            result.Correct = true;
        } catch (Exception ex) {
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }


    
    public Result Add(Usuario usuario) {
        Result result = new Result();
        try {
            usuario.setIdUsuario(null);
            
            if (usuario.getRol() != null && usuario.getRol().getIdRol() != null) {
                usuario.setRol(rolRepository.findById(usuario.getRol().getIdRol()).orElse(null));
            }

            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            
            usuarioRepository.save(usuario);
            result.Object = usuario;
            result.Correct = true;
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }
    
    public Result UpdateUser(Usuario usuario) {
        Result result = new Result();
        try {
            if (usuario.getIdUsuario() == null) {
                result.ErrorMessage = "ID requerido";
                return result;
            }
            
            Optional<Usuario> usuarioDB = usuarioRepository.findById(usuario.getIdUsuario());
            if (!usuarioDB.isPresent()) {
                result.ErrorMessage = "Usuario no encontrado";
                return result;
            }
            
            Usuario db = usuarioDB.get();
            db.setNombre(usuario.getNombre());
            db.setApellidoPaterno(usuario.getApellidoPaterno());
            db.setApellidoMaterno(usuario.getApellidoMaterno());
            db.setUsername(usuario.getUsername());
            db.setCorreo(usuario.getCorreo());
            if (usuario.getRol() != null) {
                db.setRol(rolRepository.findById(usuario.getRol().getIdRol()).orElse(db.getRol()));
            }
            
            usuarioRepository.save(db);
            result.Correct = true;
        } catch (Exception ex) {
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }
    
    public Result UpdateEstatus(Integer idUsuario, Integer estatus) {
        Result result = new Result();
        try {
            Optional<Usuario> usuarioDB = usuarioRepository.findById(idUsuario);
            if (usuarioDB.isPresent()) {
                usuarioDB.get().setEstatus(estatus);
                usuarioRepository.save(usuarioDB.get());
                result.Correct = true;
            } else {
                result.ErrorMessage = "Usuario no encontrado";
            }
        } catch (Exception ex) {
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }
    
    public Result DeleteById(Integer idUsuario) {
        Result result = new Result();
        try {
            if (usuarioRepository.existsById(idUsuario)) {
                usuarioRepository.deleteById(idUsuario);
                result.Correct = true;
            } else {
                result.ErrorMessage = "Usuario no encontrado";
            }
        } catch (Exception ex) {
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }
    
    // ===================== FAVORITOS =====================
    public Result GetFavoritosByUsuario(Integer idUsuario) {
        Result result = new Result();
        try {
            List<Favorito> favoritos = favoritoRepository.findByUsuarioIdFetchUsuario(idUsuario);
            result.Objects = new ArrayList<>(favoritos);
            result.Correct = true;
        } catch (Exception ex) {
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }
    
    public Result GetFavoritosConDetalle(Integer idUsuario) {
        Result result = new Result();

        try {
            Result favoritosResult = GetFavoritosByUsuario(idUsuario);

            if (!favoritosResult.Correct) {
                result.Correct = false;
                result.ErrorMessage = favoritosResult.ErrorMessage;
                return result;
            }

            List<Favorito> favoritos = new ArrayList<>();
            if (favoritosResult.Objects != null) {
                for (Object obj : favoritosResult.Objects) {
                    if (obj instanceof Favorito) {
                        favoritos.add((Favorito) obj);
                    }
                }
            }

            List<Object> favoritosConDetalle = new ArrayList<>();
            for (Favorito fav : favoritos) {
                String pokemonId = fav.getPokemon();

                Result pokemonResult = pokemonService.getPokemonByIdOrName(pokemonId);

                if (pokemonResult.Correct) {
                    Pokemon pokemon = (Pokemon) pokemonResult.Object;

                    Map<String, Object> combo = new HashMap<>();
                    combo.put("favorito", fav);
                    combo.put("pokemon", pokemon);

                    favoritosConDetalle.add(combo);
                }
            }

            result.Correct = true;
            result.Objects = favoritosConDetalle;

        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = "Error: " + ex.getMessage();
            result.ex = ex;
        }

        return result;
    }

    public Result AddFavorito(Favorito favorito) {
        Result result = new Result();
        try {
            if (!usuarioRepository.existsById(favorito.getUsuario().getIdUsuario())) {
                result.ErrorMessage = "Usuario no existe";
                return result;
            }
            
            favoritoRepository.save(favorito);
            result.Object = favorito;
            result.Correct = true;
        } catch (Exception ex) {
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }
    
    public Result DeleteFavorito(Integer idFavorito) {
        Result result = new Result();
        try {
            if (favoritoRepository.existsById(idFavorito)) {
                favoritoRepository.deleteById(idFavorito);
                result.Correct = true;
            } else {
                result.ErrorMessage = "Favorito no encontrado";
            }
        } catch (Exception ex) {
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }
    
    public Result UpdateFavoritoPokemon(Integer idFavorito, String pokemon) {
        Result result = new Result();
        try {
            Optional<Favorito> favoritoDB = favoritoRepository.findById(idFavorito);
            if (favoritoDB.isPresent()) {
                favoritoDB.get().setPokemon(pokemon);
                favoritoRepository.save(favoritoDB.get());
                result.Object = favoritoDB.get();
                result.Correct = true;
            } else {
                result.ErrorMessage = "Favorito no encontrado";
            }
        } catch (Exception ex) {
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }
    
    public Result GetAllRoles() {
        Result result = new Result();
        try {
            List<Rol> roles = rolRepository.findAll();
            result.Objects = new ArrayList<>(roles);
            result.Correct = true;
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getMessage();
        }
        return result;
    }

}
