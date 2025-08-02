package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheetdurationMapper")
public interface SheetdurationMapper {

	List<Map<String, String>> selectSheetDuration(Map<String, String> args) throws Exception;

	String selectSheetDurationTotalAmnt(Map<String, String> args) throws Exception;

}
