package kr.co.ucp.wrks.evtctl.stats.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("cctvConnFcltMapper")
public interface CctvConnFcltMapper {
	List<Map<String, String>> selectMonthList(Map<String, String> params);

	List<Map<String, String>> selectDayList(Map<String, String> params);

	List<Map<String, String>> selectMonthExcel(Map<String, String> params);

	List<Map<String, String>> selectDayExcel(Map<String, String> params);
}
