package kr.co.ucp.wrks.sstm.grp.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("grpAuthMapper")
public interface GrpAuthMapper {

	List<Map<String, String>> list(Map<String, String> args);

	List<Map<String, String>> authList(Map<String, String> args);

	int insert(Map<String, String> args);

	int update(Map<String, Object> args);

	int delete(Map<String, Object> args);

}
