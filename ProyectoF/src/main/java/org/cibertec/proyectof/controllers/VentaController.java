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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private static final Logger logger = LoggerFactory.getLogger(VentaController.class); // Instancia del logger

    @Autowired
    private VentaRepository ventaRepo;
    @Autowired
    private VentasDetalleRepository ventaDetalleRepo;
    @Autowired
    private ProductoRepository productoRepo;
    @Autowired
    private UsuarioRepository usuarioRepo;

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
            Venta venta = new Venta();

            String nextCodigoBoleta = generateNextCodigoBoleta();
            venta.setCodigoBoleta(nextCodigoBoleta);

            if (ventaDto.getFecha() != null) {
                venta.setFechaVenta(ventaDto.getFecha());
            } else {
                venta.setFechaVenta(LocalDate.now());
            }

            venta.setTotal(ventaDto.getTotal());

            Usuario usuario = usuarioRepo.findById(1)
                    .orElseThrow(() -> new RuntimeException("Usuario con ID 1 no encontrado. Verifique la base de datos."));
            venta.setUsuario(usuario);

            Venta ventaGuardada = ventaRepo.save(venta);

            for (VentaDetalleDTO detalleDto : ventaDto.getDetalles()) {
                Producto producto = productoRepo.findById(detalleDto.getProductoId())
                        .orElseThrow(() -> new RuntimeException("Producto con ID " + detalleDto.getProductoId() + " no encontrado."));

                if (producto.getStock() < detalleDto.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre() + " (Stock actual: " + producto.getStock() + ", Cantidad solicitada: " + detalleDto.getCantidad() + ")");
                }

                producto.setStock((int) (producto.getStock() - detalleDto.getCantidad()));
                productoRepo.save(producto);

                VentaDetalle detalle = new VentaDetalle();
                detalle.setVenta(ventaGuardada);
                detalle.setProducto(producto);
                detalle.setCantidad(detalleDto.getCantidad());

                detalle.setPrecioUnitario(detalleDto.getPrecioUnitario());

                detalle.setSubtotal(detalleDto.getSubtotal());

                ventaDetalleRepo.save(detalle);
            }

            return ResponseEntity.ok(Map.of("message", "Venta registrada con éxito", "codigoBoleta", ventaGuardada.getCodigoBoleta(), "id", ventaGuardada.getId()));
        } catch (Exception e) {
            logger.error("Error al registrar la venta: " + e.getMessage(), e); // Log del error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error al registrar la venta: " + e.getMessage()));
        }
    }

    @GetMapping("/generarBoletaPdf/{id}")
    public void generarBoletaPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        try {
            Optional<Venta> optionalVenta = ventaRepo.findById(id);

            if (optionalVenta.isEmpty()) {
                logger.warn("Venta no encontrada con ID: " + id);
                response.sendError(HttpStatus.NOT_FOUND.value(), "Venta no encontrada con ID: " + id);
                return;
            }

            Venta venta = optionalVenta.get();

            response.setContentType("application/pdf");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=boleta_" + venta.getCodigoBoleta() + ".pdf";
            response.setHeader(headerKey, headerValue);

            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Boleta de Venta"));
            document.add(new Paragraph("Código de Boleta: " + venta.getCodigoBoleta()));
            document.add(new Paragraph("Fecha: " + venta.getFechaVenta()));
            document.add(new Paragraph("Usuario: " + (venta.getUsuario() != null ? venta.getUsuario().getNombre() : "N/A")));
            document.add(new Paragraph("Total: S/ " + venta.getTotal().setScale(2, BigDecimal.ROUND_HALF_UP)));
            document.add(new Paragraph("\nDetalles de la Venta:"));

            float[] columnWidths = {1, 3, 1, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.addHeaderCell("ID Producto");
            table.addHeaderCell("Producto");
            table.addHeaderCell("Cantidad");
            table.addHeaderCell("Precio Unitario");
            table.addHeaderCell("Subtotal");

            if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
                for (VentaDetalle detalle : venta.getDetalles()) {
                    table.addCell(String.valueOf(detalle.getProducto().getId()));
                    table.addCell(detalle.getProducto().getNombre()); // Asume que Producto tiene un método getNombre()
                    table.addCell(String.valueOf(detalle.getCantidad()));
                    table.addCell(detalle.getPrecioUnitario().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    table.addCell(detalle.getSubtotal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
            } else {
                document.add(new Paragraph("No hay detalles para esta venta."));
                logger.warn("No se encontraron detalles para la venta con ID: " + id);
            }
            document.add(table);

            document.close();
            logger.info("PDF de boleta generado exitosamente para la venta con ID: " + id);

        } catch (Exception e) {
            logger.error("Error al generar el PDF de la boleta para la venta con ID: " + id, e);
            if (!response.isCommitted()) {
                response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno al generar el PDF: " + e.getMessage());
            }
        }
    }

    /**
     * Genera el siguiente código de boleta consecutivo.
     * Obtiene el número máximo de los códigos de boleta existentes y lo incrementa.
     * Asume un formato "V" seguido de dígitos (ej. V0001, V0002).
     * Si no hay boletas previas, inicia en V0001.
     * @return El siguiente código de boleta único.
     */
    private String generateNextCodigoBoleta() {
        // Usa el nuevo método para obtener el número máximo directamente
        Optional<Integer> maxNumberOpt = ventaRepo.findMaxCodigoBoletaNumber();
        String prefix = "V";
        int nextNumber = 1;

        if (maxNumberOpt.isPresent()) {
            nextNumber = maxNumberOpt.get() + 1;
        }
        // Formatear el número con ceros a la izquierda para mantener 4 dígitos
        return String.format("%s%04d", prefix, nextNumber);
    }
}
