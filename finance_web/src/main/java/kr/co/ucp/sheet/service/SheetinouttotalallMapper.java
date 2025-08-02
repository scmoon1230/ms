package kr.co.ucp.sheet.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("sheetinouttotalallMapper")
public interface SheetinouttotalallMapper {

	List<Map<String, String>> selectSheetInouttotalall(Map<String, String> args) throws Exception;

}
