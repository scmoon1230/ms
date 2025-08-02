/**
 * ----------------------------------------------------------------------------------------------
 *
 * @Class Name : CommUtil.java
 * @Description : 공통유틸리티
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015.01.08.   widecube Space  최초작성
 * <p>
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.cmm.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelValue {
	static Logger logger = LoggerFactory.getLogger(ExcelValue.class);

	public static String getStrCellVal(Cell cell) {
        String cellValue = "";
        try {
            if (cell == null) return "";

            CellType cellType = cell.getCellType();

            if (cellType == CellType.STRING) {
                cellValue = cell.getStringCellValue();
            } else if (cellType == CellType.NUMERIC) {
                double cellTypeNumeric = cell.getNumericCellValue();
                if (cellTypeNumeric == (long) cellTypeNumeric) {
                    cellValue = String.format("%d", (long)cellTypeNumeric);
                } else {
                    cellValue = String.format("%s", cellTypeNumeric);
                }
            } else if (cellType == CellType.FORMULA) {
            	cellValue = String.valueOf(cell.getNumericCellValue());
                double cellTypeFormula = cell.getNumericCellValue();
                if (cellTypeFormula == (long) cellTypeFormula) {
                    cellValue = String.format("%d", (long)cellTypeFormula);
                } else {
                    cellValue = String.format("%s", cellTypeFormula);
                }
            } else if (cellType == CellType.BLANK) {
                cellValue = "";
            } else if (cellType == CellType.BOOLEAN) {
                cellValue = String.valueOf(cell.getBooleanCellValue());
            } else if (cellType == CellType.ERROR) {
                cellValue = String.valueOf(cell.getErrorCellValue());
            } else {
                cellValue = "";
            }
//            logger.debug("====value::::{},{}", cellType.name(), cellValue);
        } catch (Exception e) {
          logger.error("====Exception::::{}", e.getMessage());
        }
        return cellValue.trim();
    }

	public static Object getObjCellVal(Cell cell) {
		Object cellValue = null;
        if (cell == null) return cellValue;

		switch (cell.getCellType()) {
			case  STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				double cellTypeNumeric = cell.getNumericCellValue();
                if (cellTypeNumeric == (long) cellTypeNumeric) {
                    return String.format("%d", (long)cellTypeNumeric);
                } else {
                	return String.format("%s", cellTypeNumeric);
                }
			case FORMULA:
				double cellTypeFormula = cell.getNumericCellValue();
                if (cellTypeFormula == (long) cellTypeFormula) {
                    return String.format("%d", (long)cellTypeFormula);
                } else {
                	return String.format("%s", cellTypeFormula);
                }
			case BLANK:
				return "";
			case BOOLEAN:
				return cell.getBooleanCellValue();
			case ERROR:
				return cell.getErrorCellValue();
			default:
				return cellValue;
		}
    }
}
