package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

public interface SheetsumyearService {

	Map<String, String> selectDurationYear(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectSheetSumyear(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectTotalAmnt(Map<String, String> args) throws Exception;

}
