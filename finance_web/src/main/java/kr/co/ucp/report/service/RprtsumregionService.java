package kr.co.ucp.report.service;

import java.util.List;
import java.util.Map;

public interface RprtsumregionService {

	List<Map<String, String>> selectDeptRegion(Map<String, String> args) throws Exception;

	
//	Map<String, String> selectDurationYear(Map<String, String> args) throws Exception;

	Map<String, String> selectNextMonth(Map<String, String> args) throws Exception;
	
	List<Map<String, String>> selectMoneyMonth(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectMoneyYear(Map<String, String> args) throws Exception;
	
	String selectMonthAmnt(Map<String, String> args) throws Exception;

	String selectTotalAmnt(Map<String, String> args) throws Exception;

}
