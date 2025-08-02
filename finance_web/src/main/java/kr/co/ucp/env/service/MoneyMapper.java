package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("moneyMapper")
public interface MoneyMapper {

	List<Map<String, String>> selectMoney(Map<String, String> args) throws Exception;

	int insertMoney(Map<String, Object> args) throws Exception;

	int updateMoney(Map<String, Object> args) throws Exception;

	int deleteMoney(Map<String, String> args) throws Exception;

}
