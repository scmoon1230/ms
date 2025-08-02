package kr.co.ucp.wrks.sstm.menu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;


/**
 * ----------------------------------------------------------
 * @Class Name : MenuIconDAO
 * @Description : 메뉴 아이콘 관리
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2016-03-31     고태영   최초작성
 * 
 * ----------------------------------------------------------
 * */
@Repository("menuIconDAO")
public class MenuIconDAO extends EgovAbstractMapper{

	/*
     * 메뉴아이콘 리스트
     */
	public List<Map<String, String>> list(Map<String, String> args) throws Exception{
		return selectList("wrks_sstm_menu_icon.list", args);
	}
	
	/*
	 * 입력
	 * */
	public void insert(Map<String, Object> map) {
		insert("wrks_sstm_menu_icon.insert", map);
	}
	
	/*
	 * 수정
	 * */
	public void update(Map<String, Object> map) {
		update("wrks_sstm_menu_icon.update", map);
	}
	
	/**
	 * Max SeqNO 
	 */
	public int getMaxSeqNo() throws Exception {
		return selectOne("wrks_sstm_menu_icon.getMaxSeqNo");
	}

	/*
	 * 삭제
	 */
	public int delete(Map<String, Object> args) throws Exception{
		return delete("wrks_sstm_menu_icon.delete", args);
	}
	
	/*
	 * 다중삭제
	 */
	public int deleteMulti(List<Map<String, String>> args) {
		int result = 0;
		
		for(int i=0; i<args.size(); i++){
			Map<String, String> arg = (Map<String, String>)args.get(i);
			result += delete("wrks_sstm_menu_icon.delete", arg);			
		}
		
		return result;
	}	
	
}
