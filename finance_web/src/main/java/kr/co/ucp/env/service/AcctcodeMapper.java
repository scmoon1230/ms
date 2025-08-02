package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("acctcodeMapper")
public interface AcctcodeMapper {

	List<Map<String, String>> stanYyList(Map<String, String> args);

	List<Map<String, String>> selectAcctcode(Map<String, Object> args) throws Exception;

	int insertAcctcode(Map<String, Object> args) throws Exception;

	int updateAcctcode(Map<String, Object> args) throws Exception;

	int deleteAcctcode(Map<String, String> args) throws Exception;

	int selectAcctcodeCnt(Map<String, Object> map);

}
