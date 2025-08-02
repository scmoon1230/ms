package kr.co.ucp.wrks.sstm.mbl.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.ucp.wrks.sstm.mbl.service.MblVersionMapper;
import kr.co.ucp.wrks.sstm.mbl.service.MblVersionService;


/**
 * ----------------------------------------------------------
 * @Class Name : MblVersionServiceImpl
 * @Description : 모바일 앱버전정보
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
@Service("mblVersionService")
public class MblVersionServiceImpl extends EgovAbstractServiceImpl implements
        MblVersionService {

    @Resource(name="mblVersionMapper")
    private MblVersionMapper mblVersionMapper;

    @Override
    public Map<String, String> getUser(String userid) throws Exception {
    	Map<String, String> mapD = mblVersionMapper.getUser(userid);

    	return mapD;
    }

	/** 모바일 계정정보 조회 */
/*	public List<Map<String, String>> getList(String mppKndCd) throws Exception {
    	List<Map<String, String>> list = mblVersionMapper.getList(mppKndCd);
    	System.out.println("로그_impl  1");
    	return list;
	}
*/

    /*
     * 앱버전정보 조건검색
     */
    @Override
    public List<Map<String, String>> getList(Map<String, String> args) throws Exception {
    	List<Map<String, String>> list = mblVersionMapper.getList(args);
    	return list;
	}


	/** 모바일 계정정보 상세 조회 */
    @Override
	public List<Map<String, String>> getListDetail(String mppId) throws Exception{
    	List<Map<String, String>> list = mblVersionMapper.getListDetail(mppId);

    	return list;
	}

	/** 모바일 계정정보_콤보박스_모바일기기종류 */
    @Override
	public List<Map<String, String>> getListMapType() throws Exception{
    	List<Map<String, String>> list = mblVersionMapper.getListMapType();

    	return list;
	}

	/** 모바일 계정정보_콤보박스_모바일기기OS유형*/
    @Override
	public List<Map<String, String>> getListOsType() throws Exception{
    	List<Map<String, String>> list = mblVersionMapper.getListOsType();

    	return list;
	}

	/*
	 * 앱버전정보 입력
	 */
    @Override
	public int insert(Map<String, Object> args) throws Exception{
    	int ret = mblVersionMapper.insert(args);

    	return ret;
	}

	/*
	 * 앱버전정보 수정
	 */
    @Override
	public int update(Map<String, Object> args) throws Exception{
    	int ret = mblVersionMapper.update(args);

    	return ret;
	}

	/*
	 * 앱버전정보 삭제
	 */
    @Override
	public int delete(Map<String, Object> args) throws Exception{
    	int ret = mblVersionMapper.delete(args);

    	return ret;
	}

	/*
	 * 앱버전정보 다중삭제
	 */
    @Override
    public int deleteMulti(List<Map<String, Object>> args) throws Exception {
    	int ret = 0;
    	for(int i = 0; i < args.size(); i++) {

    		Map<String, Object> arg = (Map<String, Object>) args.get(i);

    		ret = mblVersionMapper.delete(arg);

    	}
    	return ret;
    }
}


