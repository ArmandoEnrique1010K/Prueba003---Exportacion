package com.prueba02.util;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.prueba02.dto.PersonaDto;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaExport01 {

    // LISTA DE OBJETOS QUE SE VAN A UTILIZAR
    private List<PersonaDto> listaPersonas;

    // DEFINIR LAS CELDAS DE LA CABECERA DE LA TABLA
    private void escribirCabeceraDeLaTabla(PdfPTable tabla) {

        // UNA CELDA
        PdfPCell celda = new PdfPCell();

        // COLOR DE FONDO
        celda.setBackgroundColor(Color.BLUE);

        // ESPACIADO 
        celda.setPadding(4);

        // TIPO DE FUENTE
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA);

        // COLOR DE TEXTO
        fuente.setColor(Color.WHITE);

        // DEFINIR LAS CELDAS QUE VA A CONTENER LA CABECERA
        // (TEXTO QUE SE VA A ESCRIBIR, ESTILO DEL TEXTO)
        celda.setPhrase(new Phrase("ID", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Nombres", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Apellidos", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("DNI", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Dirección", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Email", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Telefono", fuente));
        tabla.addCell(celda);
        celda.setPhrase(new Phrase("Estado", fuente));
        tabla.addCell(celda);

    }

    // ESCRIBIR LOS DATOS QUE VA CONTENER LA TABLA (DEBAJO DE LA CABECERA)
    private void escribirDatosDeLaTabla(PdfPTable tabla) {

        // ITERAR SOBRE LA LISTA DE PERSONAS 
        for (PersonaDto personaDto : listaPersonas) {
            tabla.addCell(String.valueOf(personaDto.getId()));
            tabla.addCell(personaDto.getNombres());
            tabla.addCell(personaDto.getApellidos());
            tabla.addCell(String.valueOf(personaDto.getDni()));
            tabla.addCell(personaDto.getDireccion());
            tabla.addCell(personaDto.getEmail());
            tabla.addCell(String.valueOf(personaDto.getTelefono()));
            tabla.addCell(String.valueOf(personaDto.getEstado()));
        }
        
    }
    
    // GENERAR EL PDF...
    // DISEÑO DEL DOCUMENTO QUE SE VA A EXPORTAR A PDF
    public void export(HttpServletResponse response) throws DocumentException, IOException {

        // TAMAÑO DE HOJA DEL DOCUMENTO: A4
        try ( Document documento = new Document(PageSize.A4)) {

            // GENERA EL DOCUMENTO
            PdfWriter.getInstance(documento, response.getOutputStream());

            // ---> INICIO DEL DOCUMENTO
            documento.open();

            /****************************************/
            // TITULO
            /****************************************/
            
            // TIPO DE FUENTE
            Font fuentetitulo = FontFactory.getFont(FontFactory.TIMES_BOLD);

            // TAMAÑO
            fuentetitulo.setSize(18);

            // COLOR
            fuentetitulo.setColor(Color.BLUE);

            // DEFINIR UN PARRAFO PARA EL TITULO
            Paragraph titulo = new Paragraph("Lista de registros habilitados", fuentetitulo);

            // ALINEAR PARRAFO AL CENTRO
            titulo.setAlignment(Paragraph.ALIGN_CENTER);

            // ESPACIADO DESPUES DEL TITULO
            titulo.setSpacingAfter(30);

            /*****************************************/
            // AGREGAR EL PARRAFO "TITULO" AL DOCUMENTO
            documento.add(titulo);
            /*****************************************/

            /*****************************************/
            // TABLA
            /*****************************************/
            
            // DEFINIR UNA TABLA QUE TENGA 8 COLUMNAS
            PdfPTable tabla = new PdfPTable(8);

            // ANCHO DE LA TABLA AL 100% DEL ANCHO DE LA PAGINA
            tabla.setWidthPercentage(100f);

            // ANCHO PROPOCIONAL DE CADA COLUMNA DE LA TABLA (TANTEAR CON DIFERENTES VALORES)
            // ID / NOMBRES / APELLIDOS / DNI / DIRECCION / EMAIL / TELEFONO / ESTADO
            tabla.setWidths(new float[]{1f, 4f, 4f, 2.5f, 6f, 5.5f, 3f, 3f});

            // CABECERA DE LA TABLA
            escribirCabeceraDeLaTabla(tabla);

            // DATOS DE LA TABLA
            escribirDatosDeLaTabla(tabla);

            /*****************************************/
            // AGREGAR LA TABLA DEFINIDA
            documento.add(tabla);
            /*****************************************/

            // ---> FIN DEL DOCUMENTO
            documento.close();

        }

    }

}
