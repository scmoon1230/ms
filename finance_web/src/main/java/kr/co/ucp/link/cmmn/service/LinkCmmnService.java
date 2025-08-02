package kr.co.ucp.link.cmmn.service;

import java.util.List;
import java.util.Map;

public interface LinkCmmnService
{

	List<Map<String, Object>> selectListDstrtInfo(Map<String, Object> map) throws Exception;

	int insertWdLinkLog(Map<String, Object> map) throws Exception;

	int updateWdLinkWork(Map<String, Object> map) throws Exception;

	int updateLinkLog(Map<String, Object> map) throws Exception;

	List<Map<String, Object>> selectDstrtLinkAddrList(Map<String, Object> map) throws Exception;

	Map<String, Object> selectLinkInfo(String dstrtCd) throws Exception;
}
