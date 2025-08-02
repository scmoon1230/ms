package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

public interface FinmonthService {

	List<Map<String, String>> selectFinmonth(Map<String, String> args) throws Exception;

}
