package kr.co.ucp.money.service;

import java.util.List;
import java.util.Map;

public interface MoneyamntService {

	List<Map<String, String>> selectMoney(Map<String, String> args) throws Exception;

	String selectTotalAmnt(Map<String, String> args) throws Exception;

	int insertMoney(Map<String, Object> args) throws Exception;

	int updateMoney(Map<String, Object> args) throws Exception;

	int deleteMoney(Map<String, String> args) throws Exception;

	int deleteMoneyMulti(List<Map<String, String>> args) throws Exception;
	
}
