package kr.co.ucp.sheet2.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheet2summonthMapper")
public interface Sheet2summonthMapper {

	List<Map<String, String>> selectSheet2Summonth(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectTotalAmnt(Map<String, String> args) throws Exception;

	int insertSheet(Map<String, Object> args) throws Exception;

	int updateSheet(Map<String, Object> args) throws Exception;

	int deleteSheet(Map<String, String> args) throws Exception;

}
