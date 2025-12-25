package org.cibertec.proyectof.repositories;

import org.cibertec.proyectof.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByActivoTrue();

    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    List<Producto> findByCategoria_IdAndActivoTrue(Integer categoriaId);

    @Query("""
        SELECT p FROM Producto p
        WHERE p.activo = true
        AND (
            LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%'))
            OR CAST(p.id AS string) LIKE CONCAT('%', :query, '%')
        )
    """)
    List<Producto> buscarActivos(@Param("query") String query);
}
