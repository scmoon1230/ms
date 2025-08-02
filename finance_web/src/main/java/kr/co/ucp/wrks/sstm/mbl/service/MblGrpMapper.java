package kr.co.ucp.wrks.sstm.mbl.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("mblGrpMapper")
public interface MblGrpMapper {

	List<Map<String, String>> getList(Map<String, String> args);

	@SuppressWarnings("rawtypes")
	List grpList(Map<String, Object> map);

	@SuppressWarnings("rawtypes")
	List moblNoList(Map<String, Object> map);

	int insert(Map<String, Object> args);

	int update(Map<String, Object> args);

	int delete(Map<String, Object> args);

}