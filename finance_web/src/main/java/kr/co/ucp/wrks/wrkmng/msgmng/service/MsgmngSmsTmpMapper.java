package kr.co.ucp.wrks.wrkmng.msgmng.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("msgmngSmsTmpMapper")
public interface MsgmngSmsTmpMapper {

	List<Map<String, String>> list(Map<String, String> args);

	List<Map<String, String>> list_rcv(Map<String, String> args);

	int update(Map<String, Object> map);

	void delete(Map<String, Object> map);

	int update_sms_status_ok(Map<String, Object> map);

	String select_sms_sms_id();

	int insert_sms(Map<String, Object> map);

	int insert_rcv(Map<String, String> arg);

	List<Map<String, String>> list_grp(Map<String, String> args);

}
