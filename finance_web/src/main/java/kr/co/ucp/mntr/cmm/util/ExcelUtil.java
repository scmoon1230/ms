package kr.co.ucp.mntr.cmm.util;

import java.util.List;

@SuppressWarnings("rawtypes")
public abstract class ExcelUtil {

	@SuppressWarnings("unused")
	private ExcelUtil exlUtil;
	public String sampleSheetName = "sample";

	public static ExcelUtil getInstance(String excelFile) {
		String fileExt = excelFile.substring(excelFile.lastIndexOf(".") + 1, excelFile.length());
		return fileExt.equals("xls") ? new ExcelForXls() : new ExcelForXlsx();
	}

	public abstract boolean isExcel(String excelFile);

	public abstract List read(String excelFile) throws Exception;

	public abstract void write() throws Exception;
}
