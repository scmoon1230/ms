package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("finmastMapper")
public interface FinmastMapper {

	List<Map<String, String>> selectFinmastList(Map<String, String> args) throws Exception;
	
	//List<Map<String, String>> selectFinmast(Map<String, String> args) throws Exception;

	Map<String, String> selectFinmastOne(Map<String, String> args) throws Exception;

	int insertFinmast(Map<String, Object> args) throws Exception;

	int updateFinmast(Map<String, Object> args) throws Exception;

	int deleteFinmast(Map<String, Object> args) throws Exception;

	String selectFinmastLastStanYmd() throws Exception;
	
}
