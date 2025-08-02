package kr.co.ucp.mntr.mng.service;

import java.util.List;
import java.util.Map;

public interface MngPrprtsService {

	List<Map<String, Object>> prprtsIdList(Map<String, Object> args) throws Exception;

	List<Map<String, Object>> prprtsTyList(Map<String, Object> args) throws Exception;

	List<Map<String, String>> prprtsList(Map<String, String> args) throws Exception;

	int insert(Map<String, Object> args) throws Exception;

	int update(Map<String, Object> args) throws Exception;

	int delete(Map<String, String> args) throws Exception;

	int deleteMulti(List<Map<String, String>> args) throws Exception;


}


