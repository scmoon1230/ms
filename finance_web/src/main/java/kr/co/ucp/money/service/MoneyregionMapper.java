package kr.co.ucp.money.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("moneyregionMapper")
public interface MoneyregionMapper {

	List<Map<String, String>> selectMoneyRegion(Map<String, String> args) throws Exception;

	String selectTotalAmnt(Map<String, String> args) throws Exception;

	int insertMoney(Map<String, Object> args) throws Exception;

	int updateMoney(Map<String, Object> args) throws Exception;

	int deleteMoneyRegion(Map<String, String> args) throws Exception;

}
