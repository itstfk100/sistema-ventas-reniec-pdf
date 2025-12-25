package org.cibertec.proyectof.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class VentaDTO {
    private String dniCliente;
    private String codigoBoleta;
    private LocalDate fecha;
    private BigDecimal total;
    private List<VentaDetalleDTO> detalles;
    
    public String getDniCliente() { return dniCliente; }
    public void setDniCliente(String dniCliente) { this.dniCliente = dniCliente; }
    public String getCodigoBoleta() { return codigoBoleta; }
    public void setCodigoBoleta(String codigoBoleta) { this.codigoBoleta = codigoBoleta; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public List<VentaDetalleDTO> getDetalles() { return detalles; }
    public void setDetalles(List<VentaDetalleDTO> detalles) { this.detalles = detalles; }
}