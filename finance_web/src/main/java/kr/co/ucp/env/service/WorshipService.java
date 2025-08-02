package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

public interface WorshipService {

	List<Map<String, String>> selectWorship(Map<String, String> args) throws Exception;

	int insertWorship(Map<String, Object> args) throws Exception;

	int updateWorship(Map<String, Object> args) throws Exception;

	int deleteWorship(Map<String, String> args) throws Exception;

	int deleteWorshipMulti(List<Map<String, String>> args) throws Exception;

}
