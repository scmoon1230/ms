package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheetmonthMapper")
public interface SheetmonthMapper {

	Map<String, String> selectDurationMonth(Map<String, String> args) throws Exception;

	Map<String, String> selectDurationSumm(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectMoneyMonth(Map<String, String> args) throws Exception;

	int insertMoney(Map<String, Object> args) throws Exception;

	int updateMoney(Map<String, Object> args) throws Exception;

	int deleteMoney(Map<String, String> args) throws Exception;

}
