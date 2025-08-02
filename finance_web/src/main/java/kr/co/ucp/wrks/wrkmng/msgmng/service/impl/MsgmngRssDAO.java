package kr.co.ucp.wrks.wrkmng.msgmng.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;


/**
 * ----------------------------------------------------------
 * @Class Name : MsgmngRssDAO
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
@Repository("msgmngRssDAO")
public class MsgmngRssDAO extends EgovAbstractMapper{
	
	/*
	 * RSS 조건검색
	 */
	@SuppressWarnings("rawtypes")
	public List view(Map<String, String> args) throws Exception{
		return selectList("wrks_wrkmng_msgmng_rss.list", args);
	}
	
	/*
	 * RSS 입력
	 */
	public int insert(Map<String, Object> args) throws Exception{
		return insert("wrks_wrkmng_msgmng_rss.insert", args);
	}
	
	/*
	 * RSS 수정
	 */
	public int update(Map<String, Object> args) throws Exception{
		return update("wrks_wrkmng_msgmng_rss.update", args);
	}
	
	/*
	 * RSS 삭제
	 */
	public int delete(Map<String, Object> args) throws Exception{
		return delete("wrks_wrkmng_msgmng_rss.delete", args);
	}
	
	/*
	 * RSS 다중삭제
	 */
	public int deleteMulti(List<Map<String, String>> args) {
		int result = 0;
		
		for(int i=0; i<args.size(); i++){
			Map<String, String> arg = (Map<String, String>)args.get(i);
			result += delete("wrks_wrkmng_msgmng_rss.delete", arg);			
		}
		
		return result;
	}
}


