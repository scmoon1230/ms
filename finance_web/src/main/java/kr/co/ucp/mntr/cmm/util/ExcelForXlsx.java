package kr.co.ucp.mntr.cmm.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.ucp.cmm.util.ExcelValue;

@SuppressWarnings("rawtypes")
public class ExcelForXlsx extends ExcelUtil {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private XSSFWorkbook work = null;

	public boolean isExcel(String excelFile) {
		try {
			work = new XSSFWorkbook(OPCPackage.open(new FileInputStream(new File(excelFile))));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List read(String excelFile) throws Exception {
		List<Map<Integer, Object>> list = new ArrayList<Map<Integer, Object>>();

		int sheetNum = work.getNumberOfSheets();
		int totCells = 0;
		for (int loop = 0; loop < sheetNum; loop++) {
			XSSFSheet sheet = work.getSheetAt(loop);
			int rows = sheet.getPhysicalNumberOfRows();

			for (int rownum = 0; rownum < rows; rownum++) {
				XSSFRow row = sheet.getRow(rownum);
				String str = "";
				boolean isNull = false;

				if (rownum == 0) {
					totCells = row.getPhysicalNumberOfCells();
				}

				if (row != null) {
					// int cells = row.getPhysicalNumberOfCells();
					Map<Integer, Object> params = new HashMap<Integer, Object>();

					for (int cellnum = 0; cellnum < totCells; cellnum++) {
                        Object objValue = ExcelValue.getObjCellVal(row.getCell(cellnum));
                        if (objValue != null) {
                            params.put(cellnum, objValue);
                            str = str + "";
                        }
					}
					list.add(params);
				}
			}
		}

		return list;
	}

	public List read_old(String excelFile) throws Exception {
		List<Map<Integer, Object>> list = new ArrayList<Map<Integer, Object>>();

		int sheetNum = work.getNumberOfSheets();
		int totCells = 0;
		for (int loop = 0; loop < sheetNum; loop++) {
			XSSFSheet sheet = work.getSheetAt(loop);
			int rows = sheet.getPhysicalNumberOfRows();

			for (int rownum = 0; rownum < rows; rownum++) {
				XSSFRow row = sheet.getRow(rownum);
				String str = new String();
				boolean isNull = false;

				if (rownum == 0) {
					totCells = row.getPhysicalNumberOfCells();
				}

				if (row != null) {
					// int cells = row.getPhysicalNumberOfCells();
					Map<Integer, Object> params = new HashMap<Integer, Object>();

					for (int cellnum = 0; cellnum < totCells; cellnum++) {
						XSSFCell cell = row.getCell(cellnum);
						isNull = false;
						// logger.debug("=================ExcelForXlsx============ cellnum ="+cellnum+"["+cell+"]");
						if (cell != null) {
                            Object objValue = ExcelValue.getObjCellVal(cell);
                            params.put(cellnum, objValue);
                            
//							switch (cell.getCellType()) {
//								case XSSFCell.CELL_TYPE_NUMERIC:
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
//								case XSSFCell.CELL_TYPE_STRING:
//									params.put(cellnum, cell.getStringCellValue());
//									logger.debug("=================ExcelForXlsx==CELL_TYPE_STRING===getStringCellValue======= cellnum =" + cellnum + "[" + cell + "=" + cell.getStringCellValue() + "]");
//									break;
//								case XSSFCell.CELL_TYPE_FORMULA:
//									double cellTypeFormula = cell.getNumericCellValue();
//									if (cellTypeFormula == Math.floor(cellTypeFormula)) {
//										params.put(cellnum, (int)cell.getNumericCellValue());
//									} else {
//										params.put(cellnum, cell.getNumericCellValue());
//									}
//									logger.debug("=================ExcelForXlsx==CELL_TYPE_FORMULA===getNumericCellValue======= cellnum =" + cellnum + "[" + cell + "=" + cell.getNumericCellValue() + "]");
//									break;
//								case XSSFCell.CELL_TYPE_BLANK:
//									params.put(cellnum, "");
//									// logger.debug("=================ExcelForXlsx==CELL_TYPE_BLANK===blank======= cellnum ="+cellnum+"["+cell+"="+"]");
//									break;
//								case XSSFCell.CELL_TYPE_BOOLEAN:
//									params.put(cellnum, cell.getBooleanCellValue());
//									logger.debug("=================ExcelForXlsx==CELL_TYPE_BOOLEAN===getBooleanCellValue======= cellnum =" + cellnum + "[" + cell + "=" + cell.getBooleanCellValue() + "]");
//									break;
//								case XSSFCell.CELL_TYPE_ERROR:
//									params.put(cellnum, cell.getErrorCellValue());
//									logger.debug("=================ExcelForXlsx==CELL_TYPE_ERROR===getErrorCellValue======= cellnum =" + cellnum + "[" + cell + "=" + cell.getErrorCellValue() + "]");
//									break;
//								default:
//									isNull = true;
//									break;
//							}
							if (isNull != true) str = str + "";

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
