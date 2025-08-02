package kr.co.ucp.wrks.sstm.menu.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("menuMenuMapper")
public interface MenuMenuMapper {

	List<Map<String, Object>> select_pgm_menu(Map<String, Object> args) throws Exception;

	List<Map<String, Object>> list_prgm(Map<String, Object> args) throws Exception;

	int insert_pgm_menu(Map<String, Object> args) throws Exception;

	int update_pgm_menu(Map<String, Object> args) throws Exception;

	int delete_pgm_menu(Map<String, Object> args) throws Exception;
	
	

}
