package org.cibertec.proyectof.controllers;

import org.cibertec.proyectof.entities.Proveedor;
import org.cibertec.proyectof.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepo;

    @GetMapping
    public String mostrarProveedores(Model model) {
        model.addAttribute("proveedores", proveedorRepo.findAll());
        return "gestionProveedores";
    }

    // Agregar un nuevo proveedor
    @PostMapping("/nuevo")
    public String registrarProveedor(@RequestParam String nombre,
                                     @RequestParam String ruc,
                                     @RequestParam String direccion,
                                     @RequestParam String contacto,
                                     @RequestParam String email) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(nombre);
        proveedor.setRuc(ruc);
        proveedor.setDireccion(direccion);
        proveedor.setContacto(contacto);
        proveedor.setEmail(email);
        proveedorRepo.save(proveedor);
        return "redirect:/proveedores";
    }

    // Editar proveedor
    @PostMapping("/editar/{id}")
    public String editarProveedor(@PathVariable Integer id,
                                  @RequestParam String nombre,
                                  @RequestParam String ruc,
                                  @RequestParam String direccion,
                                  @RequestParam String contacto,
                                  @RequestParam String email) {
        Proveedor proveedor = proveedorRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("ID de proveedor inválido:" + id));
        proveedor.setNombre(nombre);
        proveedor.setRuc(ruc);
        proveedor.setDireccion(direccion);
        proveedor.setContacto(contacto);
        proveedor.setEmail(email);
        proveedorRepo.save(proveedor);
        return "redirect:/proveedores";
    }

    // Eliminar proveedor
    @PostMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Integer id) {
        proveedorRepo.deleteById(id);
        return "redirect:/proveedores";
    }
}