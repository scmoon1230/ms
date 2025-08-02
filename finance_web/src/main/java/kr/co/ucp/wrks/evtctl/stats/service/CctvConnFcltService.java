package kr.co.ucp.wrks.evtctl.stats.service;

import java.util.List;
import java.util.Map;

public interface CctvConnFcltService {
	List<Map<String, String>> getList(Map<String, String> args) throws Exception;

	List<Map<String, String>> getExcel(Map<String, String> args) throws Exception;
}
