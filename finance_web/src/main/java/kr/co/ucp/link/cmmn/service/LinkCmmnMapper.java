package kr.co.ucp.link.cmmn.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("linkCmmnMapper")
public interface LinkCmmnMapper {
	
	List<Map<String, Object>> selectListDstrtInfo(Map<String, Object> map);

	int insertWdLinkLog(Map<String, Object> map);

	int updateWdLinkWork(Map<String, Object> map);

	List<Map<String, Object>> selectDstrtLinkAddrList(Map<String, Object> map);

	Map<String, Object> selectLinkInfo(String dstrtCd);
	
}