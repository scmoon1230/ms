package kr.co.ucp.wrks.sstm.mbl.service;

import java.util.List;
import java.util.Map;

public interface MblEvtService {
	public List<Map<String, String>> getEventList(Map<String, String> args) throws Exception;

	public List<Map<String, String>> getEventNmList(Map<String, String> args) throws Exception;

	public List<Map<String, String>> getEventNmListPopup(Map<String, String> args) throws Exception;

	public int updateMbl(Map<String, Object> args) throws Exception;

	public int deleteMbl(Map<String, Object> args) throws Exception;

	public int insertMblUser(Map<String, Object> args) throws Exception;

	public int checkMblUser(Map<String, Object> args) throws Exception;
}


