package kr.co.ucp.wrks.sstm.grp.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("grpCodeMapper")
public interface GrpCodeMapper {

	List<Map<String, String>> dstrtList(Map<String, String> args);

	List<Map<String, String>> lkSysIdList(Map<String, String> args);

	List<Map<String, String>> list(Map<String, String> args);

	int insert(Map<String, Object> args);

	int update(Map<String, Object> args);

	int delete(Map<String, Object> args);

}
