package kr.co.ucp.wrks.sstm.menu.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : MenuInfoService
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
public interface MenuInfoService {

	List<Map<String, String>> fnctList(Map<String, Object> args)
			throws Exception;

	List<Map<String, String>> list(Map<String, String> args) throws Exception;

	int insert(Map<String, Object> args) throws Exception;

	int update(Map<String, Object> args) throws Exception;

	int delete(Map<String, Object> args) throws Exception;

	int deleteMulti(List<Map<String, Object>> args) throws Exception;

}

