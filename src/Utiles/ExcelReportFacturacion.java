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
import org.springframework.web.servlet.view.document.AbstractXlsView;

import Wrap.FacturaDosificacionWrap;

public class ExcelReportFacturacion extends AbstractXlsView {

	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		@SuppressWarnings("unchecked")
		List<FacturaDosificacionWrap> itemList=((List<FacturaDosificacionWrap>)model.get("itemList"));
		String title = (String)model.get("title");
		response.setHeader("Content-Disposition", "attachment:filename=\""+title+"\"");
		Sheet sheet=workbook.createSheet("Libro de Ventas IVA");
		sheet.setColumnWidth(1, 12000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(6, 2900);
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		Font font= sheet.getWorkbook().createFont();
		font.setBold(true);
		cellStyle.setFont(font);
		/**
		 * Detalle del archivo excel
		 */
		int numberRow = 0;

		
		Row header= sheet.createRow(numberRow++);
		Cell cell0 = header.createCell(0);
		cell0.setCellStyle(cellStyle);
		cell0.setCellValue("NIT/CI");
		Cell cell1 = header.createCell(1);
		cell1.setCellStyle(cellStyle);
		cell1.setCellValue("BENEFICIARIO");
		Cell cell2 = header.createCell(2);
		cell2.setCellStyle(cellStyle);
		cell2.setCellValue("NRO.FACTURA");
		Cell cell3 = header.createCell(3);
		cell3.setCellStyle(cellStyle);
		cell3.setCellValue("AUTORIZACION");
		Cell cell4 = header.createCell(4);
		cell4.setCellStyle(cellStyle);
		cell4.setCellValue("FECHA");
		Cell cell5 = header.createCell(5);
		cell5.setCellStyle(cellStyle);
		cell5.setCellValue("TOTAL FACTURA");
		Cell cell6 = header.createCell(6);
		cell6.setCellStyle(cellStyle);
		cell6.setCellValue("COD.CONTROL");
		Cell cell7 = header.createCell(7);
		cell7.setCellStyle(cellStyle);
		cell7.setCellValue("ESTADO");
		for (FacturaDosificacionWrap item : itemList) {
			Row row=sheet.createRow(numberRow++);
			row.createCell(0).setCellValue(item.getNitfac());
			row.createCell(1).setCellValue(item.getClienteNit()!=null?(!item.getClienteNit().trim().isEmpty()?item.getClienteNit():"SIN NOMBRE"):"");
			row.createCell(2).setCellValue(item.getNumfac());
			row.createCell(3).setCellValue(item.getNumaut());
			row.createCell(4).setCellValue(item.getFecfac()!=null?item.getFecfac().substring(0, 10):"");
			row.createCell(5).setCellValue(item.getTotal());
			row.createCell(6).setCellValue(item.getCodcontrol());
			row.createCell(7).setCellValue(item.getEstado());
		}
	}

}
