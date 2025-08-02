package kr.co.ucp.report.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("rprtsumweekMapper")
public interface RprtsumweekMapper {

	Map<String, String> selectDurationLastWeek(Map<String, String> args) throws Exception;
	Map<String, String> selectDurationThisWeek(Map<String, String> args) throws Exception;
	Map<String, String> selectDurationSumm(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectMoneyLastWeek(Map<String, String> args) throws Exception;
	List<Map<String, String>> selectMoneyThisWeek(Map<String, String> args) throws Exception;
	List<Map<String, String>> selectMoneySumm(Map<String, String> args) throws Exception;

	String selectLastWeekAmnt(Map<String, String> args) throws Exception;
	String selectThisWeekAmnt(Map<String, String> args) throws Exception;
	String selectTotalAmnt(Map<String, String> args) throws Exception;

}
