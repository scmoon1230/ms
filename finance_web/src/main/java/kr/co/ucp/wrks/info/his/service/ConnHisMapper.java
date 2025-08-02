package kr.co.ucp.wrks.info.his.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("connHisMapper")
public interface ConnHisMapper {

	List<Map<String, String>> connHisListData(Map<String, String> args);

	List<Map<String, String>> connHisListExcel(Map<String, String> args);

	List<Map<String, String>> menuUsedListData(Map<String, String> args);

	List<Map<String, String>> menuUsedListExcel(Map<String, String> args);

}
