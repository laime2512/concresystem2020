package Utiles;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import Modelos.ViewAlmacen;

public class ExcelReportView extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setHeader("Content-Disposition", "attachment:filename=\"informe_almacen.xls\"");
		@SuppressWarnings("unchecked")
		List<ViewAlmacen> almacenList=((List<ViewAlmacen>)model.get("almacenList"));
		String company = (String)model.get("company");
		String branch = (String)model.get("branch");
		String dateReport = (String)model.get("dateReport");
		String title = (String)model.get("title");
		Sheet sheet=workbook.createSheet("almacen");
		sheet.setColumnWidth(1, 18000);
		sheet.setColumnWidth(2, 10000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 8000);
		sheet.setColumnWidth(6, 2900);
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		Font font= sheet.getWorkbook().createFont();
		font.setBold(true);
		cellStyle.setFont(font);
		/**
		 * Datos generales del archivo excel
		 */
		int numberRow = 0;
		Row row0= sheet.createRow(numberRow++);
		Cell row0cell0 = row0.createCell(0);
		row0cell0.setCellStyle(cellStyle);
		row0cell0.setCellValue("Empresa: "+company);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
		
		Row row1= sheet.createRow(numberRow++);
		Cell row1cell0 = row1.createCell(0);
		row1cell0.setCellStyle(cellStyle);
		row1cell0.setCellValue("Sucursal: "+branch);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
		
		Row row2= sheet.createRow(numberRow++);
		Cell row2cell0 = row2.createCell(0);
		row2cell0.setCellStyle(cellStyle);
		row2cell0.setCellValue("Fecha de reporte: "+dateReport);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));
		
		Row row3= sheet.createRow(numberRow++);
		Cell row3cell0 = row3.createCell(0);
		row3cell0.setCellStyle(cellStyle);
		row3cell0.setCellValue("Reporte: "+title);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));
		
		numberRow++;
		
		Row header= sheet.createRow(numberRow++);
		Cell cell0 = header.createCell(0);
		cell0.setCellStyle(cellStyle);
		cell0.setCellValue("Cod");
		Cell cell1 = header.createCell(1);
		cell1.setCellStyle(cellStyle);
		cell1.setCellValue("Producto");
		Cell cell2 = header.createCell(2);
		cell2.setCellStyle(cellStyle);
		cell2.setCellValue("Generico");
		Cell cell3 = header.createCell(3);
		cell3.setCellStyle(cellStyle);
		cell3.setCellValue("Proveedor");
		Cell cell4 = header.createCell(4);
		cell4.setCellStyle(cellStyle);
		cell4.setCellValue("Cod.Barra");
		Cell cell5 = header.createCell(5);
		cell5.setCellStyle(cellStyle);
		cell5.setCellValue("Generico");
		Cell cell6 = header.createCell(6);
		cell6.setCellStyle(cellStyle);
		cell6.setCellValue("P.Publico");
		Cell cell7 = header.createCell(7);
		cell7.setCellStyle(cellStyle);
		cell7.setCellValue("Cantidad");
		for (ViewAlmacen product : almacenList) {
			Row row=sheet.createRow(numberRow++);
			row.createCell(0).setCellValue(product.getCodpro());
			row.createCell(1).setCellValue(product.getNombre());
			row.createCell(2).setCellValue(product.getGenerico());
			row.createCell(3).setCellValue(product.getXlaboratorio());
			row.createCell(4).setCellValue(product.getCodigobarra());
			row.createCell(5).setCellValue(product.getGenerico());
			row.createCell(6).setCellValue(product.getPvUnit());
			row.createCell(7).setCellValue(product.getCantidad());
		}
	}

}
