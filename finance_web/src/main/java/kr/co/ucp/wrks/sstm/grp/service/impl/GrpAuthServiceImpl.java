package kr.co.ucp.wrks.sstm.grp.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.grp.service.GrpAuthMapper;
import kr.co.ucp.wrks.sstm.grp.service.GrpAuthService;

import org.springframework.stereotype.Service;


/**
 * ----------------------------------------------------------
 * @Class Name : GrpAuthServiceImpl
 * @Description : 그룹별 권한레벨 관리
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
@Service("grpAuthService")
public class GrpAuthServiceImpl implements GrpAuthService {
	
	@Resource(name="grpAuthMapper")
	private GrpAuthMapper grpAuthMapper;
	
	// 그룹별 권한레벨관리 조건검색
	@Override
	public List<Map<String, String>> list(Map<String, String> args) throws Exception {
		return grpAuthMapper.list(args);
	}
	
	// 그룹별 권한레벨관리 권한레벨 조건검색
	@Override
	public List<Map<String, String>> authList(Map<String, String> args) throws Exception {
		return grpAuthMapper.authList(args);
	}
	
	// 그룹별 권한레벨관리 권한레벨 입력
	@Override
	public int insert(Map<String, String> args) throws Exception {
		return grpAuthMapper.insert(args);
	}
	
	// 그룹별 권한레벨관리 권한레벨 수정
	@Override
	public int update(Map<String, Object> args) throws Exception {
		return grpAuthMapper.update(args);
	}
	
	// 그룹별 권한레벨관리 권한레벨 삭제
	@Override
	public int delete(Map<String, Object> args) throws Exception {
		return grpAuthMapper.delete(args);
	}
	
	// 그룹별 권한레벨관리 권한레벨 다중삭제
	@Override
	public int deleteMulti(List<Map<String, Object>> args) throws Exception {
		
		for(int i=0; i<args.size(); i++){
			Map<String, Object> map = (Map<String, Object>)args.get(i);
			grpAuthMapper.delete(map);
		}
		
		return 1;		
	}

}
