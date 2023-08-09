package com.prueba02.util;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
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
public class PersonaExport001PDF {

    // LISTA DE OBJETOS QUE SE VAN A UTILIZAR
    private List<PersonaDto> listaPersonas;

    // DEFINIR LAS CELDAS DE LA CABECERA DE LA TABLA
    private void escribirCabeceraDeLaTabla(PdfPTable tabla) {

        // UNA CELDA
        PdfPCell celda = new PdfPCell();

        // ALTURA FIJA DE LA CELDA
        celda.setFixedHeight(30);

        // COLOR DE FONDO
        celda.setBackgroundColor(new Color(5, 114, 150));

        // ESPACIADO 
        celda.setPadding(4);

        // ALINEAR EL TEXTO CONTENIDO AL CENTRO
        celda.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        celda.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

        // TIPO DE FUENTE
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        // COLOR DE TEXTO
        fuente.setColor(new Color(255, 255, 255));

        // TAMAÑO DE TEXTO
        fuente.setSize(10);

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

        // DOS CELDAS
        PdfPCell celdaA = new PdfPCell();
        PdfPCell celdaB = new PdfPCell();

        // ALTURA FIJA DE LAS CELDAS
        celdaA.setFixedHeight(30);
        celdaB.setFixedHeight(30);

        // ALINEAR EL TEXTO CONTENIDO AL CENTRO
        celdaA.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        celdaA.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

        // ALINEAR EL TEXTO CONTENIDO A LA IZQUIERDA
        celdaB.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
        celdaB.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

        // ITERAR SOBRE LA LISTA DE PERSONAS / APLICAR ESTILOS A FILAS PARES E IMPARES
        for (int i = 0; i < listaPersonas.size(); i++) {
            PersonaDto personaDto = listaPersonas.get(i);

            // CONVERTIR A TEXTO LOS CAMPOS QUE NO SON DE TIPO TEXTO
            String valor1 = String.valueOf(personaDto.getId());
            String valor2 = personaDto.getNombres();
            String valor3 = personaDto.getApellidos();
            String valor4 = String.valueOf(personaDto.getDni());
            String valor5 = personaDto.getDireccion();
            String valor6 = personaDto.getEmail();
            String valor7 = String.valueOf(personaDto.getTelefono());
            // String valor8 = String.valueOf(personaDto.getEstado());
            String valor8 = "";
            if (personaDto.getEstado().equals(true)) {
                valor8 = "habilitado";
            } else if (personaDto.getEstado().equals(false)) {
                valor8 = "inhabilitado";
            }

            // TIPO DE FUENTE Y TAMAÑO DE LETRA PARA EL TEXTO DE LOS DATOS DEL ID
            Font textoId = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            textoId.setSize(8);

            // TIPO DE FUENTE Y TAMAÑO DE LETRA PARA EL TEXTO DE LOS DEMÁS DATOS 
            Font textoValores = FontFactory.getFont(FontFactory.HELVETICA);
            textoValores.setSize(8);

            // CAMBIAR DE COLOR DE FONDO A LA CELDA SI SE ENCUENTRA EN UNA FILA PAR O IMPAR
            if (i % 2 == 0) {
                celdaA.setBackgroundColor(new Color(240, 240, 240));
                celdaB.setBackgroundColor(new Color(240, 240, 240));
            } else {
                celdaA.setBackgroundColor(new Color(255, 255, 255));
                celdaB.setBackgroundColor(new Color(255, 255, 255));
            }

            // DEFINIR LAS CELDAS QUE VAN A CONTENER LOS DATOS
            // ID
            celdaA.setPhrase(new Phrase(valor1, textoId));
            tabla.addCell(celdaA);
            // NOMBRES
            celdaB.setPhrase(new Phrase(valor2, textoValores));
            tabla.addCell(celdaB);
            // APELLIDOS
            celdaB.setPhrase(new Phrase(valor3, textoValores));
            tabla.addCell(celdaB);
            // DNI
            celdaA.setPhrase(new Phrase(valor4, textoValores));
            tabla.addCell(celdaA);
            // DIRECCIÓN
            celdaB.setPhrase(new Phrase(valor5, textoValores));
            tabla.addCell(celdaB);
            // EMAIL
            celdaB.setPhrase(new Phrase(valor6, textoValores));
            tabla.addCell(celdaB);
            // TELEFONO
            celdaA.setPhrase(new Phrase(valor7, textoValores));
            tabla.addCell(celdaA);
            // ESTADO
            celdaA.setPhrase(new Phrase(valor8, textoValores));
            tabla.addCell(celdaA);

        }

    }

    // GENERAR EL PDF...
    // DISEÑO DEL DOCUMENTO QUE SE VA A EXPORTAR A PDF
    public void export(HttpServletResponse response) throws DocumentException, IOException {

        // TAMAÑO DE HOJA DEL DOCUMENTO: A4
        try ( Document documento = new Document(PageSize.A4)) {

            // DEFINIR LOS MARGENES QUE VA A TENER UNA PAGINA DEL DOCUMENTO (DERECHA, IZQUIERDA, ARRIBA Y ABAJO)
            documento.setMargins(40, 40, 40, 40);

            // GENERA EL DOCUMENTO
            // PdfWriter.getInstance(documento, response.getOutputStream());
            PdfWriter writer = PdfWriter.getInstance(documento, response.getOutputStream());

            // DEFINIR EL EVENTO DE PAGINA PARA EL ENCABEZADO
            writer.setPageEvent(new EncabezadoEvento());
            // DEFINIR EL EVENTO DE PAGINA PARA EL PIE DE PAGINA
            writer.setPageEvent(new PieDePaginaEvento());

            // ---> INICIO DEL DOCUMENTO
            documento.open();

            /**
             * ************************************
             */
            // TITULO
            /**
             * ************************************
             */
            // TIPO DE FUENTE
            Font fuentetitulo = FontFactory.getFont(FontFactory.TIMES_BOLD);

            // TAMAÑO
            fuentetitulo.setSize(24);

            // COLOR
            fuentetitulo.setColor(new Color(2, 64, 91));

            // DEFINIR UN PARRAFO PARA EL TITULO
            Paragraph titulo = new Paragraph("Lista de registros habilitados", fuentetitulo);

            // ALINEAR PARRAFO AL CENTRO
            titulo.setAlignment(Paragraph.ALIGN_CENTER);

            // ESPACIADO DESPUES DEL TITULO
            titulo.setSpacingAfter(30);

            /**
             * *************************************
             */
            // AGREGAR EL PARRAFO "TITULO" AL DOCUMENTO
            documento.add(titulo);
            /**
             * *************************************
             */

            /**
             * *************************************
             */
            // TABLA
            /**
             * *************************************
             */
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

            /**
             * *************************************
             */
            // AGREGAR LA TABLA DEFINIDA
            documento.add(tabla);
            /**
             * *************************************
             */

            // ---> FIN DEL DOCUMENTO
            documento.close();

        }

    }

    // DEFINIR LA CLASE PARA EL EVENTO DE ENCABEZADO DE PAGINA
    class EncabezadoEvento extends PdfPageEventHelper {

        @Override
        // LLAMA AUTOMATICAMENTE CADA VEZ QUE UNA NUEVA PAGINA ESTA A PUNTO DE CREARSE EN EL DOCUMENTO
        // LOS PARAMETROS writer y documento REPRESENTAN EL ESCRITO DE PDF Y EL DOCUMENTO ACTUAL
        public void onStartPage(PdfWriter writer, Document document) {
            // EL METODO getDirectContent() PERMITE DIBUJAR CONTENIDO DIRECTAMENTE SOBRE EL PDF
            PdfContentByte contentByte = writer.getDirectContent();
            // SE CREA UNA FUENTE PARA EL ENCABEZADO
            Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
            // UNA "Phrase" ES UN CONJUNTO DE TEXTO CON UNA FUENTE ESPECIFICA
            Phrase encabezadoPhrase = new Phrase("Muestra de registro", fuenteEncabezado);
            // MUESTRA EL TEXTO EN UNA POSICION ALINEADA
            // (CONTENIDO, ALINEACIÓN, TEXTO, POSICIÓN HORIZONTAL, POSICIÓN VERTICAL, ROTACIÓN)
            ColumnText.showTextAligned(contentByte, Element.ALIGN_CENTER, encabezadoPhrase,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.top() + 20, 0);
        }
    }

    // DEFINIR LA CLASE PARA EL EVENTO DE PIE DE PAGINA
    class PieDePaginaEvento extends PdfPageEventHelper {

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte contentByte = writer.getDirectContent();
            Font fuentePie = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
            // getPageNumber() OBTIENE EL NUMERO DE PAGINA
            Phrase piePhrase = new Phrase("Página " + writer.getPageNumber(), fuentePie);
            ColumnText.showTextAligned(contentByte, Element.ALIGN_CENTER, piePhrase,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 20, 0);
        }
    }

}
