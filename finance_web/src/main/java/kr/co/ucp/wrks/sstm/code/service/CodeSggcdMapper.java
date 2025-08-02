package kr.co.ucp.wrks.sstm.code.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("codeSggcdMapper")
public interface CodeSggcdMapper {

	List<Map<String, String>> selectWorship(Map<String, String> args) throws Exception;

	int insertWorship(Map<String, Object> args) throws Exception;

	int updateWorship(Map<String, Object> args) throws Exception;

	int deleteWorship(Map<String, String> args) throws Exception;

	//List<Map<String, Object>>  sidoNm_SigunguList(Map<String, Object> args) throws Exception;

	//List<Map<String, String>>  sigunguCdList(Map<String, Object> args) throws Exception;

}
