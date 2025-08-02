package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

public interface AcctcodeService {

	List<Map<String, String>> stanYyList(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectAcctcode(Map<String, Object> args) throws Exception;

	int insertAcctcode(Map<String, Object> args) throws Exception;

	int updateAcctcode(Map<String, Object> args) throws Exception;

	int deleteAcctcode(Map<String, String> args) throws Exception;

	int deleteAcctcodeMulti(List<Map<String, String>> args) throws Exception;

	int selectAcctcodeCnt(Map<String, Object> map) throws Exception;

	int makeNextFromPrev(Map<String, Object> args) throws Exception;

}
