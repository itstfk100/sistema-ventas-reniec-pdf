package org.cibertec.proyectof.repositories;

import org.cibertec.proyectof.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    Optional<Venta> findByCodigoBoleta(String codigoBoleta);
    @Query(value = "SELECT v.codigoBoleta FROM Venta v ORDER BY v.id DESC LIMIT 1")
    Optional<String> findLastCodigoBoleta();
    @Query("SELECT v FROM Venta v WHERE " +
            "(:desde IS NULL OR v.fechaVenta >= :desde) AND " +
            "(:hasta IS NULL OR v.fechaVenta <= :hasta) AND " +
            "(:boleta IS NULL OR LOWER(v.codigoBoleta) LIKE LOWER(CONCAT('%', :boleta, '%')))")
    List<Venta> buscarVentas(@Param("desde") LocalDate desde,
                             @Param("hasta") LocalDate hasta,
                             @Param("boleta") String boleta);
    @Query(value = "SELECT MAX(CAST(SUBSTRING(v.codigo_boleta, 2) AS UNSIGNED)) FROM ventas v", nativeQuery = true)
    Optional<Integer> findMaxCodigoBoletaNumber();
}