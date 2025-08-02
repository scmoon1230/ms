package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("finmngMapper")
public interface FinmngMapper {

	List<Map<String, String>> selectFinance(Map<String, String> args) throws Exception;
	
	//Map<String, String> selectFinanceOne(Map<String, String> args) throws Exception;

	int insertFinance(Map<String, Object> args) throws Exception;

	int updateFinance(Map<String, Object> args) throws Exception;

	int deleteFinance(Map<String, String> args) throws Exception;

}
