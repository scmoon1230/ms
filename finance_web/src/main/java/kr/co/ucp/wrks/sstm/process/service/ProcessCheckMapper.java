package kr.co.ucp.wrks.sstm.process.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("processCheckMapper")
public interface ProcessCheckMapper {

	List<Map<String, String>> list(Map<String, String> args);
}
