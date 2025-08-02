package kr.co.ucp.report.service;

import java.util.List;
import java.util.Map;

public interface RprtmoneyService {

	List<Map<String, String>> selectRprtMoney(Map<String, String> args) throws Exception;

	String selectRprtMoneyTotalAmnt(Map<String, String> args) throws Exception;

	int insertMoney(Map<String, Object> args) throws Exception;

	int updateMoney(Map<String, Object> args) throws Exception;

	int deleteMoney(Map<String, String> args) throws Exception;

	int deleteMoneyMulti(List<Map<String, String>> args) throws Exception;
	
}
