package kr.co.ucp.sheet2.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheet2mngMapper")
public interface Sheet2mngMapper {

	List<Map<String, String>> selectSheet2Mng(Map<String, String> args) throws Exception;

	String selectSheet2MngTotalAmnt(Map<String, String> args) throws Exception;

	// 전표마감 월간합계
	List<Map<String, String>> select2NewMonthSumList(Map<String, String> args) throws Exception;

	Map<String, String> select2NewMonthSum(Map<String, String> args) throws Exception;

}
