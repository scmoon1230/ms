package kr.co.ucp.sheet2.service;

import java.util.List;
import java.util.Map;

public interface Sheet2summonthService {

	List<Map<String, String>> selectSheet2Summonth(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectTotalAmnt(Map<String, String> args) throws Exception;

	int insertSheet(Map<String, Object> args) throws Exception;

	int updateSheet(Map<String, Object> args) throws Exception;

	int deleteSheet(Map<String, String> args) throws Exception;

	int deleteSheetMulti(List<Map<String, String>> args) throws Exception;
	
}
