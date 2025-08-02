package kr.co.ucp.wrks.sstm.code.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("codeDtcdMapper")
public interface CodeDtcdMapper {

	List<Map<String, String>> dtcdList(Map<String, String> args);
	
	List<Map<String, String>> selectUserInsttInfoList(Map<String, String> args);

	int insert(Map<String, Object> map);

	int update(Map<String, Object> map);

	int delete(Map<String, String> map);

	int deleteMulti(List<Map<String, String>> list);

}
