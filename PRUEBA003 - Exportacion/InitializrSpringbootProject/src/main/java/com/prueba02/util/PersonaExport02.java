package com.prueba02.util;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.prueba02.dto.PersonaDto;
import java.awt.Color;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaExport02{
    
    // SOLAMENTE SE VA A UTILIZAR UN OBJETO
    private PersonaDto unaPersona;
    
    // GENERAR EL PDF...
    // DISEÑO DEL DOCUMENTO QUE SE VA A EXPORTAR A PDF
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        
        // TAMAÑO DE HOJA DEL DOCUMENTO: A4
        try ( Document documento = new Document(PageSize.A4)) {
            
            // GENERA EL DOCUMENTO
            PdfWriter.getInstance(documento, response.getOutputStream());
            
            // ---> INICIO DEL DOCUMENTO
            documento.open();

            
            
            /***************************************/
            // TITULO
            /***************************************/
            
            // TIPO DE FUENTE / TAMAÑO DE FUENTE / COLOR DE FUENTE
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.BLUE);
            // DEFINIR UN PARRAFO QUE VA A CONTENER EL TEXTO "Detalles del registro #" JUNTO CON EL ID DE LA PERSONA / CON EL FORMATO DE TEXTO tituloFont
            Paragraph titulo = new Paragraph("Detalles del registro #" + unaPersona.getId(), tituloFont);
            // ALINEAR PARRAFO AL CENTRO
            titulo.setAlignment(Element.ALIGN_CENTER);
            // ESPACIADO DESPUES DEL TITULO
            titulo.setSpacingAfter(30);
            // AGREGAR EL PARRAFO "TITULO" AL DOCUMENTO
            documento.add(titulo);

            
            
            /***************************************/
            // AGREGAR IMAGEN
            /***************************************/
            
            try { // SI LA IMAGEN EXISTE...
                
                // BUSCAR LA IMAGEN EN LA RUTA
                Image imagen = Image.getInstance("assets/" + unaPersona.getRutaImagen());
                // AJUSTAR EL TAMAÑO DE LA IMAGEN PARA QUE QUEDE DENTRO DEL RECTANGULO CON LA MEDIDA 400 DE ANCHO Y 400 DE ALTURA
                imagen.scaleToFit(400, 400);
                // ALINEA LA IMAGEN AL CENTRO
                imagen.setAlignment(Element.ALIGN_CENTER);
                // AGREGAR LA IMAGEN AL DOCUMENTO
                documento.add(imagen);
                
            } catch (IOException e) {  // PERO SI LA IMAGEN NO EXISTE...
                
                // DEFINIR UN PARRAFO QUE VA A CONTENER EL TEXTO "No se encontró la imagen" / CON UN FORMATO DE TEXTO ESTABLECIDO
                Paragraph mensajeError = new Paragraph("No se encontró la imagen", FontFactory.getFont(FontFactory.HELVETICA, 12, Color.RED));
                // ALINEAR EL PARRAFO AL CENTRO
                mensajeError.setAlignment(Element.ALIGN_CENTER);
                // AGREGAR EL PARRAFO AL DOCUMENTO
                documento.add(mensajeError);
                
            }

            
            
            /***************************************/
            // DESCRIPCION
            /***************************************/
            
            // TIPO DE FUENTE / TAMAÑO DE FUENTE / COLOR DE FUENTE
            Font descripcionFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);
            // DEFINIR UN PARRAFO QUE VA A CONTENER EL TEXTO "Detalles de la persona:" / CON UN FORMATO DE TEXTO descripcionFont
            Paragraph descripcion = new Paragraph("Detalles de la persona:", descripcionFont);
            // ALINEAR EL PARRAFO A LA IZQUIERDA
            descripcion.setAlignment(Element.ALIGN_LEFT);
            // ESPACIADO ANTES DE LA DESCRIPCION
            descripcion.setSpacingBefore(30);
            // ESPACIADO DESPUES DE LA DESCRIPCION
            descripcion.setSpacingAfter(30);
            documento.add(descripcion);

            
            
            /***************************************/
            // TABLA VERTCIAL
            /***************************************/
            
            // DEFINIR UNA TABLA QUE TENGA 2 COLUMNAS
            PdfPTable tabla = new PdfPTable(2);
            // ANCHO DE LA TABLA AL 70% DEL ANCHO DE LA PAGINA
            tabla.setWidthPercentage(70);
            // ALINEAR EL PARRAFO A LA IZQUIERDA
            tabla.setHorizontalAlignment(Element.ALIGN_LEFT);
            // AGREGAR CELDAS A LA TABLA (SE GENERAN DE IZQUIERDA A DERECHA Y DE ARRIBA HACIA ABAJO...)
            tabla.addCell("ATRIBUTO");
            tabla.addCell("VALOR");
            tabla.addCell("ID:");
            tabla.addCell(String.valueOf(unaPersona.getId()));
            tabla.addCell("Nombres:");
            tabla.addCell(unaPersona.getNombres());
            tabla.addCell("Apellidos:");
            tabla.addCell(unaPersona.getApellidos());
            tabla.addCell("DNI:");
            tabla.addCell(String.valueOf(unaPersona.getDni()));
            tabla.addCell("Dirección:");
            tabla.addCell(unaPersona.getDireccion());
            tabla.addCell("Email:");
            tabla.addCell(unaPersona.getEmail());
            tabla.addCell("Teléfono:");
            tabla.addCell(String.valueOf(unaPersona.getTelefono()));
            tabla.addCell("Estado:");
            tabla.addCell(String.valueOf(unaPersona.getEstado()));
            // AGREGAR LA TABLA AL DOCUMENTO
            documento.add(tabla);

            
            // ---> FIN DEL DOCUMENTO
            documento.close();
            
        }
    }
}










