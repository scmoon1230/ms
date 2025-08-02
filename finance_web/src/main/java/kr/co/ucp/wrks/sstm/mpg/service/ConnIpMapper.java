package kr.co.ucp.wrks.sstm.mpg.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("connIpMapper")
public interface ConnIpMapper {

	List<Map<String, String>> selectConnIpInfoList(Map<String, String> args);

	int insertConnIpInfo(Map<String, Object> args);

	int updateConnIpInfo(Map<String, Object> args);

	int deleteConnIpInfo(Map<String, Object> args);

}
