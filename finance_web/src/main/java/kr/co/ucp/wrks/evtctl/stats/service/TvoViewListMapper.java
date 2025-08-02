package kr.co.ucp.wrks.evtctl.stats.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("tvoViewListMapper")
public interface TvoViewListMapper {

	List<Map<String, String>> tvoViewList(Map<String, String> args);

	List<Map<String, String>> tvoViewListExcel(Map<String, String> args);

}
