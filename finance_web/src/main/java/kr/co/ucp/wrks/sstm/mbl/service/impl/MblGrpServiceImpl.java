package kr.co.ucp.wrks.sstm.mbl.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.ucp.wrks.sstm.mbl.service.MblGrpMapper;
import kr.co.ucp.wrks.sstm.mbl.service.MblGrpService;


/**
 * ----------------------------------------------------------
 * @Class Name : MblGrpServiceImpl
 * @Description : 모바일 그룹관리
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
@Service("mblGrpService")
public class MblGrpServiceImpl extends EgovAbstractServiceImpl implements MblGrpService {

    @Resource(name="mblGrpMapper")
    private MblGrpMapper mblGrpMapper;


    // 모바일그룹관리 조건검색
    @Override
    public List<Map<String, String>> getList( Map<String, String> args) throws Exception {
    	List<Map<String, String>> list = mblGrpMapper.getList(args);
    	return list;
	}

    // 모바일그룹관리 사용유무 조회
	@SuppressWarnings("rawtypes")
	@Override
	public List grpList(Map<String, Object> map) throws Exception {
		return mblGrpMapper.grpList(map);
	}

	// 모바일그룹관리 모바일번호 조회
	@SuppressWarnings("rawtypes")
	@Override
	public List moblNoList(Map<String, Object> map) throws Exception {
		return mblGrpMapper.moblNoList(map);
	}

	// 모바일그룹관리 입력
	@Override
	public int insert(Map<String, Object> args) throws Exception{
    	int ret = mblGrpMapper.insert(args);
    	return ret;
	}

	// 모바일그룹관리 수정
	@Override
	public int update(Map<String, Object> args) throws Exception{
    	int ret = mblGrpMapper.update(args);
    	return ret;
	}

	// 모바일그룹관리 삭제
	@Override
	public int delete(Map<String, Object> args) throws Exception{
    	int ret = mblGrpMapper.delete(args);
    	return ret;
	}

	// 모바일그룹관리 다중삭제
	@Override
    public int deleteMulti(List<Map<String, Object>> args) throws Exception {
    	int ret = 0;
    	for(int i = 0; i < args.size(); i++) {
    		Map<String, Object> arg = (Map<String, Object>) args.get(i);
    		ret = mblGrpMapper.delete(arg);
    	}
    	return ret;
    }
	
}