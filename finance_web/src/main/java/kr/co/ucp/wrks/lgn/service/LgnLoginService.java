/**
 * --------------------------------------------------------------------------------------------------------------
 * @Class Name : LgnLoginService.java
 * @Description : 일반 로그인을 처리
 * @Version : 1.0
 * Copyright (c) 2016 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * --------------------------------------------------------------------------------------------------------------
 * DATE            AUTHOR      DESCRIPTION
 * --------------------------------------------------------------------------------------------------------------
 * 2016. 11.08.    seungJun    최초작성
 * --------------------------------------------------------------------------------------------------------------
 */
package kr.co.ucp.wrks.lgn.service;

import java.util.List;
import java.util.Map;

import kr.co.ucp.cmm.LoginVO;

public interface LgnLoginService
{

	LoginVO selectLogin(Map<String, String> args) throws Exception;

	int insertConnectUserCnt(LoginVO loginVO) throws Exception;

	List<Map<String, String>> getMenuList(Map<String, String> args) throws Exception;

	LoginVO apply(Map<String, String> args) throws Exception;
	
	LoginVO findPwd(Map<String, String> args) throws Exception;
	
	int changePwd(Map<String, String> args) throws Exception;

//	LoginVO getUserInfo(Map<String, String> args) throws Exception;

	//LoginVO selectLoginPass(Map<String, String> args) throws Exception;

	Map<String, String> selectSystemInfo(Map<String, String> args);

	String selectRepTelNo( Map<String, String> args) throws Exception;

}