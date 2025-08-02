package kr.co.ucp.wrks.sstm.mbl.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("mblEvtMapper")
public interface MblEvtMapper {

	List<Map<String,String>> getEventList(Map<String, String> map);

	List<Map<String,String>> getEventNmList(Map<String, String> map);

	List<Map<String,String>> getEventNmListPopup(Map<String, String> map);

	int updateMbl(Map<String, Object> map);

	int deleteMbl(Map<String, Object> map);

	int insertMblUser(Map<String, Object> map);

	int checkMblUser(Map<String, Object> map);
}
