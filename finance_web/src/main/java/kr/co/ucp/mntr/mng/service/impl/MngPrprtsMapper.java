package kr.co.ucp.mntr.mng.service.impl;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("mngPrprtsMapper")
public interface MngPrprtsMapper {

	List<Map<String, Object>> prprtsIdList(Map<String, Object> args);

	List<Map<String, Object>> prprtsTyList(Map<String, Object> args);

	List<Map<String, String>> prprtsList(Map<String, String> args);

	int insert(Map<String, Object> args);

	int update(Map<String, Object> args);

	int delete(Map<String, String> args);

	int deleteMulti(List<Map<String, String>> args);

}
