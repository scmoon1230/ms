package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("acctgbMapper")
public interface AcctgbMapper {

	List<Map<String, String>> selectAcctgb(Map<String, String> args) throws Exception;

	int insertAcctgb(Map<String, Object> args) throws Exception;

	int updateAcctgb(Map<String, Object> args) throws Exception;

	int deleteAcctgb(Map<String, String> args) throws Exception;

}
