package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheetsumMapper")
public interface SheetsumMapper {

	List<Map<String, String>> selectSheetSum(Map<String, String> args) throws Exception;

}
