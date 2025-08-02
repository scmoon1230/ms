package kr.co.ucp.wrks.sstm.grp.service;

import java.util.List;
import java.util.Map;


/**
 * ----------------------------------------------------------
 * @Class Name : GrpRoleService
 * @Description : 롤관리
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-02-16   설준환       최초작성
 * 
 * ----------------------------------------------------------
 * */
public interface GrpRoleService {
	
	/*
	 * 롤 조건검색
	 */
	@SuppressWarnings("rawtypes")
	List view(Map<String, String> args) throws Exception;
	
	/*
	 * 롤 입력
	 */
	int insert(Map<String, Object> args) throws Exception;

	/*
	 * 롤 수정
	 */
	int update(Map<String, Object> args) throws Exception;
	
	/*
	 * 롤 삭제
	 */
	int delete(Map<String, Object> args) throws Exception;
	
	/*
	 * 롤 다중삭제
	 */
	int deleteMulti(List<Map<String, String>> args) throws Exception;
}


