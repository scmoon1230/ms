package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("fintransMapper")
public interface FintransMapper {

	List<Map<String, String>> selectFintrans(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectFintransAmt(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectTotalAmnt(Map<String, String> args) throws Exception;

	Map<String, String> selectNewDetSeq(Map<String, String> args) throws Exception;
	
	int insertFintrans(Map<String, Object> args) throws Exception;

	int updateFintrans(Map<String, String> args) throws Exception;

	int deleteFintrans(Map<String, String> args) throws Exception;

	Map<String, String> selectFintransCloseCnt(Map<String, String> args) throws Exception;

	int updateFintransClose(Map<String, String> args) throws Exception;

	
	
	
	
}
