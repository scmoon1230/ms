package kr.co.ucp.wrks.evtctl.stats.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("tvoOutListMapper")
public interface TvoOutListMapper {

	List<Map<String, String>> tvoOutList(Map<String, String> args);

	List<Map<String, String>> tvoOutListExcel(Map<String, String> args);

}
