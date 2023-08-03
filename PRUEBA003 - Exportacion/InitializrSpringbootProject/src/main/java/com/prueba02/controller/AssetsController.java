package com.prueba02.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prueba02.service.PersonaServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/assets")
public class AssetsController {

	@Autowired
	private PersonaServiceImpl personaServiceImpl;
        
        // EL USUARIO PODRA VER LA IMAGEN EN TAMAÑO COMPLETO SI HACE CLIC DERECHO EN LA IMAGEN 
        // Y HACE CLIC EN LA OPCIÓN "ABRIR IMAGEN EN UNA NUEVA PESTAÑA"
        @GetMapping("/{filename:.+}")
        public ResponseEntity<Resource> obtenerRecurso(@PathVariable("filename") String filename) {
        Resource recurso = personaServiceImpl.cargarComoRecurso(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.IMAGE_GIF)
                .body(recurso);
        }

}

