package org.cibertec.proyectof.dtos;

import java.math.BigDecimal;
import java.util.List;

public class VentaFormDTO {
    private String codigoBoleta;
    private Long usuarioId;
    private BigDecimal total;
    private List<VentaDetalleDTO> items;

    // Getters y setters
    public String getCodigoBoleta() { return codigoBoleta; }
    public void setCodigoBoleta(String codigoBoleta) { this.codigoBoleta = codigoBoleta; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public List<VentaDetalleDTO> getItems() { return items; }
    public void setItems(List<VentaDetalleDTO> items) { this.items = items; }
}