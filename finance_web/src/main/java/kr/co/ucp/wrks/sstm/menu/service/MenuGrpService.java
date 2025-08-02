package kr.co.ucp.wrks.sstm.menu.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : MenuGrpService
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
public interface MenuGrpService {
	
	/*
     * 그룹별 프로그램 메뉴 지구코드 조회
     */
	public List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception;
	
	/*
     * 그룹별 프로그램 메뉴 리스트
     */
	List<Map<String, String>> list_grp(Map<String, String> args) throws Exception;
	
	/*
     * 그룹별 프로그램 메뉴 메뉴 리스트
     */
	List<Map<String, String>> list(Map<String, String> args) throws Exception;

	/*
     * 그룹별 프로그램 메뉴 수정
     */
	int update(List<Map<String, String>> args) throws Exception;

	/*
	 * 그룹별 프로그램 메뉴 복사
	 */
	int copy(Map<String, String> args) throws Exception;
}