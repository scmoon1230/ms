package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("acctyearMapper")
public interface AcctyearMapper {

	List<Map<String, String>> selectAcctyear(Map<String, String> args) throws Exception;

	int insertAcctyear(Map<String, Object> args) throws Exception;

	int updateAcctyear(Map<String, Object> args) throws Exception;

	int deleteAcctyear(Map<String, String> args) throws Exception;

}
