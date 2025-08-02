package kr.co.ucp.wrks.sstm.code.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("codeCmcdMapper")
public interface CodeCmcdMapper {

	List<Map<String, String>> grpList(Map<String, Object> map);

	List<Map<String, String>> sysList(Map<String, Object> map);

	List<Map<String, String>> cmcdList(Map<String, String> map);

	int insert(Map<String, Object> map);

	int delete(Map<String, String> map);

	int update(Map<String, Object> map);

	int deleteMulti(List<Map<String, String>> list);

	String getCurrentDay();

	List<Map<String, String>> sysCodeList(Map<String, Object> map);






}
