package kr.co.ucp.yearend.service;

import java.util.List;
import java.util.Map;

public interface YearendhometaxService {

	List<Map<String, String>> stanYyList(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectMoney(Map<String, String> args) throws Exception;

}
