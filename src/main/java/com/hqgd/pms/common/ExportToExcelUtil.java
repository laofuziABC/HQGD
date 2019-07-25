package com.hqgd.pms.common;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExportToExcelUtil {
	final static SimpleDateFormat DF=new SimpleDateFormat("yyyyMMddHHmmss");
	final static SimpleDateFormat SDF=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//获取用户桌面url地址
	static FileSystemView fsv = FileSystemView.getFileSystemView();
	static File com=fsv.getHomeDirectory();   
	static String urlDesk=com.getPath()+"\\";
	
	public static void outExcel(HttpServletResponse response, List<Object[]> list, String filename, int num) {
		try {
			File exportFile=new File(urlDesk+filename+DF.format(new Date())+".xls");
			if(!exportFile.exists()) {
				exportFile.createNewFile();
			}
			HSSFWorkbook workbook = new HSSFWorkbook();
			createNextSheet(workbook, list, 1, num);
			//使用输出流导出结果
			FileOutputStream fos= new FileOutputStream(exportFile);
			workbook.write(fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建表单页，承接结果
	 * @param workbook
	 * @param list
	 * @param index
	 * @param SheetIndex
	 */
	public static void createNextSheet(HSSFWorkbook workbook, List<Object[]> list, int sheetIndex, int num) {
		HSSFSheet sheet=SetExcelTitle(workbook,sheetIndex,num);
		HSSFCellStyle style = workbook.createCellStyle();	
		Font font = workbook.createFont();	
		//设置导出历史监测数据的主体（设置主体内容从第三行开始）
		int cols = num*3+3;
		for(int i = 2; i < list.size()+2; i++) {
			if(i < 30002) {
				HSSFRow row = sheet.createRow(i);
				Object[] obj = list.get(i-2);
				for(int j = 0; j < obj.length; j++) {
					HSSFCell cell = row.createCell(j);
					Object o = obj[j];
					if(obj[j] == null) { cell.setCellValue(""); }
					else if(o instanceof Date) {cell.setCellValue(SDF.format((Date)obj[j])); }
					else if(o instanceof BigDecimal) {cell.setCellValue(((BigDecimal)obj[j]).doubleValue()); }
					else if(o instanceof Integer) {cell.setCellValue((Integer)obj[j]); }
					else if(o instanceof Number) {cell.setCellValue((Double)obj[j]); }
					else {cell.setCellValue(obj[j].toString()); }
					cell.setCellStyle(setUserCellStyle(style,font,false,14));
				}
			}else {
				list = list.subList(30000, list.size());
				createNextSheet(workbook, list, sheetIndex+1, num);
				break;
			}
		}
		sheet.setDefaultRowHeight((short) 20); 	//设置行高
		for(int i = 0; i < cols; i++) {
			sheet.autoSizeColumn(i,true); 			// 调整宽度
		}
	}
	
	
	/**
	 * 设置导出数据的标题
	 * @param workbook
	 * @param sheetIndex
	 * @param num
	 * @return
	 */
	private static HSSFSheet SetExcelTitle(HSSFWorkbook workbook,int sheetIndex,int num) {
		HSSFSheet sheet = workbook.createSheet("第"+sheetIndex+"页");	//数据表
		//设置导出历史监测数据的表头（设置前两行为表头内容）
		HSSFRow row1 = sheet.createRow(0);
		HSSFRow row2 = sheet.createRow(1);
		HSSFCell first=row1.createCell(0);
		HSSFCell second=row1.createCell(num*3+1);
		HSSFCell last=row1.createCell(num*3+2);
		//设置样式【开始】
		HSSFCellStyle style = workbook.createCellStyle();	
		Font font = workbook.createFont();	
		HSSFCellStyle tstyle = setUserCellStyle(style,font,true,16);	
		//设置样式【结束】
		first.setCellValue("设备名称");
		second.setCellValue("运行状态");
		last.setCellValue("采集时间");
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, num*3+1, num*3+1));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, num*3+2, num*3+2));
		first.setCellStyle(tstyle);
		second.setCellStyle(tstyle);
		last.setCellStyle(tstyle);
		for(int m=1; m<num*3; m+=3) {
			HSSFCell tCell10 = row1.createCell(m);
			HSSFCell tCell11 = row1.createCell(m+1);
			HSSFCell tCell12 = row1.createCell(m+2);
			HSSFCell tCell20 = row2.createCell(m);
			HSSFCell tCell21 = row2.createCell(m+1);
			HSSFCell tCell22 = row2.createCell(m+2);
			tCell10.setCellValue("CH"+(m+2)/3);
			tCell11.setCellValue("CH"+(m+2)/3);
			tCell12.setCellValue("CH"+(m+2)/3);
			tCell20.setCellValue("温度");
			tCell21.setCellValue("PD");
			tCell22.setCellValue("UV");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, m, m+2));
			tCell10.setCellStyle(tstyle);
			tCell11.setCellStyle(tstyle);
			tCell12.setCellStyle(tstyle);
			tCell20.setCellStyle(tstyle);
			tCell21.setCellStyle(tstyle);
			tCell22.setCellStyle(tstyle);
		}
		return sheet;
	}

	/**
	 * 设置字体样式
	 * @param workbook 
	 * @param ustyle		样式对象
	 * @param ufont		字体对象
	 * @param isTitle		是否为表头
	 * @param fontsize	字体大小
	 * @return
	 */
	private static HSSFCellStyle setUserCellStyle(HSSFCellStyle style, Font font, boolean isTitle, int fontsize) {
		//设置样式
		style.setAlignment(HorizontalAlignment.CENTER);			//水平居中
		style.setVerticalAlignment(VerticalAlignment.CENTER);	//垂直居中
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		//设置字体
		font.setFontName("宋体");
		font.setBold(isTitle);
		font.setFontHeightInPoints((short) fontsize);
		style.setFont(font);
		style.setLocked(isTitle);
		return style;
	}

	
}