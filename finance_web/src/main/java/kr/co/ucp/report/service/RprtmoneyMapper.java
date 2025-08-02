package kr.co.ucp.report.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("rprtmoneyMapper")
public interface RprtmoneyMapper {

	List<Map<String, String>> selectRprtMoney(Map<String, String> args) throws Exception;

	String selectRprtMoneyTotalAmnt(Map<String, String> args) throws Exception;

	int insertMoney(Map<String, Object> args) throws Exception;

	int updateMoney(Map<String, Object> args) throws Exception;

	int deleteMoney(Map<String, String> args) throws Exception;

}
