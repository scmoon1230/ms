package kr.co.ucp.wrks.sstm.grp.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * ----------------------------------------------------------
 * @Class Name : GrpAuthRoleDAO
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
@Repository("grpAuthRoleDAO")
public class GrpAuthRoleDAO extends EgovAbstractMapper{
	
	/*
     * 그룹별권한레벨별롤 그룹 조회
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> list_grp(Map<String, String> map) throws Exception{
		return selectList("wrks_sstm_grp_auth_role.list_grp", map);
	}
	
	/*
     * 그룹별권한레벨별롤 롤 조회
     */ 
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> list_role(Map<String, String> map) throws Exception{
		return selectList("wrks_sstm_grp_auth_role.list_role", map);
	}
	
	/*
     * 그룹별권한레벨별롤 롤 조회 팝업
     */ 
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> list_popup(Map<String, String> map) throws Exception{
		return selectList("wrks_sstm_grp_auth_role.list_popup", map);
	}
	
	/*
     * 그룹별권한레벨별롤 롤 추가
     */ 
	public int insert_role(List<Map<String, String>> args) throws Exception {
		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = args.get(i);
    		insert("wrks_sstm_grp_auth_role.insert_role", arg);
		}
		return 1;
	}
	
	/*
     * 그룹별권한레벨별롤 롤 삭제
     */
	public int delete(Map<String, Object> args) throws Exception {
		return delete("wrks_sstm_grp_auth_role.delete_role", args);
	}
	
	/*
     * 그룹별권한레벨별롤 롤 다중삭제
     */
	public int delete_role(List<Map<String, String>> args) throws Exception {
		for(int i = 0; i < args.size(); i++) {
			Map<String, String> arg = args.get(i);
			insert("wrks_sstm_grp_auth_role.delete_role", arg);
		}
		return 1;
	}
	
}


