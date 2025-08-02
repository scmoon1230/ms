/**
 * -----------------------------------------------------------------
 * @Class Name : GrpLevCctvAuthDAO.java
 * @Description : 
 * @Version : 1.0
 * Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * -----------------------------------------------------------------
 * DATE           AUTHOR      DESCRIPTION
 * -----------------------------------------------------------------
 * 2018. 4. 9.   seungJun      New Write
 * -----------------------------------------------------------------
 */
package kr.co.ucp.wrks.sstm.grp.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("grpLevCctvAuthDAO")
public class GrpLevCctvAuthDAO extends EgovAbstractMapper
{
	public List<HashMap<String, String>> grpList() throws Exception{
		return selectList("wrks_sstm_grp_lev_cctv_auth.grp_list");
	}
	public List<HashMap<String, String>> grpAuthlist(HashMap<String, String> map) throws Exception{
		return selectList("wrks_sstm_grp_lev_cctv_auth.grp_auth_list", map);
	}
	public List<HashMap<String, String>> list(HashMap<String, String> map) throws Exception{
		return selectList("wrks_sstm_grp_lev_cctv_auth.list", map);
	}
	public int insert(HashMap<String, Object> args) throws Exception{
		return insert("wrks_sstm_grp_lev_cctv_auth.insert", args);
	}
	public int update(HashMap<String, Object> args) throws Exception{
		return update("wrks_sstm_grp_lev_cctv_auth.update", args);
	}
	public int delete(HashMap<String, Object> args) throws Exception{
		return delete("wrks_sstm_grp_lev_cctv_auth.delete", args);
	}
}
