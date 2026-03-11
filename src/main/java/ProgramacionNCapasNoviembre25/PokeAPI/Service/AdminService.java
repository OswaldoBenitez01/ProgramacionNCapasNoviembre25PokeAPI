package ProgramacionNCapasNoviembre25.PokeAPI.Service;

import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IUsuario;
import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IRol;
import ProgramacionNCapasNoviembre25.PokeAPI.DAO.IFavoritos;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.*;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired private IUsuario usuarioRepository;
    @Autowired private IRol rolRepository;
    @Autowired private IFavoritos favoritoRepository;
    @Autowired private PokemonService pokemonService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final ExecutorService poolDeHilos = Executors.newFixedThreadPool(50);

    private final ConcurrentHashMap<Integer, List<Object>> cachePrefetch = new ConcurrentHashMap<>();

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
            Usuario usuarioEnBD = usuarioDB.get();
            usuarioEnBD.setNombre(usuario.getNombre());
            usuarioEnBD.setApellidoPaterno(usuario.getApellidoPaterno());
            usuarioEnBD.setApellidoMaterno(usuario.getApellidoMaterno());
            usuarioEnBD.setUsername(usuario.getUsername());
            usuarioEnBD.setCorreo(usuario.getCorreo());
            if (usuario.getRol() != null) {
                usuarioEnBD.setRol(rolRepository.findById(usuario.getRol().getIdRol()).orElse(usuarioEnBD.getRol()));
            }
            usuarioRepository.save(usuarioEnBD);
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

    private CompletableFuture<Map<String, Object>> consultarPokemonAsync(Favorito favorito) {
        return CompletableFuture.supplyAsync(() -> {
            Result pokemonResult = pokemonService.getPokemonByIdOrName(favorito.getPokemon());
            Map<String, Object> entrada = new HashMap<>();
            entrada.put("favorito", favorito);
            entrada.put("pokemon", pokemonResult.Correct ? pokemonResult.Object : null);
            return entrada;
        }, poolDeHilos).orTimeout(3, TimeUnit.SECONDS).exceptionally(ex -> {
            Map<String, Object> entradaVacia = new HashMap<>();
            entradaVacia.put("favorito", favorito);
            entradaVacia.put("pokemon", null);
            return entradaVacia;
        });
    }

    private List<Object> consultarFavoritosEnParalelo(List<Favorito> listaFavoritos) throws Exception {
        List<CompletableFuture<Map<String, Object>>> futuros = listaFavoritos.stream()
            .map(this::consultarPokemonAsync)
            .collect(Collectors.toList());

        CompletableFuture.allOf(futuros.toArray(new CompletableFuture[0])).join();

        return futuros.stream()
            .map(CompletableFuture::join)
            .filter(entrada -> entrada.get("pokemon") != null)
            .collect(Collectors.toList());
    }

    private void prefetchPaginaSiguiente(Integer idUsuario, List<Favorito> todosLosFavoritos, int paginaSiguiente, int tamanio) {
        int indiceInicio = paginaSiguiente * tamanio;
        if (indiceInicio >= todosLosFavoritos.size()) return;

        int indiceFin = Math.min(indiceInicio + tamanio, todosLosFavoritos.size());
        List<Favorito> favoritosSiguientePagina = todosLosFavoritos.subList(indiceInicio, indiceFin);

        int claveCache = (idUsuario * 10000) + paginaSiguiente;

        CompletableFuture.runAsync(() -> {
            try {
                List<Object> resultados = consultarFavoritosEnParalelo(favoritosSiguientePagina);
                cachePrefetch.put(claveCache, resultados);
            } catch (Exception ignored) {}
        }, poolDeHilos);
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
            List<Favorito> listaFavoritos = favoritosResult.Objects.stream()
                .filter(o -> o instanceof Favorito)
                .map(o -> (Favorito) o)
                .collect(Collectors.toList());

            result.Objects = consultarFavoritosEnParalelo(listaFavoritos);
            result.Correct = true;
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = "Error: " + ex.getMessage();
            result.ex = ex;
        }
        return result;
    }

    public Result GetFavoritosConDetallePaginado(Integer idUsuario, int numeroDePagina, int tamanio) {
        Result result = new Result();
        try {
            Result favoritosResult = GetFavoritosByUsuario(idUsuario);
            if (!favoritosResult.Correct) {
                result.Correct = false;
                result.ErrorMessage = favoritosResult.ErrorMessage;
                return result;
            }

            List<Favorito> todosLosFavoritos = favoritosResult.Objects.stream()
                .filter(o -> o instanceof Favorito)
                .map(o -> (Favorito) o)
                .collect(Collectors.toList());

            int indiceInicio = numeroDePagina * tamanio;
            int indiceFin    = Math.min(indiceInicio + tamanio, todosLosFavoritos.size());

            if (indiceInicio >= todosLosFavoritos.size()) {
                result.Correct = true;
                result.Objects = new ArrayList<>();
                result.Object  = todosLosFavoritos.size();
                return result;
            }

            int claveCache = (idUsuario * 10000) + numeroDePagina;
            List<Object> favoritosConDetalle;

            if (cachePrefetch.containsKey(claveCache)) {
                favoritosConDetalle = cachePrefetch.remove(claveCache);
            } else {
                List<Favorito> favoritosDeLaPagina = todosLosFavoritos.subList(indiceInicio, indiceFin);
                favoritosConDetalle = consultarFavoritosEnParalelo(favoritosDeLaPagina);
            }

            prefetchPaginaSiguiente(idUsuario, todosLosFavoritos, numeroDePagina + 1, tamanio);

            result.Correct = true;
            result.Objects = favoritosConDetalle;
            result.Object  = todosLosFavoritos.size();

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
            result.Object  = favorito;
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
                result.Object  = favoritoDB.get();
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
