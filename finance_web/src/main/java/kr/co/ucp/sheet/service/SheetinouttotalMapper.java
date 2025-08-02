package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheetinouttotalMapper")
public interface SheetinouttotalMapper {

	List<Map<String, String>> selectSheetInouttotal(Map<String, String> args) throws Exception;

}
