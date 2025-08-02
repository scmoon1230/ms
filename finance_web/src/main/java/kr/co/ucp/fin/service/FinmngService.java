package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

public interface FinmngService {

	List<Map<String, String>> selectFinance(Map<String, String> args) throws Exception;

	int insertFinance(Map<String, Object> args) throws Exception;

	int updateFinance(Map<String, Object> args) throws Exception;

	int deleteFinance(Map<String, String> args) throws Exception;

	int deleteFinanceMulti(List<Map<String, String>> args) throws Exception;

}
