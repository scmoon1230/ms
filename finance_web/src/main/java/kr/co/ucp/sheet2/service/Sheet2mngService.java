package kr.co.ucp.sheet2.service;

import java.util.List;
import java.util.Map;

public interface Sheet2mngService {

	List<Map<String, String>> selectSheet2Mng(Map<String, String> args) throws Exception;

	String selectSheet2MngTotalAmnt(Map<String, String> args) throws Exception;

	int doClose(Map<String, String> args) throws Exception;

	int cancelClose(Map<String, String> args) throws Exception;

//	int insertSheet(Map<String, Object> args) throws Exception;
//
//	int updateSheet(Map<String, Object> args) throws Exception;
//
//	int deleteSheet(Map<String, String> args) throws Exception;
//
//	int deleteSheetMulti(List<Map<String, String>> args) throws Exception;
	
}
