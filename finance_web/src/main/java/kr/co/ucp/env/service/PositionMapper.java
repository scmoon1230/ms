package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("positionMapper")
public interface PositionMapper {

	List<Map<String, String>> selectPosition(Map<String, String> args) throws Exception;

	int insertPosition(Map<String, Object> args) throws Exception;

	int updatePosition(Map<String, Object> args) throws Exception;

	int deletePosition(Map<String, String> args) throws Exception;

}
