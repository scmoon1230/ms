package kr.co.ucp.wrks.sstm.grp.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : GrpUserAccService
 * @Description : 그룹별이벤트
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
public interface GrpUserAccService {

	List<Map<String, String>> getCM_GRP_AUTH_LVL(Map<String, String> map) throws Exception;

	List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception;

	List<Map<String, String>> getCM_GROUP(Map<String, String> map) throws Exception;

	List<Map<String, String>> getCM_GRP_EVT(Map<String, String> map) throws Exception;

	List<Map<String, String>> getCM_EVENT(Map<String, String> map) throws Exception;

	List<Map<String, String>> getCM_GRP_EVT_USER_LVL(Map<String, String> map) throws Exception;

	List<Map<String, String>> getCM_USER_POPUP(Map<String, String> map) throws Exception;

	List<Map<String, String>> getCM_GRP_EVT_MOBL_LVL(Map<String, String> map) throws Exception;

	List<Map<String, String>> getCM_GRP_MOBL(Map<String, String> map) throws Exception;

	int insert_event(List<Map<String, String>> args) throws Exception;

	int delete_event(List<Map<String, String>> args) throws Exception;

	int insert_user_acc(List<Map<String, String>> args) throws Exception;

	int update_user_acc(List<Map<String, String>> args) throws Exception;

	int delete_user_acc(List<Map<String, String>> args) throws Exception;

	int insert_mobl_acc(List<Map<String, String>> args) throws Exception;
	
	int update_mobl_acc(List<Map<String, String>> args) throws Exception;

	int delete_mobl_acc(List<Map<String, String>> args) throws Exception;

	int insert_user_acc_sel(List<Map<String, String>> args) throws Exception;

	List<Map<String, String>> getCM_USER_SEL_POPUP(Map<String, String> map) throws Exception;

	List<Map<String, String>> getCM_MOBL_SEL_POPUP(Map<String, String> map) throws Exception;

	int insert_mobl_acc_sel(List<Map<String, String>> args) throws Exception;
	
	
}

