package kr.co.ucp.wrks.sstm.grp.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import kr.co.ucp.wrks.sstm.grp.service.GrpAuthRoleService;
import org.springframework.stereotype.Service;

/**
 * ----------------------------------------------------------
 * @Class Name :GrpAuthRoleServiceImpl
 * @Description : 그룹별권한레벨별롤
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-03-12   설준환       최초작성
 * 
 * ----------------------------------------------------------
 * */
@Service("grpAuthRoleService")
public class GrpAuthRoleServiceImpl implements GrpAuthRoleService {
	
	@Resource(name="grpAuthRoleDAO")
	private GrpAuthRoleDAO grpAuthRoleDAO;

	/*
     * 그룹별권한레벨별롤 그룹 조회
     */
	@Override
	public List<Map<String, String>> list_grp(Map<String, String> map)
			throws Exception {
		return grpAuthRoleDAO.list_grp(map);
	}
	
	/*
     * 그룹별권한레벨별롤 롤 조회
     */ 
	@Override
	public List<Map<String, String>> list_role(Map<String, String> map)
			throws Exception {
		return grpAuthRoleDAO.list_role(map);
	}

	/*
     * 그룹별권한레벨별롤 롤 조회 팝업
     */ 
	@Override
	public List<Map<String, String>> list_popup(Map<String, String> map)
			throws Exception {
		return grpAuthRoleDAO.list_popup(map);
	}

	/*
     * 그룹별권한레벨별롤 롤 추가
     */ 
	@Override
	public int insert_role(List<Map<String, String>> args) throws Exception {
		int ret = grpAuthRoleDAO.insert_role(args);
		return ret;
	}
    
	/*
     * 그룹별권한레벨별롤 롤 삭제
     */
	@Override
	public int delete(Map<String, Object> args) throws Exception {
		return grpAuthRoleDAO.delete(args);
	}
	
	/*
     * 그룹별권한레벨별롤 롤 다중삭제
     */
	@Override
	public int delete_role(List<Map<String, String>> args) throws Exception {
		int ret = grpAuthRoleDAO.delete_role(args);
		return ret;
	}
	
}