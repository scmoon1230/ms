package kr.co.ucp.wrks.sstm.menu.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.menu.service.MenuIconMapper;
import kr.co.ucp.wrks.sstm.menu.service.MenuIconService;

import org.springframework.stereotype.Service;


/**
 * ----------------------------------------------------------
 * @Class Name : MenuIconServiceImpl
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
@Service("menuIconService")
public class MenuIconServiceImpl implements MenuIconService {
	
	@Resource(name="menuIconMapper")	
	private MenuIconMapper menuIconMapper;
	
	/*
	 * 조회
	 * */
	@Override
	public List<Map<String, String>> list(Map<String, String> args) throws Exception {
		return menuIconMapper.list(args);
	}
	
	/*
	 * 입력
	 * */
	@Override
	public void insert(Map<String, Object> map) throws Exception  {
		menuIconMapper.insert(map);
	}
	
	/*
	 * 수정
	 * */
	@Override
	public void update(Map<String, Object> map) throws Exception  {
		menuIconMapper.update(map);
	}
	
	/**
	 * MaxSeqNo
	 */
	public int getMaxSeqNo() throws Exception {
		return menuIconMapper.getMaxSeqNo();
	}
	

	@Override
	public int delete(Map<String, Object> args) throws Exception {
		return menuIconMapper.delete(args);
	}
	

	@Override
	public int deleteMulti(List<Map<String, String>> args) throws Exception {
		//return menuIconMapper.deleteMulti(args);
		int result = 0;
		
		for(int i=0; i<args.size(); i++){
			Map<String, String> arg = (Map<String, String>)args.get(i);
			//result += delete("wrks_sstm_menu_icon.delete", arg);
			result += menuIconMapper.deleteMulti(arg);
		}
		
		return result;		
	}
	
	
}




