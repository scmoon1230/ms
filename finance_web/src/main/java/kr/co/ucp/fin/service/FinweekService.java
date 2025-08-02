package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

public interface FinweekService {

	List<Map<String, String>> selectFinweek(Map<String, String> args) throws Exception;
	
}
