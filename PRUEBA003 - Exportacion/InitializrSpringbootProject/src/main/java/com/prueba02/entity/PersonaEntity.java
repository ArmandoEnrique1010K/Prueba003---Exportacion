package com.prueba02.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Entity
@Table(name = "Tabla_personas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "nombres")
    private String nombres;
    
    @Column(name = "apellidos")
    private String apellidos;
    
    @Column(name = "dni", length = 8)
    private Integer dni;
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "telefono")
    private Integer telefono;

    /* PARA LAS IMAGENES */
    @Column(name = "rutaImagen")
    private String rutaImagen;
    @Transient
    private MultipartFile imagen;

    @Column(name = "estado")
    private Boolean estado;

}