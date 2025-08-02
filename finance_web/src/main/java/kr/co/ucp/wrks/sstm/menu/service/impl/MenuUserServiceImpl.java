package kr.co.ucp.wrks.sstm.menu.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.menu.service.MenuUserMapper;
import kr.co.ucp.wrks.sstm.menu.service.MenuUserService;

import org.springframework.stereotype.Service;


/**
 * ----------------------------------------------------------
 * @Class Name : MenuUserServiceImpl
 * @Description : 사용자별 프로그램메뉴
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
@Service("menuUserService")
public class MenuUserServiceImpl implements MenuUserService {

	@Resource(name="menuUserMapper")
	private MenuUserMapper menuUserMapper;


	/*
	 * 사용자별 프로그램 메뉴 지구명 리스트
	 */
	@Override
	public List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception {
		return menuUserMapper.getCM_DSTRT_CD_MNG(map);
	}

	/*
	 * 사용자별 프로그램 메뉴 사용자 리스트
	 */
	@Override
	public List<Map<String, String>> list_user(Map<String, String> args)
			throws Exception {
		return menuUserMapper.list_user(args);
	}

	/*
     * 사용자별 프로그램 메뉴 프로그램 리스트
     */
	@Override
	public List<Map<String, String>> list(Map<String, String> args)
			throws Exception {
		return menuUserMapper.list(args);
	}

	/*
     * 사용자별 프로그램 메뉴 수정
     */
	@Override
	public int update(List<Map<String, String>> args) throws Exception {
		//return menuUserMapper.update(args);
    	for(int i = 0; i < args.size(); i++) {

    		Map<String, String> arg = (Map<String, String>) args.get(i);

    		//Map<String, String> resultMap = (Map<String, String>) selectOne("wrks_sstm_menu_user.get_userId", arg);
    		Map<String, String> resultMap =  (Map<String, String>) menuUserMapper.get_userId(arg);

    		if(arg.get("RGS_AUTH_YN").equals("N") && arg.get("SEA_AUTH_YN").equals("N") && arg.get("UPD_AUTH_YN").equals("N") && arg.get("DEL_AUTH_YN").equals("N")) {
        		if(resultMap != null)
            		//delete("wrks_sstm_menu_user.delete", arg);
        			menuUserMapper.delete(arg);
    		}
    		else {
        		if(resultMap == null)
            		//insert("wrks_sstm_menu_user.insert", arg);
        			menuUserMapper.insert(arg);
        		else {
            		if(!(
            			arg.get("RGS_AUTH_YN").equals(resultMap.get("RGS_AUTH_YN"))
            			&& arg.get("SEA_AUTH_YN").equals(resultMap.get("SEA_AUTH_YN"))
            			&& arg.get("UPD_AUTH_YN").equals(resultMap.get("UPD_AUTH_YN"))
            			&& arg.get("DEL_AUTH_YN").equals(resultMap.get("DEL_AUTH_YN"))
            			)
            		){
            			//update("wrks_sstm_menu_user.update", arg);
            			menuUserMapper.update(arg);
            		}
        		}
    		}
    	}
    	return 1;
	}
	/*
	 * 사용자별 프로그램 메뉴 복사
	 */
	@Override
	public int copy(Map<String, String> args) throws Exception {
		return menuUserMapper.copy(args);
	}
}




