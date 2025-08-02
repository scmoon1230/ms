package kr.co.ucp.wrks.sstm.mpg.service;

import java.util.List;
import java.util.Map;

public interface MpgInfoService
{

	List<Map<String, String>> selectMpgInfoList(Map<String, String> args) throws Exception;

	int insertMpgInfo(Map<String, Object> args) throws Exception;

	int updateMpgInfo(Map<String, Object> args) throws Exception;

	int deleteMpgInfo(Map<String, Object> args) throws Exception;

	int deleteMpgInfoMulti(List<Map<String, Object>> args) throws Exception;
}