package kr.co.ucp.wrks.sstm.code.service;

import java.util.List;
import java.util.Map;

/**
 * ----------------------------------------------------------
 * @Class Name : CodeFcltcdService
 * @Description : 시설물용도별유형코드
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-03-19   설준환       최초작성
 *
 * ----------------------------------------------------------
 * */
public interface CodeFcltcdService {

	List<Map<String, String>> fcltcdList(Map<String, String> args) throws Exception;

	int insert(Map<String, Object> args) throws Exception;

	int update(Map<String, Object> args) throws Exception;

	int delete(Map<String, String> args) throws Exception;

	int deleteMulti(List<Map<String, String>> args) throws Exception;



}