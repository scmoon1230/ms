package kr.co.ucp.wrks.sstm.menu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;


/**
 * ----------------------------------------------------------
 * @Class Name : MenuInfoDAO
 * @Description : 프로그램정보
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
@Repository("menuInfoDAO")
public class MenuInfoDAO extends EgovAbstractMapper{
	
	/*
	 * 프로그램정보 기능유형 설정
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> fnctList(Map<String, Object> args) {
		return selectList("wrks_sstm_menu_info.fnctList", args);
	}
	
	/*
     * 프로그램정보 조건검색
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> view(Map<String, String> args) {
		return selectList("wrks_sstm_menu_info.list", args);
	}
	
	/*
     * 프로그램정보 등록
     */
	public int insert(Map<String, Object> args) {
		return insert("wrks_sstm_menu_info.insert", args);
	}
	
	/*
     * 프로그램정보 수정
     */
	public int update(Map<String, Object> args) {
		return update("wrks_sstm_menu_info.update", args);
	}
	
	/*
     * 프로그램정보 삭제
     */
	public int delete(Map<String, Object> args) {
		return delete("wrks_sstm_menu_info.delete", args);
	}
	
	/*
     * 프로그램정보 다중삭제
     */
	public int deleteMulti(List<Map<String, String>> args) {
		int result = 0;
		
		for(int i=0; i<args.size(); i++){
			Map<String, String> arg = args.get(i);
			result += delete("wrks_sstm_menu_info.delete", arg);
		}
		
		return result;
	}

}






