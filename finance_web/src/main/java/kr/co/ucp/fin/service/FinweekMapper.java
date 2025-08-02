package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("finweekMapper")
public interface FinweekMapper {

	List<Map<String, String>> selectFinweek(Map<String, String> args) throws Exception;

}
