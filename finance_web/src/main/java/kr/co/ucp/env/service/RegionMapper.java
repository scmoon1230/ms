package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("regionMapper")
public interface RegionMapper {

	List<Map<String, String>> selectRegion(Map<String, String> args) throws Exception;

	int insertRegion(Map<String, Object> args) throws Exception;

	int updateRegion(Map<String, Object> args) throws Exception;

	int deleteRegion(Map<String, String> args) throws Exception;

}
