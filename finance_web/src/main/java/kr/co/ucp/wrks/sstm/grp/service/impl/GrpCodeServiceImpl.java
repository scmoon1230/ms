package kr.co.ucp.wrks.sstm.grp.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.grp.service.GrpCodeMapper;
import kr.co.ucp.wrks.sstm.grp.service.GrpCodeService;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


/**
 * ----------------------------------------------------------
 * @Class Name : GrpCodeServiceImpl
 * @Description : 그룹관리
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
@Service("grpCodeService")
public class GrpCodeServiceImpl extends EgovAbstractServiceImpl implements GrpCodeService {

    @Resource(name="grpCodeMapper")
    private GrpCodeMapper grpCodeMapper;

    // 그룹관리 지구코드 조회
	@Override
   public List<Map<String, String>> getDstrtList( Map<String, String> args) throws Exception {
    	List<Map<String, String>> list = grpCodeMapper.dstrtList(args);
    	return list;
	}

    // 그룹관리 시스템구분 조회
	@Override
      public List<Map<String, String>> getLkSysIdList( Map<String, String> args) throws Exception {
      	List<Map<String, String>> list = grpCodeMapper.lkSysIdList(args);
      	return list;
  	}

    // 그룹관리 조건검색
	@Override
    public List<Map<String, String>> getList( Map<String, String> args) throws Exception {
    	List<Map<String, String>> list = grpCodeMapper.list(args);
    	return list;
    }

    // 그룹관리 입력
	@Override
	public int insert(Map<String, Object> args) throws Exception{
    	int ret = grpCodeMapper.insert(args);
    	return ret;
	}

	// 그룹관리 수정
	@Override
	public int update(Map<String, Object> args) throws Exception{
    	int ret = grpCodeMapper.update(args);
    	return ret;
	}

	// 그룹관리 삭제
	@Override
	public int delete(Map<String, Object> args) throws Exception{
    	int ret = grpCodeMapper.delete(args);
    	return ret;
	}

	// 그룹관리 다중삭제
	@Override
    public int deleteMulti(List<Map<String, Object>> args) throws Exception {
    	int ret = 0;
    	for(int i = 0; i < args.size(); i++) {
    		Map<String, Object> arg = (Map<String, Object>) args.get(i);
    		ret = grpCodeMapper.delete(arg);
    	}
    	return ret;
    }
	
}