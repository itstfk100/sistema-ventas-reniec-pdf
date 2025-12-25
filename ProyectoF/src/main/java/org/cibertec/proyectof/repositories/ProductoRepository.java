package org.cibertec.proyectof.repositories;

import org.cibertec.proyectof.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByNombreContainingIgnoreCase(String nombre);


    List<Producto> findByCategoria_Id(Integer categoriaId);

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR p.id = :queryId")
    List<Producto> findByNombreContainingIgnoreCaseOrId(@Param("query") String query, @Param("queryId") Integer queryId);

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR CAST(p.id AS string) LIKE CONCAT('%', :query, '%')")
    List<Producto> findByNombreOrIdContaining(@Param("query") String query);
}