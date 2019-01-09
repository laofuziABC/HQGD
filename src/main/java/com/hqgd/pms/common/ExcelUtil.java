/**
 * 
 */
package com.hqgd.pms.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hqgd.pms.domain.EquipmentInfo;

/**
 * @author Hongten
 * @created 2014-5-20
 */
public class ExcelUtil {

	public static void writeExcel(List<EquipmentInfo> list, String path) throws Exception {
		if (list == null) {
			return;
		} else if (path == null || Common.EMPTY.equals(path)) {
			return;
		} else {
			String postfix = CommonUtil.getPostfix(path);
			if (!Common.EMPTY.equals(postfix)) {
				if (Common.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					writeXls(list, path);
				} else if (Common.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					writeXlsx(list, path);
				}
			} else {
				System.out.println(path + Common.NOT_EXCEL_FILE);
			}
		}
	}

	/**
	 * read the Excel file
	 * 
	 * @param path
	 *            the path of the Excel file
	 * @return
	 * @throws IOException
	 */
	public static List<EquipmentInfo> readExcel(String path) throws IOException {
		if (path == null || Common.EMPTY.equals(path)) {
			return null;
		} else {
			String postfix = CommonUtil.getPostfix(path);
			if (!Common.EMPTY.equals(postfix)) {
				if (Common.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
					return readXls(path);
				} else if (Common.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
					return readXlsx(path);
				}
			} else {
				System.out.println(path + Common.NOT_EXCEL_FILE);
			}
		}
		return null;
	}

	/**
	 * Read the Excel 2010
	 * 
	 * @param path
	 *            the path of the excel file
	 * @return
	 * @throws IOException
	 */
	public static List<EquipmentInfo> readXlsx(String path) throws IOException {
		System.out.println(Common.PROCESSING + path);
		InputStream is = new FileInputStream(path);
		@SuppressWarnings("resource")
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		EquipmentInfo equipmentInfo = null;
		List<EquipmentInfo> list = new ArrayList<EquipmentInfo>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					equipmentInfo = new EquipmentInfo();
					XSSFCell equipmentId = xssfRow.getCell(0);
					XSSFCell frameStru = xssfRow.getCell(1);

					equipmentInfo.setEquipmentId(equipmentId.toString());
					;
					equipmentInfo.setFrameStru(frameStru.toString());

					list.add(equipmentInfo);
				}
			}
		}
		return list;
	}

	/**
	 * Read the Excel 2003-2007
	 * 
	 * @param path
	 *            the path of the Excel
	 * @return
	 * @throws IOException
	 */
	public static List<EquipmentInfo> readXls(String path) throws IOException {
		System.out.println(Common.PROCESSING + path);
		InputStream is = new FileInputStream(path);
		@SuppressWarnings("resource")
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		EquipmentInfo EquipmentInfo = null;
		List<EquipmentInfo> list = new ArrayList<EquipmentInfo>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// Read the Row
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					EquipmentInfo = new EquipmentInfo();
					HSSFCell equipmentId = hssfRow.getCell(1);
					HSSFCell frameStru = hssfRow.getCell(9);
					EquipmentInfo.setEquipmentId(equipmentId.toString());
					;
					EquipmentInfo.setFrameStru(frameStru.toString());
					list.add(EquipmentInfo);
				}
			}
		}
		return list;
	}

	@SuppressWarnings({ "static-access", "unused" })
	private String getValue(XSSFCell xssfRow) {
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

	@SuppressWarnings({ "static-access", "unused" })
	private String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	@SuppressWarnings("resource")
	public static void writeXls(List<EquipmentInfo> list, String path) throws Exception {
		if (list == null) {
			return;
		}
		int countColumnNum = list.size();
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet("EquipmentInfoSheet");
		// option at first row.
		HSSFRow firstRow = sheet.createRow(0);
		HSSFCell[] firstCells = new HSSFCell[countColumnNum];
		String[] options = { "id", "equipmentId", "equipmentName", "userName", "userId", "channelTem", "address",
				"adcode", "cadcode", "padcode", "lngLat", "frameStru", "icon", "creator", "createTime", "updater",
				"updateTime" };
		for (int j = 0; j < options.length; j++) {
			firstCells[j] = firstRow.createCell(j);
			firstCells[j].setCellValue(new HSSFRichTextString(options[j]));
		}
		//
		for (int i = 0; i < countColumnNum; i++) {
			HSSFRow row = sheet.createRow(i + 1);
			EquipmentInfo EquipmentInfo = list.get(i);
			for (int column = 0; column < options.length; column++) {
				HSSFCell EquipmentId = row.createCell(1);
				HSSFCell FrameStru = row.createCell(11);
				EquipmentId.setCellValue(EquipmentInfo.getEquipmentId());
				FrameStru.setCellValue(EquipmentInfo.getFrameStru());

			}
		}
		File file = new File(path);
		OutputStream os = new FileOutputStream(file);
		System.out.println(Common.WRITE_DATA + path);
		book.write(os);
		os.close();
	}

	public static void writeXlsx(List<EquipmentInfo> list, String path) throws Exception {
		if (list == null) {
			return;
		}
		// XSSFWorkbook
		int countColumnNum = list.size();
		@SuppressWarnings("resource")
		XSSFWorkbook book = new XSSFWorkbook();
		XSSFSheet sheet = book.createSheet("EquipmentInfoSheet");
		// option at first row.
		XSSFRow firstRow = sheet.createRow(0);
		XSSFCell[] firstCells = new XSSFCell[17];
		String[] options = { "id", "equipmentId", "equipmentName", "userName", "userId", "channelTem", "address",
				"adcode", "cadcode", "padcode", "lngLat", "frameStru", "icon", "creator", "createTime", "updater",
				"updateTime" };
		for (int j = 0; j < options.length; j++) {
			firstCells[j] = firstRow.createCell(j);
			firstCells[j].setCellValue(new XSSFRichTextString(options[j]));
		}
		//
		for (int i = 0; i < countColumnNum; i++) {
			XSSFRow row = sheet.createRow(i + 1);
			EquipmentInfo EquipmentInfo = list.get(i);
			for (int column = 0; column < options.length; column++) {
				XSSFCell EquipmentId = row.createCell(1);
				XSSFCell FrameStru = row.createCell(11);
				EquipmentId.setCellValue(EquipmentInfo.getEquipmentId());
				FrameStru.setCellValue(EquipmentInfo.getFrameStru());
			}
		}
		File file = new File(path);
		if (!file.exists()) {
			file.getParentFile().mkdir();
		}
		OutputStream os = new FileOutputStream(file);
		System.out.println(Common.WRITE_DATA + path);
		book.write(os);
		os.close();
	}
}
