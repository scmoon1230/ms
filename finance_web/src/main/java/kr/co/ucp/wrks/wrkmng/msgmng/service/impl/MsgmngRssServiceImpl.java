package kr.co.ucp.wrks.wrkmng.msgmng.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.wrkmng.msgmng.service.MsgmngRssService;

import org.springframework.stereotype.Service;


/**
 * ----------------------------------------------------------
 * @Class Name : MsgmngRssServiceImpl
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
@Service("msgmngRssService")
public class MsgmngRssServiceImpl implements MsgmngRssService {
	
	@Resource(name="msgmngRssDAO")
	private MsgmngRssDAO msgmngRssDAO;
	
	/*
	 * RSS 조건검색
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List view(Map<String, String> args) throws Exception {
		return msgmngRssDAO.view(args);
	}
	
	/*
	 * RSS 입력
	 */
	@Override
	public int insert(Map<String, Object> args) throws Exception {
		return msgmngRssDAO.insert(args);
	}
	
	/*
	 * RSS 수정
	 */
	@Override
	public int update(Map<String, Object> args) throws Exception {
		return msgmngRssDAO.update(args);
	}
	
	/*
	 * RSS 삭제
	 */
	@Override
	public int delete(Map<String, Object> args) throws Exception {
		return msgmngRssDAO.delete(args);
	}
	
	/*
	 * RSS 다중삭제
	 */
	@Override
	public int deleteMulti(List<Map<String, String>> args) throws Exception {
		return msgmngRssDAO.deleteMulti(args);
	}
}


