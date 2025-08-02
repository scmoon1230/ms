package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheetsummonthMapper")
public interface SheetsummonthMapper {

	List<Map<String, String>> selectSheetSummonth(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectTotalAmnt(Map<String, String> args) throws Exception;

}
