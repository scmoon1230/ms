package kr.co.ucp.wrks.sstm.code.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : CodeSycdService
 * @Description : 시스템코드
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
public interface CodeSycdService {

	List<Map<String, String>> sycdList(Map<String, String> args) throws Exception;

	int insert(Map<String, Object> args) throws Exception;

	int update(Map<String, Object> args) throws Exception;

	int delete(Map<String, String> args) throws Exception;

	int deleteMulti(List<Map<String, String>> args) throws Exception;


}


