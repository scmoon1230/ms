package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheetinvenMapper")
public interface SheetinvenMapper {

	List<Map<String, String>> selectSheetInven(Map<String, String> args) throws Exception;

	String selectSheetInvenTotalAmnt(Map<String, String> args) throws Exception;

}
