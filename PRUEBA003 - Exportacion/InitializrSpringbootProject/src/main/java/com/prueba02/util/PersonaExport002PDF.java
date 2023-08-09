package com.prueba02.util;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
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
public class PersonaExport002PDF {
    
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
            Font tituloFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 24, new Color(2, 64, 91));
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
                // AJUSTAR EL TAMAÑO DE LA IMAGEN PARA QUE QUEDE DENTRO DEL RECTANGULO CON LA MEDIDA 500 DE ANCHO Y 375 DE ALTURA
                imagen.scaleToFit(500, 375);
                // ALINEA LA IMAGEN AL CENTRO
                imagen.setAlignment(Element.ALIGN_CENTER);
                // AGREGAR LA IMAGEN AL DOCUMENTO
                documento.add(imagen);
                
            } catch (IOException e) {  // PERO SI LA IMAGEN NO EXISTE...
                
                // DEFINIR UN PARRAFO QUE VA A CONTENER EL TEXTO "No se encontró la imagen" / CON UN FORMATO DE TEXTO ESTABLECIDO
                Paragraph mensajeError = new Paragraph("No se encontró la imagen", FontFactory.getFont(FontFactory.HELVETICA, 12, new Color(200, 200, 200)));
                // ALINEAR EL PARRAFO AL CENTRO
                mensajeError.setAlignment(Element.ALIGN_CENTER);
                // AGREGAR EL PARRAFO AL DOCUMENTO
                documento.add(mensajeError);
                
            }

            
            
            /***************************************/
            // DESCRIPCION
            /***************************************/
            
            // TIPO DE FUENTE / TAMAÑO DE FUENTE / COLOR DE FUENTE
            Font descripcionFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Color.BLACK);
            // DEFINIR UN PARRAFO QUE VA A CONTENER EL TEXTO "Detalles del registro solicitado por el cliente:" / CON UN FORMATO DE TEXTO descripcionFont
            Paragraph descripcion = new Paragraph("Detalles del registro solicitado por el cliente:", descripcionFont);
            // ALINEAR EL PARRAFO A LA IZQUIERDA
            descripcion.setAlignment(Element.ALIGN_LEFT);
            // ESPACIADO ANTES DE LA DESCRIPCION
            descripcion.setSpacingBefore(30);
            // ESPACIADO DESPUES DE LA DESCRIPCION
            descripcion.setSpacingAfter(30);
            documento.add(descripcion);

            
            
            /***************************************/
            // TABLA VERTICAL
            /***************************************/
            
            // DEFINIR UNA TABLA QUE TENGA 2 COLUMNAS
            PdfPTable tabla = new PdfPTable(2);
            // DEFINIR UNA CELDA
            PdfPCell celdacabecera = new PdfPCell();
            PdfPCell celdaatributo = new PdfPCell();
            PdfPCell celdavalor = new PdfPCell();

            
            
            // ESPACIADO DE LA CELDA
            celdacabecera.setPadding(4);
            celdaatributo.setPadding(4);
            celdavalor.setPadding(4);

            // COLOR DE CELDA
            celdacabecera.setBackgroundColor(new Color(2, 64, 91));
            celdaatributo.setBackgroundColor(Color.WHITE);
            celdavalor.setBackgroundColor(Color.WHITE);

            // ANCHO DE LA TABLA AL 70% DEL ANCHO DE LA PAGINA
            tabla.setWidthPercentage(70);
            // ALINEAR EL PARRAFO A LA IZQUIERDA
            tabla.setHorizontalAlignment(Element.ALIGN_LEFT);
            
            // TIPO DE FUENTE
            Font fuentecabecera = FontFactory.getFont(FontFactory.COURIER_BOLD, 14, Color.WHITE);
            Font fuenteatributo = FontFactory.getFont(FontFactory.COURIER_BOLD, 12, new Color(2, 64, 91));
            Font fuentevalor = FontFactory.getFont(FontFactory.COURIER, 12, Color.BLACK);
            
            // AGREGAR CELDAS A LA TABLA (SE GENERAN DE IZQUIERDA A DERECHA Y DE ARRIBA HACIA ABAJO...)
            
            celdacabecera.setPhrase(new Phrase("ATRIBUTO", fuentecabecera));
            tabla.addCell(celdacabecera);
            celdacabecera.setPhrase(new Phrase("VALOR", fuentecabecera));
            tabla.addCell(celdacabecera);
            
            celdaatributo.setPhrase(new Phrase("ID", fuenteatributo));
            tabla.addCell(celdaatributo);
            celdavalor.setPhrase(new Phrase(String.valueOf(unaPersona.getId()), fuentevalor));
            tabla.addCell(celdavalor);

            celdaatributo.setPhrase(new Phrase("Nombres", fuenteatributo));
            tabla.addCell(celdaatributo);
            celdavalor.setPhrase(new Phrase(unaPersona.getNombres(), fuentevalor));
            tabla.addCell(celdavalor);
            
            celdaatributo.setPhrase(new Phrase("Apellidos", fuenteatributo));
            tabla.addCell(celdaatributo);
            celdavalor.setPhrase(new Phrase(unaPersona.getApellidos(), fuentevalor));
            tabla.addCell(celdavalor);

            celdaatributo.setPhrase(new Phrase("DNI", fuenteatributo));
            tabla.addCell(celdaatributo);
            celdavalor.setPhrase(new Phrase(String.valueOf(unaPersona.getDni()), fuentevalor));
            tabla.addCell(celdavalor);
            
            celdaatributo.setPhrase(new Phrase("Dirección", fuenteatributo));
            tabla.addCell(celdaatributo);
            celdavalor.setPhrase(new Phrase(unaPersona.getDireccion(), fuentevalor));
            tabla.addCell(celdavalor);
            
            celdaatributo.setPhrase(new Phrase("Email", fuenteatributo));
            tabla.addCell(celdaatributo);
            celdavalor.setPhrase(new Phrase(unaPersona.getEmail(), fuentevalor));
            tabla.addCell(celdavalor);

            celdaatributo.setPhrase(new Phrase("Teléfono", fuenteatributo));
            tabla.addCell(celdaatributo);
            celdavalor.setPhrase(new Phrase(String.valueOf(unaPersona.getTelefono()), fuentevalor));
            tabla.addCell(celdavalor);
            
            String valorestado = "";
            
            if (unaPersona.getEstado().equals(true)){
                valorestado = "habilitado";
            } else {
                valorestado = "inhabilitado";
            }

            celdaatributo.setPhrase(new Phrase("Estado", fuenteatributo));
            tabla.addCell(celdaatributo);
            celdavalor.setPhrase(new Phrase(valorestado, fuentevalor));
            tabla.addCell(celdavalor);

            
            // AGREGAR LA TABLA AL DOCUMENTO
            documento.add(tabla);

            // ---> FIN DEL DOCUMENTO
            documento.close();
            
        }
    }
    
}


























