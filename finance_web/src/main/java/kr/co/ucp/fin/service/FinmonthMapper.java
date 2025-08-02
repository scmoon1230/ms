package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("finmonthMapper")
public interface FinmonthMapper {

	List<Map<String, String>> selectFinmonth(Map<String, String> args) throws Exception;

}
