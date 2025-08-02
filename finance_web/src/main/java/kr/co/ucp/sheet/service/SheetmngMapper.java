package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheetmngMapper")
public interface SheetmngMapper {

	// 전표관리
	List<Map<String, String>> selectSheetMng(Map<String, String> args) throws Exception;

	String selectSheetMngTotalAmnt(Map<String, String> args) throws Exception;

	Map<String, String> selectNewSeqNo(Map<String, String> args) throws Exception;

	int insertSheetMng(Map<String, String> args) throws Exception;

	int updateSheetMng(Map<String, String> args) throws Exception;

	int updateSheetMngCloseYn(Map<String, String> args) throws Exception;

	int deleteSheetMng(Map<String, String> args) throws Exception;

	
	// 전표마감 주간합계
	Map<String, String> selectWeekSum(Map<String, String> args) throws Exception;
	
	Map<String, String> selectSheetCloseCnt(Map<String, String> args) throws Exception;

	Map<String, String> selectNewWeekSum(Map<String, String> args) throws Exception;

	int insertWeekSum(Map<String, String> args) throws Exception;
	
	int deleteWeekSum(Map<String, String> args) throws Exception;


	// 전표마감 월간합계
	List<Map<String, String>> selectNewMonthSumList(Map<String, String> args) throws Exception;

	Map<String, String> selectNewMonthSum(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectSumAcctCodeList(Map<String, String> args) throws Exception;
	
	int mergeMonthSum(Map<String, String> args) throws Exception;

	int deleteMonthSum(Map<String, String> args) throws Exception;

	
	// 기타
	Map<String, String> selectPrevStartEndDay(Map<String, String> args) throws Exception;

	Map<String, String> selectNextStartEndDay(Map<String, String> args) throws Exception;

}
