package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheetcashtotalMapper")
public interface SheetcashtotalMapper {

	List<Map<String, String>> selectSheetCashtotal(Map<String, String> args) throws Exception;

	int insertSheet(Map<String, Object> args) throws Exception;

	int updateSheet(Map<String, Object> args) throws Exception;

	int deleteSheet(Map<String, String> args) throws Exception;

}
