package kr.co.ucp.wrks.sstm.mpg.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("connIpMpgMapper")
public interface ConnIpMpgMapper {

	List<Map<String, String>> selectConnIpMpgInfoList(Map<String, String> args);

	int insertConnIpMpgInfo(Map<String, Object> args);

	int updateConnIpMpgInfo(Map<String, Object> args);

	int deleteConnIpMpgInfo(Map<String, Object> args);

}
