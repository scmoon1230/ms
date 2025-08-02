package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

public interface SheetdurationService {

	List<Map<String, String>> selectSheetDuration(Map<String, String> args) throws Exception;

	String selectSheetDurationTotalAmnt(Map<String, String> args) throws Exception;

}
