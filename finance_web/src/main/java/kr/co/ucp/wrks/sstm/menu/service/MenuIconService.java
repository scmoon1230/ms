package kr.co.ucp.wrks.sstm.menu.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : MenuIconService
 * @Description : 메뉴아이콘 관리
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
public interface MenuIconService {

	List<Map<String, String>> list(Map<String, String> args) throws Exception;

	void insert(Map<String, Object> map) throws Exception;

	void update(Map<String, Object> map) throws Exception;
	
	public int getMaxSeqNo() throws Exception ;
	
	int delete(Map<String, Object> args) throws Exception;
	
	int deleteMulti(List<Map<String, String>> args) throws Exception;
	
}