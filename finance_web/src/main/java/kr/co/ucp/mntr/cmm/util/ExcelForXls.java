package kr.co.ucp.mntr.cmm.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.ucp.cmm.util.ExcelValue;

public class ExcelForXls extends ExcelUtil {

	private POIFSFileSystem fileSystem = null;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public boolean isExcel(String excelFile) {
		try {
			fileSystem = new POIFSFileSystem(new FileInputStream(new File(excelFile)));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public List<Map<Integer, Object>> read(String excelFile) throws Exception {
		List<Map<Integer, Object>> list = new ArrayList<Map<Integer, Object>>();
		HSSFWorkbook work = new HSSFWorkbook(fileSystem);
		int sheetNum = work.getNumberOfSheets();
		int totCells = 0;
		for (int loop = 0; loop < sheetNum; loop++) {
			HSSFSheet sheet = work.getSheetAt(loop);
			if (sampleSheetName.equals(sheet.getSheetName())) continue;

			int rows = sheet.getPhysicalNumberOfRows();

			for (int rownum = 0; rownum < rows; rownum++) {
				HSSFRow row = sheet.getRow(rownum);

				if (rownum == 0 && row.getPhysicalNumberOfCells() != 0) {
					totCells = row.getPhysicalNumberOfCells();
				}

				if (row != null) {
					// int cells = row.getPhysicalNumberOfCells();
					Map<Integer, Object> params = new HashMap<Integer, Object>();

					for (int cellnum = 0; cellnum < totCells; cellnum++) {
                        Object objValue = ExcelValue.getObjCellVal(row.getCell(cellnum));
                        if (objValue != null) {
                            params.put(cellnum, objValue);
                        }
					}
					list.add(params);
				}
			}
		}
		return list;
	}
	public List<Map<Integer, Object>> read_old(String excelFile) throws Exception {
		List<Map<Integer, Object>> list = new ArrayList<Map<Integer, Object>>();
		HSSFWorkbook work = new HSSFWorkbook(fileSystem);
		int sheetNum = work.getNumberOfSheets();
		int totCells = 0;
		for (int loop = 0; loop < sheetNum; loop++) {
			HSSFSheet sheet = work.getSheetAt(loop);
			if (sampleSheetName.equals(sheet.getSheetName())) continue;

			int rows = sheet.getPhysicalNumberOfRows();

			for (int rownum = 0; rownum < rows; rownum++) {
				HSSFRow row = sheet.getRow(rownum);

				if (rownum == 0 && row.getPhysicalNumberOfCells() != 0) {
					totCells = row.getPhysicalNumberOfCells();
				}

				if (row != null) {
					// int cells = row.getPhysicalNumberOfCells();
					Map<Integer, Object> params = new HashMap<Integer, Object>();

					for (int cellnum = 0; cellnum < totCells; cellnum++) {
						HSSFCell cell = row.getCell(cellnum);
						// logger.debug("=================ExcelForXls============ cellnum ="+cellnum+"["+cell+"]");
						if (cell != null) {
//							switch (cell.getCellType()) {
//								case HSSFCell.CELL_TYPE_NUMERIC:
//									// cell.setCellType( XSSFCell.CELL_TYPE_STRING );
//									// params.put(cellnum, cell.getStringCellValue());
//									double cellTypeNumeric = cell.getNumericCellValue();
//									if (cellTypeNumeric == Math.floor(cellTypeNumeric)) {
//										params.put(cellnum, (int)cell.getNumericCellValue());
//									} else {
//										params.put(cellnum, cell.getNumericCellValue());
//									}
//									logger.debug("=================ExcelForXlsx==CELL_TYPE_NUMERIC===getNumericCellValue======= cellnum =" + cellnum + "[" + cell + "=" + cell.getNumericCellValue() + "]");
//									break;
//								case HSSFCell.CELL_TYPE_STRING:
//									params.put(cellnum, cell.getStringCellValue());
//									break;
//								case HSSFCell.CELL_TYPE_FORMULA:
//									double cellTypeFormula = cell.getNumericCellValue();
//									if (cellTypeFormula == Math.floor(cellTypeFormula)) {
//										params.put(cellnum, (int)cell.getNumericCellValue());
//									} else {
//										params.put(cellnum, cell.getNumericCellValue());
//									}
//									break;
//								case HSSFCell.CELL_TYPE_BLANK:
//									params.put(cellnum, "");
//									break;
//								case HSSFCell.CELL_TYPE_BOOLEAN:
//									params.put(cellnum, cell.getBooleanCellValue());
//									break;
//								case HSSFCell.CELL_TYPE_ERROR:
//									params.put(cellnum, cell.getErrorCellValue());
//									break;
//								default:
//									params.put(cellnum, "");
//									break;
//							}
						} else {
							params.put(cellnum, "");
						}
					}
					list.add(params);
				}
			}
		}
		return list;
	}

	@Override
	public void write() throws Exception {

	}

}
