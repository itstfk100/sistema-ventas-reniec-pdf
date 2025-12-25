package org.cibertec.proyectof.controllers;

import org.cibertec.proyectof.repositories.VentasDetalleRepository;
import org.cibertec.proyectof.dtos.VentaReporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private VentasDetalleRepository ventasDetalleRepository;

    @GetMapping
    public String mostrarReportes(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) String boleta, // El parámetro 'boleta' coincide con el HTML
            Model model
    ) {
        String searchBoleta = (boleta != null && !boleta.isEmpty()) ? "%" + boleta + "%" : null;

        List<VentaReporte> lista = ventasDetalleRepository.buscarReporte(desde, hasta, searchBoleta);

        Map<String, List<VentaReporte>> ventasAgrupadas = lista.stream()
                .collect(Collectors.groupingBy(VentaReporte::getCodigoBoleta));

        double totalGeneral = lista.stream()
                .map(VentaReporte::getSubtotal)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        model.addAttribute("ventasAgrupadas", ventasAgrupadas);
        model.addAttribute("total", totalGeneral);

        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("boleta", boleta);

        return "reportes";
    }
}
