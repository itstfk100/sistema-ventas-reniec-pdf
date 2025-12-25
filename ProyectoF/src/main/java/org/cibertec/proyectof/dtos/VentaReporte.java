package org.cibertec.proyectof.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

// Este DTO se utiliza para proyectar los datos necesarios para el reporte de ventas
// y la generación del PDF, combinando información de Venta y VentaDetalle.
public class VentaReporte {

    // CAMBIO: Añadido el ID de la Venta principal
    private Long ventaId;

    private Long idDetalle; // ID del detalle de venta
    private Integer productoId;
    private String producto;
    private String categoria;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private LocalDate fechaVenta;
    private String codigoBoleta;

    /**
     * Constructor usado por la consulta @Query de JPA.
     * El orden y los tipos de los parámetros DEBEN coincidir con los de la consulta.
     * Alineado con los tipos de la base de datos (p.id como Integer, vd.cantidad como Integer).
     *
     * Se añadió ventaId como primer parámetro para ser populado desde v.id en la consulta.
     */
    public VentaReporte(Long ventaId, Long idDetalle, Integer productoId, String producto, String categoria, Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal, LocalDate fechaVenta, String codigoBoleta) {
        this.ventaId = ventaId; // Inicializar nuevo campo
        this.idDetalle = idDetalle;
        this.productoId = productoId;
        this.producto = producto;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.fechaVenta = fechaVenta;
        this.codigoBoleta = codigoBoleta;
    }

    public VentaReporte() {} // Constructor por defecto necesario para algunas operaciones de Spring/JPA

    // Getters y Setters para el nuevo campo ventaId
    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    // Getters existentes
    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }

    public String getCodigoBoleta() { return codigoBoleta; }
    public void setCodigoBoleta(String codigoBoleta) { this.codigoBoleta = codigoBoleta; }
}
