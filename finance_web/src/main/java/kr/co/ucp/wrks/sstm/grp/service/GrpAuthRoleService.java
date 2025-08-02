package kr.co.ucp.wrks.sstm.grp.service;

import java.util.List;
import java.util.Map;

/**
 * ----------------------------------------------------------
 * @Class Name : GrpAuthRoleService
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
public interface GrpAuthRoleService {
	
	/*
     * 그룹별권한레벨별롤 그룹 조회
     */
	public List<Map<String, String>> list_grp(Map<String, String> map) throws Exception;
	
	/*
     * 그룹별권한레벨별롤 롤 조회
     */
	public List<Map<String, String>> list_role(Map<String, String> map) throws Exception;
	
	/*
     * 그룹별권한레벨별롤 롤 조회 팝업
     */ 
	public List<Map<String, String>> list_popup(Map<String, String> map) throws Exception;
	
	/*
     * 그룹별권한레벨별롤 롤 추가
     */ 
	public int insert_role(List<Map<String, String>> args) throws Exception;
	
	/*
     * 그룹별권한레벨별롤 롤 삭제
     */
	public int delete(Map<String, Object> args) throws Exception;
	
	/*
     * 그룹별권한레벨별롤 롤 다중삭제
     */ 
	public int delete_role(List<Map<String, String>> args) throws Exception;
	
}