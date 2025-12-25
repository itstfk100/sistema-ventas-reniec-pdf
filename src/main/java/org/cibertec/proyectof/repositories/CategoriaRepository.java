package org.cibertec.proyectof.repositories;

import org.cibertec.proyectof.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
