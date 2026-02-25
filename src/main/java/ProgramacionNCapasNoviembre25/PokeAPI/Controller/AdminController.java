package ProgramacionNCapasNoviembre25.PokeAPI.Controller;

import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Usuario;
import ProgramacionNCapasNoviembre25.PokeAPI.JPA.Favorito;
import ProgramacionNCapasNoviembre25.PokeAPI.ML.Result;
import ProgramacionNCapasNoviembre25.PokeAPI.Service.AdminService;
import java.util.Map;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired private AdminService adminService;
    
    // ===================== VISTAS =====================
    @GetMapping("/admin")
    public String adminPanel(Model model) {
        Result result = adminService.GetAll();
        model.addAttribute("usuarios", result.Objects != null ? result.Objects : new ArrayList<>());
        return "usuarios";
    }
    
    // ===================== USUARIOS API =====================
    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getAll() {
        Result result = adminService.GetAll();
        return result.Correct ? ResponseEntity.ok(result.Objects) : ResponseEntity.badRequest().body(result.ErrorMessage);
    }
    
    @GetMapping("/roles")
    @ResponseBody
    public ResponseEntity<?> getRoles() {
        Result result = adminService.GetAllRoles();
        return result.Correct ? 
            ResponseEntity.ok(result.Objects) : 
            ResponseEntity.badRequest().body(result.ErrorMessage);
    }

    
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Result result = adminService.GetById(id);
        return result.Correct ? ResponseEntity.ok(result.Object) : ResponseEntity.badRequest().body(result.ErrorMessage);
    }
    
    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<?> buscarUsuarios(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String correo) {

        Result result = adminService.BuscarPorUsernameOCorreo(username, correo);
        return result.Correct ? 
            ResponseEntity.ok(result.Objects) : 
            ResponseEntity.badRequest().body(result.ErrorMessage);
    }

    
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody Usuario usuario) {
        Result result = adminService.Add(usuario);
        return result.Correct ? ResponseEntity.ok(result.Object) : ResponseEntity.badRequest().body(result.ErrorMessage);
    }
    
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Usuario usuario) {
        usuario.setIdUsuario(id);
        Result result = adminService.UpdateUser(usuario);
        return result.Correct ? ResponseEntity.ok("Actualizado") : ResponseEntity.badRequest().body(result.ErrorMessage);
    }
    
    @PatchMapping("/{id}/estatus/{estatus}")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @PathVariable Integer estatus) {
        Result result = adminService.UpdateEstatus(id, estatus);
        return result.Correct ? ResponseEntity.ok("Estatus actualizado") : ResponseEntity.badRequest().body(result.ErrorMessage);
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Result result = adminService.DeleteById(id);
        return result.Correct ? ResponseEntity.ok("Eliminado") : ResponseEntity.badRequest().body(result.ErrorMessage);
    }
    
    // ===================== FAVORITOS API =====================
    @GetMapping("/{idUsuario}/favoritos")
    @ResponseBody
    public ResponseEntity<?> getFavoritos(@PathVariable Integer idUsuario) {
        Result result = adminService.GetFavoritosConDetalle(idUsuario);
        return result.Correct ? 
            ResponseEntity.ok(result.Objects) : 
            ResponseEntity.badRequest().body(result.ErrorMessage);
    }
  
    @PostMapping("/favoritos")
    @ResponseBody
    public ResponseEntity<?> addFavorito(@RequestBody Favorito favorito) {
        Result result = adminService.AddFavorito(favorito);
        return result.Correct ? ResponseEntity.ok(result.Object) : ResponseEntity.badRequest().body(result.ErrorMessage);
    }
    
    @PutMapping("/favoritos/{idFavorito}/pokemon")
    @ResponseBody
    public ResponseEntity<?> updateFavorito(@PathVariable Integer idFavorito, @RequestBody Map<String, String> data) {
        Result result = adminService.UpdateFavoritoPokemon(idFavorito, data.get("pokemon"));
        return result.Correct ? ResponseEntity.ok(result.Object) : ResponseEntity.badRequest().body(result.ErrorMessage);
    }
    
    @DeleteMapping("/favoritos/{idFavorito}")
    @ResponseBody
    public ResponseEntity<?> deleteFavorito(@PathVariable Integer idFavorito) {
        Result result = adminService.DeleteFavorito(idFavorito);
        return result.Correct ? ResponseEntity.ok("Eliminado") : ResponseEntity.badRequest().body(result.ErrorMessage);
    }
}
