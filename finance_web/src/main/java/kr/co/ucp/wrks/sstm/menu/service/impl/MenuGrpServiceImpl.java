package kr.co.ucp.wrks.sstm.menu.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

//import kr.co.ucp.wrks.sstm.menu.service.MenuMenuService;
import kr.co.ucp.wrks.sstm.menu.service.MenuGrpService;

import org.springframework.stereotype.Service;


/**
 * ----------------------------------------------------------
 * @Class Name : MenuGrpServiceImpl
 * @Description : 그룹별 프로그램메뉴
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
@Service("menuGrpService")
public class MenuGrpServiceImpl implements MenuGrpService {
	
	@Resource(name="menuGrpDAO")	
	private MenuGrpDAO menuGrpDAO;
	
	
	/*
     * 그룹별 프로그램 메뉴 지구코드 조회
     */
	@Override
	public List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception {
		return menuGrpDAO.getCM_DSTRT_CD_MNG(map);
	}
	
	/*
     * 그룹별 프로그램 메뉴 리스트
     */
	@Override
	public List<Map<String, String>> list_grp(Map<String, String> args)
			throws Exception {
		return menuGrpDAO.list_grp(args);
	}
	
	/*
     * 그룹별 프로그램 메뉴 메뉴 리스트
     */
	@Override
	public List<Map<String, String>> list(Map<String, String> args)
			throws Exception {
		return menuGrpDAO.list(args);
	}

	/*
     * 그룹별 프로그램 메뉴 수정
     */
	@Override
	public int update(List<Map<String, String>> args) throws Exception {
		return menuGrpDAO.update(args);
	}
	/*
	 * 그룹별 프로그램 메뉴 복사
	 */
	@Override
	public int copy(Map<String, String> args) throws Exception {
		return menuGrpDAO.copy(args);
	}
}




