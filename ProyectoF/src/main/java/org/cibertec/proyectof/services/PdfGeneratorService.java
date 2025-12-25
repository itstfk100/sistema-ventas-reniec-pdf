package org.cibertec.proyectof.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.cibertec.proyectof.dtos.VentaReporte;
import org.cibertec.proyectof.entities.Venta;
import org.cibertec.proyectof.repositories.VentasDetalleRepository;
import org.cibertec.proyectof.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PdfGeneratorService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentasDetalleRepository ventasDetalleRepository;

    public byte[] generarPdfVenta(String codigoBoleta) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Optional<Venta> optionalVenta = ventaRepository.findByCodigoBoleta(codigoBoleta);
        if (optionalVenta.isEmpty()) {
            document.add(new Paragraph("Venta no encontrada para el código de boleta: " + codigoBoleta));
            document.close();
            return baos.toByteArray();
        }
        Venta venta = optionalVenta.get();
        List<VentaReporte> detallesReporte = ventasDetalleRepository.buscarReporte(null, null, codigoBoleta);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        document.add(new Paragraph("MINIMARKET - BOLETA DE VENTA").setTextAlignment(TextAlignment.CENTER).setFontSize(20));
        document.add(new Paragraph("--------------------------------------------------").setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Código de Boleta: " + venta.getCodigoBoleta()));
        document.add(new Paragraph("Fecha de Venta: " + venta.getFechaVenta().format(formatter)));
        document.add(new Paragraph("Atendido por: " + (venta.getUsuario() != null ? venta.getUsuario().getNombre() : "N/A")));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Detalle de Productos:").setFontSize(14));
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 1, 1, 1}));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Paragraph("Cód. Prod.").setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Paragraph("Producto").setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Paragraph("Cant.").setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Paragraph("P. Unit.").setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Paragraph("Subtotal").setTextAlignment(TextAlignment.CENTER));

        for (VentaReporte detalle : detallesReporte) {
            table.addCell(new Paragraph(String.valueOf(detalle.getProductoId())).setTextAlignment(TextAlignment.CENTER)); // <-- CORREGIDO: getProductoId()
            table.addCell(new Paragraph(detalle.getProducto()).setTextAlignment(TextAlignment.LEFT));
            table.addCell(new Paragraph(String.valueOf(detalle.getCantidad())).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Paragraph(String.format("S/ %.2f", detalle.getPrecioUnitario())).setTextAlignment(TextAlignment.RIGHT)); // <-- CORREGIDO: getPrecioUnitario()
            table.addCell(new Paragraph(String.format("S/ %.2f", detalle.getSubtotal())).setTextAlignment(TextAlignment.RIGHT));
        }

        document.add(table);
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("TOTAL DE LA VENTA: S/ " + String.format("%.2f", venta.getTotal()))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(16));

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("¡Gracias por su compra!").setTextAlignment(TextAlignment.CENTER));

        document.close();
        return baos.toByteArray();
    }
}
