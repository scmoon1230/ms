package kr.co.ucp.wrks.sstm.grp.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : GrpAuthService
 * @Description : 그룹별 권한레벨 관리
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
public interface GrpAuthService {

	List<Map<String, String>> list(Map<String, String> args) throws Exception;

	List<Map<String, String>> authList(Map<String, String> args) throws Exception;

	int insert(Map<String, String> args) throws Exception;

	int update(Map<String, Object> args) throws Exception;

	int delete(Map<String, Object> args) throws Exception;

	int deleteMulti(List<Map<String, Object>> args) throws Exception;
	
}
