package kr.co.ucp.wrks.sstm.menu.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("menuIconMapper")
public interface MenuIconMapper {

	List<Map<String, String>> list(Map<String, String> args) throws Exception;

	void insert(Map<String, Object> map) throws Exception;

	void update(Map<String, Object> map) throws Exception;

	int getMaxSeqNo() throws Exception;

	int delete(Map<String, Object> args) throws Exception;

	int deleteMulti(Map<String, String> arg) throws Exception;


}
