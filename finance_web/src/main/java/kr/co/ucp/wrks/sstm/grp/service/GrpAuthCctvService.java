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
public interface GrpAuthCctvService {

	List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map)
			throws Exception;

	List<Map<String, String>> getGroupList(Map<String, String> map)
			throws Exception;

	List<Map<String, String>> getUserList(Map<String, String> map)
			throws Exception;

	List<Map<String, String>> getCctvUsedList(Map<String, String> map)
			throws Exception;

	List<Map<String, String>> getCctvAuthList(Map<String, String> map)
			throws Exception;

	List<Map<String, String>> getFcltUseList(Map<String, String> map)
			throws Exception;

	int insert_cctv_used(List<Map<String, String>> args) throws Exception;

	int delete_cctv_used(List<Map<String, String>> args) throws Exception;

	int insert_cctv_auth(List<Map<String, String>> args) throws Exception;

	int delete_cctv_auth(List<Map<String, String>> args) throws Exception;
}

