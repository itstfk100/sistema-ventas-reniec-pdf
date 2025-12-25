package org.cibertec.proyectof.controllers;

import org.cibertec.proyectof.entities.Producto;
import org.cibertec.proyectof.repositories.ProductoRepository;
import org.cibertec.proyectof.repositories.CategoriaRepository;
import org.cibertec.proyectof.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private CategoriaRepository categoriaRepo;

    @Autowired
    private ProveedorRepository proveedorRepo;

    @GetMapping("/productos")
    public String mostrarProductos(@RequestParam(required = false) Integer id,
                                   @RequestParam(required = false) String nombre,
                                   @RequestParam(required = false) Integer categoriaId,
                                   Model model) {

        List<Producto> productos;

        if (id != null) {
            productos = productoRepo.findById(id)
                    .filter(Producto::getActivo)
                    .map(List::of)
                    .orElse(List.of());

        } else if (nombre != null && !nombre.isEmpty()) {
            productos = productoRepo
                    .findByNombreContainingIgnoreCaseAndActivoTrue(nombre);

        } else if (categoriaId != null) {
            productos = productoRepo
                    .findByCategoria_IdAndActivoTrue(categoriaId);

        } else {
            productos = productoRepo.findByActivoTrue();
        }

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categoriaRepo.findAll());
        model.addAttribute("proveedores", proveedorRepo.findAll());
        return "productos";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        producto.setActivo(true);
        productoRepo.save(producto);
        return "redirect:/productos";
    }

    @PostMapping("/productos/editar")
    public String editarProducto(@ModelAttribute Producto producto) {
        productoRepo.save(producto);
        return "redirect:/productos";
    }

    // ✅ ELIMINACIÓN LÓGICA
    @PostMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Integer id) {

        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setActivo(false);
        productoRepo.save(producto);

        return "redirect:/productos";
    }
}

