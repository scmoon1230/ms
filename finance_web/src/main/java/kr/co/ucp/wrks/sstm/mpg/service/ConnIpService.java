package kr.co.ucp.wrks.sstm.mpg.service;

import java.util.List;
import java.util.Map;

public interface ConnIpService
{

	List<Map<String, String>> selectConnIpInfoList(Map<String, String> args) throws Exception;

	int insertConnIpInfo(Map<String, Object> args) throws Exception;

	int updateConnIpInfo(Map<String, Object> args) throws Exception;

	int deleteConnIpInfo(Map<String, Object> args) throws Exception;

	int deleteConnIpInfoMulti(List<Map<String, Object>> args) throws Exception;
}