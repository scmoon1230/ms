package kr.co.ucp.money.service;

import java.util.List;
import java.util.Map;

public interface MoneymngService {

	List<Map<String, String>> selectMoneyMng(Map<String, String> args) throws Exception;

	String selectTotalAmnt(Map<String, String> args) throws Exception;

	int insertMoneyMng(Map<String, Object> args) throws Exception;

	int updateMoneyMng(Map<String, Object> args) throws Exception;

	int deleteMoneyMng(Map<String, String> args) throws Exception;

	int deleteMoneyMngMulti(List<Map<String, String>> args) throws Exception;

	int modifyMoneyMngMulti(List<Map<String, String>> args) throws Exception;

	Map<String, Object> excelUpload(List<Map<String, String>> args) throws Exception;

}
