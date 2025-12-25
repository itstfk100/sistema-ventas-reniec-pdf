package org.cibertec.proyectof.controllers;

import org.cibertec.proyectof.entities.Usuario;
import org.cibertec.proyectof.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping
    public String mostrarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepo.findAll());
        return "gestionUsuarios";
    }

    @PostMapping("/nuevo")
    public String registrarUsuario(@RequestParam String nombre,
                                   @RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String email,
                                   @RequestParam String rol) {
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setEmail(email);
        usuario.setRol(rol);
        usuarioRepo.save(usuario);
        return "redirect:/usuarios";
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Integer id,
                                @RequestParam String nombre,
                                @RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String rol) {
        Usuario usuario = usuarioRepo.findById(id).orElseThrow();
        usuario.setNombre(nombre);
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setRol(rol);
        usuarioRepo.save(usuario);
        return "redirect:/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        usuarioRepo.deleteById(id);
        return "redirect:/usuarios";
    }
}
