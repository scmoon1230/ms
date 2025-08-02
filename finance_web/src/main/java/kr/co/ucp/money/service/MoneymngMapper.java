package kr.co.ucp.money.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("moneymngMapper")
public interface MoneymngMapper {

	List<Map<String, String>> selectMoneyMng(Map<String, String> args) throws Exception;

	String selectTotalAmnt(Map<String, String> args) throws Exception;

	Map<String, String> selectMastMoneyOne(Map<String, String> args) throws Exception;

	int insertMoneyMngById(Map<String, Object> args) throws Exception;

	int insertMoneyMngByName(Map<String, Object> args) throws Exception;

	int updateMoneyMng(Map<String, Object> args) throws Exception;

	int modifyMoneyMng(Map<String, String> args) throws Exception;
	
	int deleteMoneyMng(Map<String, String> args) throws Exception;

}
