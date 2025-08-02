package kr.co.ucp.wrks.sstm.menu.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("menuInfoMapper")
public interface MenuInfoMapper {

	List<Map<String, String>> fnctList(Map<String, Object> args);

	List<Map<String, String>> list(Map<String, String> args);

	int insert(Map<String, Object> args);

	int update(Map<String, Object> args);

	int delete(Map<String, Object> args);

}
