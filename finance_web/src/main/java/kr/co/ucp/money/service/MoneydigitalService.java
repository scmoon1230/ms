package kr.co.ucp.money.service;

import java.util.List;
import java.util.Map;

public interface MoneydigitalService {

	List<Map<String, String>> selectMastUpload(Map<String, String> args) throws Exception;

	String selectTotalAmnt(Map<String, String> args) throws Exception;

	int insertMastUpload(Map<String, String> args) throws Exception;

//	int updateMastUpload(Map<String, Object> args) throws Exception;

	int deleteMastUpload(Map<String, String> args) throws Exception;

	int deleteMastUploadMulti(List<Map<String, String>> args) throws Exception;

	Map<String, Object> excelUpload(List<Map<String, String>> args) throws Exception;

	int insertMastMoneyMulti(List<Map<String, String>> args) throws Exception;

	int deleteMastMoneyMulti(List<Map<String, String>> args) throws Exception;

	
}
