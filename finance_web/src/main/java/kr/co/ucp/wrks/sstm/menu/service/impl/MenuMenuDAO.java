package kr.co.ucp.wrks.sstm.menu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;


/**
 * ----------------------------------------------------------
 * @Class Name : MenuMenuDAO
 * @Description : 프로그램메뉴
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원       최초작성
 * 
 * ----------------------------------------------------------
 * */
@Repository("menuMenuDAO")
public class MenuMenuDAO extends EgovAbstractMapper{
	
	/*
     * 프로그램메뉴 조건검색
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> list(Map<String, Object> args) throws Exception{
		return selectList("wrks_sstm_menu_menu.list", args);
	}
	
	/*
     * 프로그램메뉴 프로그램목록 조회
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> list_prgm(Map<String, Object> args) throws Exception{
		return selectList("wrks_sstm_menu_menu.list_prgm", args);
	}
	
	/*
     * 프로그램메뉴 등록
     */
	public int insert(Map<String, Object> args) throws Exception{
		return insert("wrks_sstm_menu_menu.insert_pgm_menu", args);
	}
	
	/*
     * 프로그램메뉴 수정
     */
	public int update(Map<String, Object> args) throws Exception{
		return update("wrks_sstm_menu_menu.update_pgm_menu", args);
	}
	
	/*
     * 프로그램메뉴 삭제
     */
	public int delete(Map<String, Object> args) throws Exception{
		return delete("wrks_sstm_menu_menu.delete_pgm_menu", args);
	}
}
