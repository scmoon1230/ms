package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

public interface DigitalmoneyService {

	List<Map<String, String>> selectDigitalmoney(Map<String, String> args) throws Exception;

	int insertDigitalmoney(Map<String, Object> args) throws Exception;

	int updateDigitalmoney(Map<String, Object> args) throws Exception;

	int deleteDigitalmoney(Map<String, String> args) throws Exception;

	int deleteDigitalmoneyMulti(List<Map<String, String>> args) throws Exception;

}
