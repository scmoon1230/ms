package kr.co.ucp.wrks.sstm.code.service;

import java.util.List;
import java.util.Map;



/**
 * ----------------------------------------------------------
 * @Class Name : CodeDtcdService
 * @Description : 코드상세
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
public interface CodeDtcdService {

	List<Map<String, String>> dtcdList(Map<String, String> map) throws Exception;

	int insert(Map<String, Object> map) throws Exception;

	int update(Map<String, Object> map) throws Exception;

	int delete(Map<String, String> map) throws Exception;

	int deleteMulti(List<Map<String, String>> list) throws Exception;

}


