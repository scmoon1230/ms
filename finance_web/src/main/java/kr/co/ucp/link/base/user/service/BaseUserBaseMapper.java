package kr.co.ucp.link.base.user.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("BaseUserBaseMapper")
public interface BaseUserBaseMapper {
	
	List<Map<String, Object>> selectUserList(Map<String, Object> map);
	
}
