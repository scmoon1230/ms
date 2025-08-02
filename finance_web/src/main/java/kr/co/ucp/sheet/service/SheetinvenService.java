package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

public interface SheetinvenService {

	List<Map<String, String>> selectSheetInven(Map<String, String> args) throws Exception;

	String selectSheetInvenTotalAmnt(Map<String, String> args) throws Exception;

}
