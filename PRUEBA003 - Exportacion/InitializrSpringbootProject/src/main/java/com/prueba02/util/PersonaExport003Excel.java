package com.prueba02.util;

import com.prueba02.dto.PersonaDto;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaExport003Excel {

    // LISTA DE OBJETOS QUE SE VAN A UTILIZAR
    private List<PersonaDto> listaPersonas;

    // OBJETOS DE EXCEL
    private XSSFWorkbook libro;
    private XSSFSheet hoja;
	public PersonaExport003Excel(List<PersonaDto> listaPersonas) {
		this.listaPersonas = listaPersonas;
		libro = new XSSFWorkbook();
		hoja = libro.createSheet("Hoja 1");
	}

    private void escribirCabeceraDeTabla() {
        Row fila = hoja.createRow(0);
        CellStyle estilo = libro.createCellStyle();
        XSSFFont fuente = libro.createFont(); 
        fuente.setBold(true);
        fuente.setFontHeight(16);
        estilo.setFont(fuente);

        Cell celda = fila.createCell(0);
        celda.setCellValue("ID");
        celda.setCellStyle(estilo);

        celda = fila.createCell(1);
        celda.setCellValue("Nombres");
        celda.setCellStyle(estilo);

        celda = fila.createCell(2);
        celda.setCellValue("Apellidos");
        celda.setCellStyle(estilo);

        celda = fila.createCell(3);
        celda.setCellValue("DNI");
        celda.setCellStyle(estilo);

        celda = fila.createCell(4);
        celda.setCellValue("Dirección");
        celda.setCellStyle(estilo);

        celda = fila.createCell(5);
        celda.setCellValue("Email");
        celda.setCellStyle(estilo);

        celda = fila.createCell(6);
        celda.setCellValue("Telefono");
        celda.setCellStyle(estilo);

        celda = fila.createCell(7);
        celda.setCellValue("Estado");
        celda.setCellStyle(estilo);

    }

    private void escribirDatosDeLaTabla() {
        int numeroFilas = 1;

        CellStyle estilo = libro.createCellStyle();
        XSSFFont fuente = libro.createFont();
        fuente.setFontHeight(14);
        estilo.setFont(fuente);

        for (PersonaDto personaDto : listaPersonas) {
            Row fila = hoja.createRow(numeroFilas++);

            Cell celda = fila.createCell(0);
            celda.setCellValue(personaDto.getId());
            hoja.autoSizeColumn(0);
            celda.setCellStyle(estilo);

            celda = fila.createCell(1);
            celda.setCellValue(personaDto.getNombres());
            hoja.autoSizeColumn(1);
            celda.setCellStyle(estilo);

            celda = fila.createCell(2);
            celda.setCellValue(personaDto.getApellidos());
            hoja.autoSizeColumn(2);
            celda.setCellStyle(estilo);

            celda = fila.createCell(3);
            Integer dni = personaDto.getDni();
            int dniValue = (dni != null) ? dni : 0;
            celda.setCellValue(dniValue);
            hoja.autoSizeColumn(3);
            celda.setCellStyle(estilo);

            celda = fila.createCell(4);
            celda.setCellValue(personaDto.getDireccion());
            hoja.autoSizeColumn(4);
            celda.setCellStyle(estilo);

            celda = fila.createCell(5);
            celda.setCellValue(personaDto.getEmail());
            hoja.autoSizeColumn(5);
            celda.setCellStyle(estilo);

            celda = fila.createCell(6);
            Integer telefono = personaDto.getTelefono();
            int telefonoValue = (telefono != null) ? telefono : 0;
            celda.setCellValue(telefonoValue);
            hoja.autoSizeColumn(6);
            celda.setCellStyle(estilo);

            celda = fila.createCell(7);
            String valorestado = "";
            if (personaDto.getEstado().equals(true)) {
                valorestado = "habilitado";
            } else {
                valorestado = "inhabilitado";
            }
            celda.setCellValue(valorestado);
            hoja.autoSizeColumn(7);
            celda.setCellStyle(estilo);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        escribirCabeceraDeTabla();
        escribirDatosDeLaTabla();
        try ( ServletOutputStream outPutStream = response.getOutputStream()) {
            libro.write(outPutStream);
            libro.close();
        }
    }

}

























