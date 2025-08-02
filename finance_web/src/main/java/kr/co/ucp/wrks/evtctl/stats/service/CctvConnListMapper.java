package kr.co.ucp.wrks.evtctl.stats.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("cctvConnListMapper")
public interface CctvConnListMapper {

	List<Map<String, String>> cctvConnList(Map<String, String> args);

	List<Map<String, String>> cctvConnListExcel(Map<String, String> args);

	List<Map<String, String>> cctvConnMonth(Map<String, String> args);

	List<Map<String, String>> cctvConnMonthExcel(Map<String, String> args);

	List<Map<String, String>> selectMonthList(Map<String, String> params);

	List<Map<String, String>> selectDayList(Map<String, String> params);

	List<Map<String, String>> selectMonthExcel(Map<String, String> params);

	List<Map<String, String>> selectDayExcel(Map<String, String> params);

}
