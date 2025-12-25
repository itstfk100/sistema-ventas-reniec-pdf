package org.cibertec.proyectof.controllers;

import org.cibertec.proyectof.dtos.VentaDTO;
import org.cibertec.proyectof.dtos.VentaDetalleDTO;
import org.cibertec.proyectof.entities.Producto;
import org.cibertec.proyectof.entities.Venta;
import org.cibertec.proyectof.entities.VentaDetalle;
import org.cibertec.proyectof.entities.Usuario;
import org.cibertec.proyectof.repositories.ProductoRepository;
import org.cibertec.proyectof.repositories.VentaRepository;
import org.cibertec.proyectof.repositories.VentasDetalleRepository;
import org.cibertec.proyectof.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import org.cibertec.proyectof.entities.Cliente;
import org.cibertec.proyectof.repositories.ClienteRepository;
import org.cibertec.proyectof.services.PdfGeneratorService;
import org.cibertec.proyectof.services.ReniecService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private static final Logger logger = LoggerFactory.getLogger(VentaController.class); // Instancia del logger
    
    @Autowired
    private ClienteRepository clienteRepo;
    @Autowired
    private ReniecService reniecService;
    @Autowired
    private VentaRepository ventaRepo;
    @Autowired
    private VentasDetalleRepository ventaDetalleRepo;
    @Autowired
    private ProductoRepository productoRepo;
    @Autowired
    private UsuarioRepository usuarioRepo;
    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @GetMapping("/registrar")
    public String mostrarFormularioVenta(Model model) {
        String nextCodigoBoleta = generateNextCodigoBoleta();
        model.addAttribute("codigoBoleta", nextCodigoBoleta);
        return "ventas";
    }

    @PostMapping("/registrar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> registrarVenta(@RequestBody VentaDTO ventaDto) {
    try {
        String dni = ventaDto.getDniCliente();

        if (dni == null || !dni.matches("\\d{8}")) {
            throw new RuntimeException("DNI inválido. Debe contener 8 dígitos.");
        }

        Cliente cliente = clienteRepo.findByDni(dni)
                .orElseGet(() -> {
                    var reniec = reniecService.consultarDni(dni);

                    if (reniec == null) {
                        throw new RuntimeException("No se pudo obtener información desde RENIEC.");
                    }

                    Cliente nuevo = new Cliente();
                    nuevo.setDni(dni);
                    nuevo.setNombreCompleto(reniec.getFull_name());
                    return clienteRepo.save(nuevo);
                });

        Venta venta = new Venta();
        venta.setCodigoBoleta(generateNextCodigoBoleta());
        venta.setFechaVenta(
                ventaDto.getFecha() != null ? ventaDto.getFecha() : LocalDate.now()
        );
        venta.setTotal(ventaDto.getTotal());

        Usuario usuario = usuarioRepo.findById(1)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        venta.setUsuario(usuario);

        venta.setCliente(cliente);

        Venta ventaGuardada = ventaRepo.save(venta);

        for (VentaDetalleDTO detalleDto : ventaDto.getDetalles()) {
            Producto producto = productoRepo.findById(detalleDto.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getStock() < detalleDto.getCantidad()) {
                throw new RuntimeException("Stock insuficiente");
            }

            producto.setStock(producto.getStock() - detalleDto.getCantidad());
            productoRepo.save(producto);

            VentaDetalle detalle = new VentaDetalle();
            detalle.setVenta(ventaGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDto.getCantidad());
            detalle.setPrecioUnitario(detalleDto.getPrecioUnitario());
            detalle.setSubtotal(detalleDto.getSubtotal());

            ventaDetalleRepo.save(detalle);
        }

        return ResponseEntity.ok(Map.of(
                "message", "Venta registrada con éxito",
                "codigoBoleta", ventaGuardada.getCodigoBoleta(),
                "id", ventaGuardada.getId()
        ));

    } catch (Exception e) {
        logger.error("Error al registrar la venta", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", e.getMessage()));
    }
}

    @GetMapping("/generarBoletaPdf/{id}")
    public void generarBoletaPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {

        Venta venta = ventaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        byte[] pdfBytes = pdfGeneratorService.generarPdfVenta(venta.getCodigoBoleta());

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "inline; filename=boleta_" + venta.getCodigoBoleta() + ".pdf"
        );

        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

    private String generateNextCodigoBoleta() {
        Optional<Integer> maxNumberOpt = ventaRepo.findMaxCodigoBoletaNumber();
        String prefix = "V";
        int nextNumber = 1;

        if (maxNumberOpt.isPresent()) {
            nextNumber = maxNumberOpt.get() + 1;
        }
        return String.format("%s%04d", prefix, nextNumber);
    }
}