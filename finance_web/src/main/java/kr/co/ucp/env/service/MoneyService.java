package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

public interface MoneyService {

	List<Map<String, String>> selectMoney(Map<String, String> args) throws Exception;

	int insertMoney(Map<String, Object> args) throws Exception;

	int updateMoney(Map<String, Object> args) throws Exception;

	int deleteMoney(Map<String, String> args) throws Exception;

	int deleteMoneyMulti(List<Map<String, String>> args) throws Exception;

}
