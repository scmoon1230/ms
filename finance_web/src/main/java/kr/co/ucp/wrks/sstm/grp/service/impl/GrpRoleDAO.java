package kr.co.ucp.wrks.sstm.grp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;


/**
 * ----------------------------------------------------------
 * @Class Name : GrpRoleDAO
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
@Repository("grpRoleDAO")
public class GrpRoleDAO extends EgovAbstractMapper{
	
	/*
	 * 롤 조건검색
	 */
	@SuppressWarnings("rawtypes")
	public List view(Map<String, String> args) throws Exception{
		return list("wrks_sstm_grp_role.list", args);
	}
	
	/*
	 * 롤 입력
	 */
	public int insert(Map<String, Object> args) throws Exception{
		return insert("wrks_sstm_grp_role.insert", args);
	}
	
	/*
	 * 롤 수정
	 */
	public int update(Map<String, Object> args) throws Exception{
		return update("wrks_sstm_grp_role.update", args);
	}
	
	/*
	 * 롤 삭제
	 */
	public int delete(Map<String, Object> args) throws Exception{
		return delete("wrks_sstm_grp_role.delete", args);
	}
	
	/*
	 * 롤 다중삭제
	 */
	public int deleteMulti(List<Map<String, String>> args) {
		int result = 0;
		
		for(int i=0; i<args.size(); i++){
			Map<String, String> arg = (Map<String, String>)args.get(i);
			result += delete("wrks_sstm_grp_role.delete", arg);			
		}
		
		return result;
	}
}


