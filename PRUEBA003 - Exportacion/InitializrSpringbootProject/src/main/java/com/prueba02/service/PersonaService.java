package com.prueba02.service;

import com.prueba02.dto.PersonaDto;
import java.util.List;
import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PersonaService {
    
    /* Metodos para las imagenes */
    public void iniciarContenedorImagenes();
    public String almacenarUnaImagen(MultipartFile imagen);
    public Path cargarImagen(String nombreImagen);
    public Resource cargarComoRecurso(String nombreImagen);
    public void eliminarImagen(String nombreImagen);
    
    /* Metodos para los registros */
    public List<PersonaDto> getAllRegistros();
    public List<PersonaDto> getAllRegistrosStateVerdadero(Boolean estado);
    public PersonaDto getRegistro(Long id);
    public PersonaDto createRegistro(PersonaDto registro);
    public PersonaDto updateRegistro(PersonaDto registro);
    public void changeStateFalseRegistro(Long id);
    public void changeStateVerdaderoRegistro(Long id);
    public void deleteRegistro(Long id);
    
    /* Metodo para la paginacion */
    public Page<PersonaDto> getAllRegistrosPaginados(Pageable pageable);

}
