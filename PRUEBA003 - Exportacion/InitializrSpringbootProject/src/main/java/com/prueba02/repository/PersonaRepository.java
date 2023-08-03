package com.prueba02.repository;

import com.prueba02.entity.PersonaEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonaRepository extends JpaRepository<PersonaEntity, Long>{
    
    // CONSULTA PARA FILTRAR RESULTADOS QUE TENGAN UN ESTADO TRUE
    // EN MYSQL WORKBENCH: SELECT * FROM Tabla_Personas WHERE estado = 1;
    // @Query(value = "select * from Tabla_Personas where estado = true", nativeQuery = true)
    @Query(value = "select x from PersonaEntity x where x.estado = true")
    List<PersonaEntity> listarPorEstadoTrue(Boolean estado);
    
    // CONSULTA PARA LISTAR LOS REGISTROS DE MANERA INVERSA (DE MAYOR A MENOR) SEGUN SU ID
    @Query(value = "select x from PersonaEntity x order by x.id DESC")
            
    // SIN PAGINACIÓN
    // List<PersonaEntity> listarPorIDMayorAMenor();
            
    // CON PAGINACIÓN
    Page<PersonaEntity> listarPorIDMayorAMenor(Pageable pageable);
    
}
