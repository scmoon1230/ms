package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

public interface SheetmngService {

	List<Map<String, String>> selectSheetMng(Map<String, String> args) throws Exception;

	String selectSheetMngTotalAmnt(Map<String, String> args) throws Exception;

	int insertSheetMng(Map<String, String> args) throws Exception;

	int updateSheetMng(Map<String, String> args) throws Exception;

	int deleteSheetMng(Map<String, String> args) throws Exception;


	Map<String, String> selectWeekSum(Map<String, String> args) throws Exception;
	
	Map<String, String> selectSheetCloseCnt(Map<String, String> args) throws Exception;
	
	int doClose(Map<String, String> args) throws Exception;

	int cancelClose(Map<String, String> args) throws Exception;

	
	Map<String, String> selectPrevStartEndDay(Map<String, String> args) throws Exception;

	Map<String, String> selectNextStartEndDay(Map<String, String> args) throws Exception;

}
