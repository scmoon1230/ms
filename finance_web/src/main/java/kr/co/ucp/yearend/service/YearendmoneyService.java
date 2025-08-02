package kr.co.ucp.yearend.service;

import java.util.List;
import java.util.Map;

public interface YearendmoneyService {

	List<Map<String, String>> selectMoney(Map<String, String> args) throws Exception;

}
