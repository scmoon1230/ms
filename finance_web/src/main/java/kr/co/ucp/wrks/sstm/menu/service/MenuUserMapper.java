package kr.co.ucp.wrks.sstm.menu.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("menuUserMapper")
public interface MenuUserMapper {

	List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception;
	
	Map<String, String> get_userId(Map<String, String> args) throws Exception;

	List<Map<String, String>> list_user(Map<String, String> args) throws Exception;

	List<Map<String, String>> list(Map<String, String> args) throws Exception;

	int copy(Map<String, String> args) throws Exception;

	void insert(Map<String, String> arg) throws Exception;

	void delete(Map<String, String> arg) throws Exception;

	void update(Map<String, String> arg) throws Exception;

}
