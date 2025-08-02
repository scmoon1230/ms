package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

public interface FintransService {

	List<Map<String, String>> selectFintrans(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectTotalAmnt(Map<String, String> args) throws Exception;

	int insertFintrans(Map<String, Object> args) throws Exception;

	int updateFintrans(Map<String, String> args) throws Exception;

	int deleteFintrans(Map<String, String> args) throws Exception;

	int deleteFintransMulti(List<Map<String, String>> args) throws Exception;

	Map<String, String> selectFintransCloseCnt(Map<String, String> args) throws Exception;
	
	int doClose(Map<String, String> args) throws Exception;

	int cancelClose(Map<String, String> args) throws Exception;

}
