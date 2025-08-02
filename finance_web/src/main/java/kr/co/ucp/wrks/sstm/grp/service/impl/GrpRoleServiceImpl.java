package kr.co.ucp.wrks.sstm.grp.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.grp.service.GrpRoleService;

import org.springframework.stereotype.Service;


/**
 * ----------------------------------------------------------
 * @Class Name : GrpRoleServiceImpl
 * @Description : 롤관리
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
@Service("grpRoleService")
public class GrpRoleServiceImpl implements GrpRoleService {
	
	@Resource(name="grpRoleDAO")
	private GrpRoleDAO grpRoleDAO;
	
	/*
	 * 롤 조건검색
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List view(Map<String, String> args) throws Exception {
		return grpRoleDAO.view(args);
	}
	
	/*
	 * 롤 입력
	 */
	@Override
	public int insert(Map<String, Object> args) throws Exception {
		return grpRoleDAO.insert(args);
	}
	
	/*
	 * 롤 수정
	 */
	@Override
	public int update(Map<String, Object> args) throws Exception {
		return grpRoleDAO.update(args);
	}
	
	/*
	 * 롤 삭제
	 */
	@Override
	public int delete(Map<String, Object> args) throws Exception {
		return grpRoleDAO.delete(args);
	}
	
	/*
	 * 롤 다중삭제
	 */
	@Override
	public int deleteMulti(List<Map<String, String>> args) throws Exception {
		return grpRoleDAO.deleteMulti(args);
	}
}


