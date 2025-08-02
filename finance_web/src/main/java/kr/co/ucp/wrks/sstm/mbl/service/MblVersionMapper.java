package kr.co.ucp.wrks.sstm.mbl.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("mblVersionMapper")
public interface MblVersionMapper {

	Map<String, String> getUser(String userid);

	List<Map<String, String>> getList(Map<String, String> args);

	List<Map<String, String>> getListDetail(String mppId);

	List<Map<String, String>> getListMapType();

	List<Map<String, String>> getListOsType();

	int insert(Map<String, Object> args);

	int update(Map<String, Object> args);

	int delete(Map<String, Object> args);

}
