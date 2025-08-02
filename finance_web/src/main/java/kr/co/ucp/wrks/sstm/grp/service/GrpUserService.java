package kr.co.ucp.wrks.sstm.grp.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : GrpUserService
 * @Description : 그룹별 사용자 조회
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
public interface GrpUserService {

	List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map)
			throws Exception;

	List<Map<String, String>> getCM_GROUP(Map<String, String> map)
			throws Exception;

	List<Map<String, String>> getCM_GRP_USER(Map<String, String> map)
			throws Exception;
}