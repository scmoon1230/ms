package kr.co.ucp.wrks.sstm.menu.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;


/**
 * ----------------------------------------------------------
 * @Class Name : MenuGrpAuthDAO
 * @Description : 그룹권한별 프로그램메뉴
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
@Repository("menuGrpAuthDAO")
public class MenuGrpAuthDAO extends EgovAbstractMapper{

	/*
     * 그룹권한별 프로그램 메뉴 지구코드 조회
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception{
		return selectList("wrks_sstm_menu_grpauth.list_cm_dstrt_cd_mng", map);
	}

	/*
     * 그룹권한별 프로그램 메뉴 리스트
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> list_grpauth(Map<String, String> args) throws Exception{
		return selectList("wrks_sstm_menu_grpauth.list_grpauth", args);
	}

	/*
     * 그룹권한별 프로그램 메뉴 메뉴 리스트
     */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> list_grpauth_menu(Map<String, String> args) throws Exception{
		return selectList("wrks_sstm_menu_grpauth.list_grpauth_menu", args);
	}

	/*
     * 그룹권한별 프로그램 메뉴 수정
     */
	@SuppressWarnings("unchecked")
	public int update(List<Map<String, String>> args) throws Exception{
    	for(int i = 0; i < args.size(); i++) {

    		Map<String, String> arg = (Map<String, String>) args.get(i);

    		Map<String, String> resultMap =  (Map<String, String>) selectOne("wrks_sstm_menu_grpauth.get_grpId", arg);

    		if(arg.get("RGS_AUTH_YN").equals("N") && arg.get("SEA_AUTH_YN").equals("N") && arg.get("UPD_AUTH_YN").equals("N") && arg.get("DEL_AUTH_YN").equals("N")) {
        		if(resultMap != null)
            		delete("wrks_sstm_menu_grpauth.delete", arg);
    		}
    		else {
        		if(resultMap == null)
            		insert("wrks_sstm_menu_grpauth.insert", arg);
        		else {
            		if(!(
            			arg.get("RGS_AUTH_YN").equals(resultMap.get("RGS_AUTH_YN"))
            			&& arg.get("SEA_AUTH_YN").equals(resultMap.get("SEA_AUTH_YN"))
            			&& arg.get("UPD_AUTH_YN").equals(resultMap.get("UPD_AUTH_YN"))
            			&& arg.get("DEL_AUTH_YN").equals(resultMap.get("DEL_AUTH_YN"))
            			)
            		){
            			update("wrks_sstm_menu_grpauth.update", arg);
            		}
        		}
    		}
    	}
    	return 1;
	}
	/*
	 * 그룹권한별 프로그램 메뉴 복사
	 */
	@SuppressWarnings("unchecked")
	public int copy(Map<String, String> args) throws Exception{
		delete("wrks_sstm_menu_grpauth.copy", args);
		return 1;
	}
}
