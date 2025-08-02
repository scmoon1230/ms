package kr.co.ucp.wrks.sstm.mpg.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("mpgInfoMapper")
public interface MpgInfoMapper {

	List<Map<String, String>> selectMpgInfoList(Map<String, String> args);

	int insertMpgInfo(Map<String, Object> args);

	int updateMpgInfo(Map<String, Object> args);

	int deleteMpgInfo(Map<String, Object> args);

}
