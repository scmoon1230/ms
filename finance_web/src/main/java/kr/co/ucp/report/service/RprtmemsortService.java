package kr.co.ucp.report.service;

import java.util.List;
import java.util.Map;

public interface RprtmemsortService {

	List<Map<String, String>> selectRprtMemsort(Map<String, String> args) throws Exception;

	String selectRprtMemsortTotalAmnt(Map<String, String> args) throws Exception;

	
	
	List<Map<String, String>> selectRprtMemsorthigh(Map<String, String> args) throws Exception;

}
