package kr.co.ucp.wrks.sstm.mpg.service;

import java.util.List;
import java.util.Map;

public interface ConnIpMpgService
{

	List<Map<String, String>> selectConnIpMpgInfoList(Map<String, String> args) throws Exception;

	int insertConnIpMpgInfo(Map<String, Object> args) throws Exception;

	int updateConnIpMpgInfo(Map<String, Object> args) throws Exception;

	int deleteConnIpMpgInfo(Map<String, Object> args) throws Exception;

	int deleteConnIpMpgInfoMulti(List<Map<String, Object>> args) throws Exception;
}