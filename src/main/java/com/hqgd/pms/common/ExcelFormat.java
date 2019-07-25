//package com.hqgd.pms.common;
//
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFFont;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.util.HSSFColor;
//
//public class ExcelFormat {
//
//	public static HSSFCellStyle getHeaderStyle(HSSFWorkbook wb) {
//		HSSFCellStyle headerStyle = wb.createCellStyle();
//		// 设置单元格的背景颜色
//		headerStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
//		headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		// 设置单元格居中对齐
//		headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		// headerStyle.setWrapText(true);
//		HSSFFont font = wb.createFont();
//		// 字体加粗
//		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//		headerStyle.setFont(font);
//		// 设置边框细线条
//		headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
//
//		return headerStyle;
//	}
//
//	public static HSSFCellStyle getBodyStyle(HSSFWorkbook wb) {
//		HSSFCellStyle bodyStyle = wb.createCellStyle();
//		// 设置单元格居中对齐
//		bodyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		// 设置自动换行
//		// bodyStyle.setWrapText(true);
//		// 设置边框细线条
//		bodyStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//		bodyStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//		bodyStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//		bodyStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
//
//		return bodyStyle;
//	}
//}
