package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("worshipMapper")
public interface WorshipMapper {

	List<Map<String, String>> selectWorship(Map<String, String> args) throws Exception;

	int insertWorship(Map<String, Object> args) throws Exception;

	int updateWorship(Map<String, Object> args) throws Exception;

	int deleteWorship(Map<String, String> args) throws Exception;

}
