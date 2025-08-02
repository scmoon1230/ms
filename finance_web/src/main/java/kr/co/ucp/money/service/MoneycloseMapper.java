package kr.co.ucp.money.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("moneycloseMapper")
public interface MoneycloseMapper {

	Map<String, String> selectStartEndDay(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectMoneyClose(Map<String, String> args) throws Exception;

	String selectMoneyCloseTotalAmnt(Map<String, String> args) throws Exception;

	Map<String, String> selectMoneyCloseCnt(Map<String, String> args) throws Exception;

	int updateMoneyClose(Map<String, String> args) throws Exception;

}
