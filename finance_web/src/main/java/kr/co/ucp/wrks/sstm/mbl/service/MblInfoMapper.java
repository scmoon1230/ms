package kr.co.ucp.wrks.sstm.mbl.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("mblInfoMapper")
public interface MblInfoMapper {

	Map<String, String> getUser(String userid);

	List<Map<String, String>> list(Map<String, String> args);

	List<Map<String, String>> getListDetail(String mppId);

	List<Map<String, String>> getListMapType();

	List<Map<String, String>> getListOsType();

	int insert(Map<String, Object> args);

	int update(Map<String, Object> args);

	int delete(Map<String, Object> args);

	int mobileExcelInsert(Map<String, String> map);

	List<Map<String, String>> rcvTimeList(Map<String, Object> args);

	int deleteRcvTime(Map<String, Object> args);

	int updateRcvTime(Map<String, String> arg);

	int insertRcvTime(Map<String, String> arg);

	List<Map<String, String>> getUserList(Map<String, Object> args);

}