package org.cibertec.proyectof.controllers;

import org.cibertec.proyectof.entities.Producto;
import org.cibertec.proyectof.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoApiController {

    @Autowired
    private ProductoRepository productoRepo;

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarProductos(
            @RequestParam("query") String query) {

        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(productoRepo.findByActivoTrue());
        }

        return ResponseEntity.ok(productoRepo.buscarActivos(query));
    }
}
