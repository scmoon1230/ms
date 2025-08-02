package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("finchangeMapper")
public interface FinchangeMapper {

	List<Map<String, String>> selectFinchange(Map<String, String> args) throws Exception;

}
