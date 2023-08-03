package com.prueba02.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaDto {
    private Long id;
    private String nombres;
    private String apellidos;
    private Integer dni;
    private String direccion;
    private String email;
    private Integer telefono;
    private String rutaImagen;
    private MultipartFile imagen;
    private Boolean estado;
}
