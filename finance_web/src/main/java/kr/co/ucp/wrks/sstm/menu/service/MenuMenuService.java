package kr.co.ucp.wrks.sstm.menu.service;

import java.util.List;
import java.util.Map;

public interface MenuMenuService {
	
	// 프로그램메뉴 조건검색
	List<Map<String, Object>> select_pgm_menu(Map<String, Object> args) throws Exception;
	
	// 프로그램메뉴 프로그램목록 조회
	List<Map<String, Object>> list_prgm(Map<String, Object> args) throws Exception;
	
	// 프로그램메뉴 등록
	int insert_pgm_menu(Map<String, Object> args) throws Exception;
	
	// 프로그램메뉴 수정
	int update_pgm_menu(Map<String, Object> args) throws Exception;
	
	// 프로그램메뉴 삭제
	int delete_pgm_menu(Map<String, Object> args) throws Exception;

}
