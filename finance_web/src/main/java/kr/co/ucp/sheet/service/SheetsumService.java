package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

public interface SheetsumService {

	List<Map<String, String>> selectSheetSum(Map<String, String> args) throws Exception;

}
