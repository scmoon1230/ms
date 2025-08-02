package kr.co.ucp.wrks.sstm.code.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("codeDstMapper")
public interface CodeDstMapper {

	//List<Map<String, String>> sggList(Map<String, Object> args);

	List<Map<String, String>> codeDstList(Map<String, String> args);

	int insertCodeDst(Map<String, Object> args);

	int updateCodeDst(Map<String, Object> args);

	int deleteCodeDst(Map<String, String> args);
}
