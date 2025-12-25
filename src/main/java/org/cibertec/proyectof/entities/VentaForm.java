package org.cibertec.proyectof.entities;

import java.math.BigDecimal;

public class VentaForm {

    private String codigoBoleta;
    private String productoId;
    private int cantidad;
    private String proveedorId;
    private BigDecimal total;

    // Getters y setters
    public String getCodigoBoleta() { return codigoBoleta; }
    public void setCodigoBoleta(String codigoBoleta) { this.codigoBoleta = codigoBoleta; }
    public String getProductoId() { return productoId; }
    public void setProductoId(String productoId) { this.productoId = productoId; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getProveedorId() { return proveedorId; }
    public void setProveedorId(String proveedorId) { this.proveedorId = proveedorId; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    @Override
    public String toString() {
        return "VentaForm{" +
                "codigoBoleta='" + codigoBoleta + '\'' +
                ", productoId='" + productoId + '\'' +
                ", cantidad=" + cantidad +
                ", proveedorId='" + proveedorId + '\'' +
                ", total=" + total +
                '}';
    }
}