package org.cibertec.proyectof.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VentaReporte {

    private Long ventaId;

    private Long idDetalle;
    private Integer productoId;
    private String producto;
    private String categoria;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private LocalDate fechaVenta;
    private String codigoBoleta;

    public VentaReporte(Long ventaId, Long idDetalle, Integer productoId, String producto, String categoria, Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal, LocalDate fechaVenta, String codigoBoleta) {
        this.ventaId = ventaId; 
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

    public VentaReporte() {} 

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

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
