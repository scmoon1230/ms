package kr.co.ucp.wrks.sstm.grp.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : GrpCodeService
 * @Description : 그룹관리
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
public interface GrpCodeService {

	List<Map<String, String>> getDstrtList(Map<String, String> args)
			throws Exception;

	List<Map<String, String>> getLkSysIdList(Map<String, String> args)
			throws Exception;

	List<Map<String, String>> getList(Map<String, String> args)
			throws Exception;

	int insert(Map<String, Object> args) throws Exception;

	int update(Map<String, Object> args) throws Exception;

	int delete(Map<String, Object> args) throws Exception;

	int deleteMulti(List<Map<String, Object>> args) throws Exception;
}


