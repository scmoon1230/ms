package kr.co.ucp.wrks.evtctl.stats.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("cctvConnCrtStateMapper")
public interface CctvConnCrtStateMapper {

	List<Map<String, String>> getYearList();

	List<Map<String, String>> getEventList(Map<String, Object> args);

	List<Map<String, String>> getMonthTypeList(Map<String, String> args);

	List<Map<String, String>> getMonthTypeExcel(Map<String, String> args);

	List<Map<String, String>> getMonthUserList(Map<String, String> args);

	List<Map<String, String>> getMonthUserExcel(Map<String, String> args);

	List<Map<String, String>> getTearmTypeList(Map<String, String> args);

	List<Map<String, String>> getTearmTypeExcel(Map<String, String> args);

	List<Map<String, String>> getTearmUserList(Map<String, String> args);

	List<Map<String, String>> getTearmUserExcel(Map<String, String> args);

	List<Map<String, String>> getConnDetailList(Map<String, String> args);

	List<Map<String, String>> getConnDetailExcel(Map<String, String> args);

}
