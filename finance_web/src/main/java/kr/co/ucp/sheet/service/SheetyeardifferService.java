package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

public interface SheetyeardifferService {

	List<Map<String, String>> selectSheetyeardiffer(Map<String, String> args) throws Exception;

	int insertSheet(Map<String, Object> args) throws Exception;

	int updateSheet(Map<String, Object> args) throws Exception;

	int deleteSheet(Map<String, String> args) throws Exception;

	int deleteSheetMulti(List<Map<String, String>> args) throws Exception;
	
}
