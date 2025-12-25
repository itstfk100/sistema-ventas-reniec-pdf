    package org.cibertec.proyectof.controllers;

import jakarta.servlet.http.HttpSession;
import org.cibertec.proyectof.entities.Usuario;
import org.cibertec.proyectof.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping("/")
    public String mostrarLogin() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        Usuario usuario = usuarioRepo.findByUsernameAndPassword(username, password);

        if (usuario != null) {
            session.setAttribute("usuario", usuario);

            if ("Empleado".equals(usuario.getRol())) {
                return "redirect:/ventas/registrar";
            }

            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
