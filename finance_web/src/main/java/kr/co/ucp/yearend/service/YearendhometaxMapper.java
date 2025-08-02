package kr.co.ucp.yearend.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("yearendhometaxMapper")
public interface YearendhometaxMapper {

	List<Map<String, String>> stanYyList(Map<String, String> args);

	List<Map<String, String>> selectMoney(Map<String, String> args) throws Exception;

}
