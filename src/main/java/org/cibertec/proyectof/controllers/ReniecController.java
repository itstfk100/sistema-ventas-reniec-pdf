package org.cibertec.proyectof.controllers;

import org.cibertec.proyectof.dtos.ReniecResponse;
import org.cibertec.proyectof.services.ReniecService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reniec")
public class ReniecController {

    private final ReniecService reniecService;

    public ReniecController(ReniecService reniecService) {
        this.reniecService = reniecService;
    }

    @GetMapping("/{dni}")
    public ReniecResponse buscarPorDni(@PathVariable String dni) {
        return reniecService.consultarDni(dni);
    }
}
