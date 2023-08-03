package com.prueba02.service;

import com.prueba02.dto.PersonaDto;
import com.prueba02.entity.PersonaEntity;
import com.prueba02.exception.AlmacenException;
import com.prueba02.exception.FileNotFoundException;
import com.prueba02.repository.PersonaRepository;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PersonaServiceImpl implements PersonaService{

    // Función para generar un codigo aleatorio
    private String generarUUIDComoNombre(String extension) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + extension;
    }

    // Función para obtener la extensión del nombre original de la imagen
    private String obtenerExtension(String nombreOriginal) {
        int extensionIndex = nombreOriginal.lastIndexOf('.');
        return (extensionIndex != -1) ? nombreOriginal.substring(extensionIndex) : "";
    }

    // Función para obtener el nombre original de la imagen (sin extensión)
    private String obtenerNombreOriginal(String nombreOriginal) {
        int extensionIndex = nombreOriginal.lastIndexOf('.');
        return (extensionIndex != -1) ? nombreOriginal.substring(0, extensionIndex) : nombreOriginal;
    }

    @Value("${storage.location}")
    private String storageLocation;

    @Autowired
    private PersonaRepository personaRepository;

    @PostConstruct
    @Override
    public void iniciarContenedorImagenes() {
        try {
            Files.createDirectories(Paths.get(storageLocation));
        } catch (IOException exception) {
            throw new AlmacenException("Error al inicializar la ubicación en el almacen de imagenes");
        }
    }

    @Override
    public String almacenarUnaImagen(MultipartFile imagen) {
        // String nombreImagen = imagen.getOriginalFilename();
        if (imagen.isEmpty()) {
            throw new AlmacenException("No se puede almacenar un archivo vacio");
        }

        // Verificar si el tipo de archivo es válido (solo admitir JPG, PNG y GIF)
        String contentType = imagen.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AlmacenException("Tipo de archivo no válido. Solo se admiten imágenes (JPG, PNG, GIF)");
        }

        try {

            // Obtener la extensión del archivo original
            String extension = obtenerExtension(imagen.getOriginalFilename());

            // Generar un código aleatorio usando UUID
            String codigoAleatorio = generarUUIDComoNombre("");

            // Generar el nuevo nombre usando el código aleatorio, el nombre original y la extensión del archivo original
            String nuevoNombre = codigoAleatorio + " - " + obtenerNombreOriginal(imagen.getOriginalFilename()) + extension;

            // Copiar la imagen al servidor con el nuevo nombre
            InputStream inputStream = imagen.getInputStream();
            Files.copy(inputStream, Paths.get(storageLocation).resolve(nuevoNombre), StandardCopyOption.REPLACE_EXISTING);

            // Retornar el nombre de la imagen almacenada
            return nuevoNombre;

        } catch (IOException exception) {
            throw new AlmacenException("Error al almacenar la imagen ", exception);
        }
    }
    
    @Override
    public Path cargarImagen(String nombreImagen) {
        Path imagenPath = Paths.get(storageLocation).resolve(nombreImagen);
        if (!Files.exists(imagenPath)) {
            throw new FileNotFoundException("No se pudo encontrar el archivo " + nombreImagen);
        }
        return imagenPath;
    }

    @Override
    public Resource cargarComoRecurso(String nombreImagen) {
        try {
            Path imagen = cargarImagen(nombreImagen);
            Resource recurso = new UrlResource(imagen.toUri());

            if (recurso.exists() || recurso.isReadable()) {
                return recurso;
            } else {
                throw new FileNotFoundException("No se pudo encontrar el archivo " + nombreImagen);
            }
        } catch (MalformedURLException exception) {
            throw new FileNotFoundException("No se pudo encontrar el archivo " + nombreImagen, exception);
        }
    }

    @Override
    public void eliminarImagen(String nombreImagen) {
        Path imagen = cargarImagen(nombreImagen);
        try {
            FileSystemUtils.deleteRecursively(imagen);
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
    
    @Override
    // LISTAR TODOS LOS REGISTROS (Solo se va a listar el id, nombre, apellidos, dni, imagen y el estado)
    public List<PersonaDto> getAllRegistros() {
        List<PersonaEntity> listPersonaEntity = personaRepository.findAll();
        
        // LISTAR POR ID MAYOR A MENOR
        // List<PersonaEntity> listPersonaEntity = personaRepository.listarPorIDMayorAMenor();
        
        List<PersonaDto> list = new ArrayList<>();
        for (PersonaEntity personaEntity : listPersonaEntity) {
            list.add(PersonaDto.builder()
                    .id(personaEntity.getId())
                    .nombres(personaEntity.getNombres())
                    .apellidos(personaEntity.getApellidos())
                    .dni(personaEntity.getDni())
                    // .direccion(personaEntity.getDireccion())
                    // .email(personaEntity.getEmail())
                    // .telefono(personaEntity.getTelefono())
                    .rutaImagen(personaEntity.getRutaImagen())
                    .imagen(personaEntity.getImagen())
                    .estado(personaEntity.getEstado())
                    .build()
            );
        }
        return list;
    }
    
    @Override
    // LISTAR TODOS LOS REGISTROS QUE TENGAN EL ESTADO TRUE (Se va a listar todos los registros, excepto las imagenes)
    public List<PersonaDto> getAllRegistrosStateVerdadero(Boolean estado) {
        List<PersonaEntity> listPersonaEntity = personaRepository.listarPorEstadoTrue(Boolean.TRUE);
        List<PersonaDto> list = new ArrayList<>();
        for (PersonaEntity personaEntity : listPersonaEntity) {
            list.add(PersonaDto.builder()
                    .id(personaEntity.getId())
                    .nombres(personaEntity.getNombres())
                    .apellidos(personaEntity.getApellidos())
                    .dni(personaEntity.getDni())
                    .direccion(personaEntity.getDireccion())
                    .email(personaEntity.getEmail())
                    .telefono(personaEntity.getTelefono())
                    // .rutaImagen(personaEntity.getRutaImagen())
                    // .imagen(personaEntity.getImagen())
                    .estado(personaEntity.getEstado())
                    .build()
            );
        }
        return list;
    }
    
    // MOSTRAR SOLAMENTE UN REGISTRO POR SU ID
    @Override
    public PersonaDto getRegistro(Long id) {

        PersonaEntity personaEntity = personaRepository.findById(id).orElse(null);

        if (null == personaEntity) {
            return null;
        }

        return PersonaDto.builder()
                .id(personaEntity.getId())
                .nombres(personaEntity.getNombres())
                .apellidos(personaEntity.getApellidos())
                .dni(personaEntity.getDni())
                .direccion(personaEntity.getDireccion())
                .email(personaEntity.getEmail())
                .telefono(personaEntity.getTelefono())
                .rutaImagen(personaEntity.getRutaImagen())
                .imagen(personaEntity.getImagen())
                .estado(personaEntity.getEstado())
                .build();

    }
    
    // INSERTAR UN REGISTRO
    @Override
    public PersonaDto createRegistro(PersonaDto registro) {

        // Almacenar la imagen con el nuevo nombre único
        String rutaImagen = almacenarUnaImagen(registro.getImagen());

        // Configurar los datos para la entidad
        PersonaEntity personaEntity = PersonaEntity.builder()
                .nombres(registro.getNombres())
                .apellidos(registro.getApellidos())
                .dni(registro.getDni())
                .direccion(registro.getDireccion())
                .email(registro.getEmail())
                .telefono(registro.getTelefono())
                .rutaImagen(rutaImagen)
                .estado(Boolean.TRUE)
                .build();

        // Guardar la entidad en la base de datos
        personaRepository.save(personaEntity);

        // Actualizar el objeto personaEntity con el ID generado y la ruta de la imagen
        registro.setId(personaEntity.getId());
        registro.setRutaImagen(rutaImagen);

        return registro;

    }
    
    // EDITAR UN REGISTRO, SI EL USUARIO NO SUBE UNA IMAGEN, ENTONCES SE VA A CONSERVAR LA IMAGEN ANTERIOR
    @Override
    public PersonaDto updateRegistro(PersonaDto registro) {
        PersonaEntity personaEntity = personaRepository.findById(registro.getId()).orElse(null);

        if (personaEntity == null) {
            return null;
        }

        // Si la imagen no está vacía y es diferente a la imagen actual, eliminar la imagen anterior y almacenar la nueva
        if (registro.getImagen() != null && !registro.getImagen().isEmpty() && 
                !registro.getImagen().equals(personaEntity.getImagen())) {
            eliminarImagen(personaEntity.getRutaImagen());

            // Almacenar la nueva imagen con un nuevo nombre único basado en UUID y su extensión
            String rutaImagen = almacenarUnaImagen(registro.getImagen());
            registro.setRutaImagen(rutaImagen);
        } else {
            // Si la imagen está vacía o es igual a la imagen actual, conservar la imagen actual en PersonaDto
            registro.setImagen(personaEntity.getImagen());
            registro.setRutaImagen(personaEntity.getRutaImagen());
        }

        // Actualizar los campos de galeriaEntity con los valores del GaleriaDto
        personaEntity.setNombres(registro.getNombres());
        personaEntity.setApellidos(registro.getApellidos());
        personaEntity.setDni(registro.getDni());
        personaEntity.setDireccion(registro.getDireccion());
        personaEntity.setEmail(registro.getEmail());
        personaEntity.setTelefono(registro.getTelefono());
        personaEntity.setRutaImagen(registro.getRutaImagen());
        personaEntity.setEstado(Boolean.TRUE);

        personaRepository.save(personaEntity);
        return registro;
        
    }

    // CAMBIAR EL ESTADO A FALSE
    @Override
    public void changeStateFalseRegistro(Long id) {
        PersonaEntity personaEntity = personaRepository.findById(id).orElse(null);

        if (personaEntity != null) {
            personaEntity.setEstado(Boolean.FALSE);
            personaRepository.save(personaEntity);
        }
    }

    // CAMBIAR EL ESTADO A TRUE
    @Override
    public void changeStateVerdaderoRegistro(Long id) {
        PersonaEntity personaEntity = personaRepository.findById(id).orElse(null);

        if (personaEntity != null) {
            personaEntity.setEstado(Boolean.TRUE);
            personaRepository.save(personaEntity);
        }
    }

    // ELIMINAR EL REGISTRO, ADEMÁS TAMBIEN DE LA IMAGEN ASOCIADA AL REGISTRO
    @Override
    public void deleteRegistro(Long id) {
        PersonaEntity personaEntity = personaRepository.findById(id).orElse(null);

        if (personaEntity != null) {
            // Obtener la ruta de la imagen asociada
            String rutaImagen = personaEntity.getRutaImagen();
            // Eliminar físicamente el archivo de la imagen
            if (rutaImagen != null && !rutaImagen.isEmpty()) {
                File archivoImagen = new File(rutaImagen);
                if (archivoImagen.exists()) {
                    archivoImagen.delete();
                }
            }
            // Eliminar el registro de la base de datos
            personaRepository.delete(personaEntity);
        }
    }

    // LISTAR UN CIERTO NUMERO DE REGISTROS EN UNA PAGINA (PAGINACION) "DE MANERA INVERSA"
    @Override
    public Page<PersonaDto> getAllRegistrosPaginados(Pageable pageable) {
        
        Page<PersonaEntity> personasPage = (Page<PersonaEntity>) personaRepository.listarPorIDMayorAMenor(pageable);
        
        // Lista para almacenar los objetos PersonaDto
        List<PersonaDto> personasDtoList = new ArrayList<>();
        
        // Iterar por cada PersonaEntity y convertir a PersonaDto dentro del bucle
        for (PersonaEntity personaEntity : personasPage) {
            PersonaDto personaDto = PersonaDto.builder()
                    .id(personaEntity.getId())
                    .nombres(personaEntity.getNombres())
                    .apellidos(personaEntity.getApellidos())
                    .dni(personaEntity.getDni())
                    // .direccion(personaEntity.getDireccion())
                    // .email(personaEntity.getEmail())
                    // .telefono(personaEntity.getTelefono())
                    .rutaImagen(personaEntity.getRutaImagen())
                    .imagen(personaEntity.getImagen())
                    .estado(personaEntity.getEstado())
                    .build();

            personasDtoList.add(personaDto);
        }
        return new PageImpl<>(personasDtoList, pageable, personasPage.getTotalElements());
    }
    
}
