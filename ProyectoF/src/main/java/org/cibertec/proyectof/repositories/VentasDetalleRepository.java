package org.cibertec.proyectof.repositories;

import org.cibertec.proyectof.entities.VentaDetalle;
import org.cibertec.proyectof.dtos.VentaReporte;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VentasDetalleRepository extends JpaRepository<VentaDetalle, Long> {

    @Query("""
    SELECT
        new org.cibertec.proyectof.dtos.VentaReporte(
            v.id,
            vd.id,
            p.id,
            p.nombre,
            c.nombre,
            vd.cantidad,
            vd.precioUnitario,
            vd.subtotal,
            v.fechaVenta,
            v.codigoBoleta
        )
    FROM VentaDetalle vd
    JOIN vd.venta v
    JOIN vd.producto p
    JOIN p.categoria c
    WHERE
        (:desde IS NULL OR v.fechaVenta >= :desde)
        AND (:hasta IS NULL OR v.fechaVenta <= :hasta)
        AND (:boleta IS NULL OR LOWER(v.codigoBoleta) LIKE LOWER(:boleta))
    ORDER BY v.fechaVenta DESC
""")
    List<VentaReporte> buscarReporte(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta,
            @Param("boleta") String boleta
    );
}
