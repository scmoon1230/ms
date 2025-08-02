package kr.co.ucp.wrks.sstm.code.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("codeSycdMapper")
public interface CodeSycdMapper {

	List<Map<String, String>> sycdList(Map<String, String> args);

	int insert(Map<String, Object> args);

	int update(Map<String, Object> args);

	int delete(Map<String, String> args);

	int deleteMulti(List<Map<String, String>> args);







}
