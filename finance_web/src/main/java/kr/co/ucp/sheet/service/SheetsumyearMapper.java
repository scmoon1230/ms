package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheetsumyearMapper")
public interface SheetsumyearMapper {

	Map<String, String> selectDurationYear(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectSheetSumyear(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectTotalAmnt(Map<String, String> args) throws Exception;

}
