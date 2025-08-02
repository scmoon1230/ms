package kr.co.ucp.wrks.wrkmng.msgmng.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : MsgmngRssService
 * @Description : RSS정보관리
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-02-16   설준환       최초작성
 * 
 * ----------------------------------------------------------
 * */
public interface MsgmngRssService {
	
	/*
	 * RSS 조건검색
	 */
	@SuppressWarnings("rawtypes")
	List view(Map<String, String> args) throws Exception;
	
	/*
	 * RSS 입력
	 */
	int insert(Map<String, Object> args) throws Exception;

	/*
	 * RSS 수정
	 */
	int update(Map<String, Object> args) throws Exception;
	
	/*
	 * RSS 삭제
	 */
	int delete(Map<String, Object> args) throws Exception;
	
	/*
	 * RSS 다중삭제
	 */
	int deleteMulti(List<Map<String, String>> args) throws Exception;
}


