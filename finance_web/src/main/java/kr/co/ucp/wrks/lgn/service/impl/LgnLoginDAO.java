/**
 * --------------------------------------------------------------------------------------------------------------
 * @Class Name : LgnLoginDAO.java
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
package kr.co.ucp.wrks.lgn.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.co.ucp.cmm.LoginVO;
import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("lgnLoginDAO")
public class LgnLoginDAO extends EgovAbstractMapper
{
	public LoginVO login(Map<String, String> args) throws Exception {
		return (LoginVO)selectOne("wrks_lgn.select", args);
	}

	public LoginVO loginEncrypt(Map<String, String> args) throws Exception {
		return (LoginVO)selectOne("wrks_lgn.select_encrypt", args);
	}

	public LoginVO loginSSO(Map<String, String> args) throws Exception {
		return (LoginVO)selectOne("wrks_lgn.select_sso", args);
	}

	public LoginVO findPwd(Map<String, String> args) throws Exception {
		return (LoginVO)selectOne("wrks_lgn.findPwd", args);
	}

	public int changePwd(Map<String, String> args) throws Exception
	{
		String sqlPath = "wrks_lgn.changePwd";
		String dbEncryptTag = args.get("dbEncryptTag");
		sqlPath = sqlPath + dbEncryptTag; 

		return update(sqlPath, args);
	}

	public List<Map<String, String>> getMenuList( Map<String, String> args) throws Exception {
		return selectList("wrks_lgn.get_menu_list", args);
	}

	public int insertConnectUserCnt(LoginVO loginVO) throws Exception  {
		return insert("wrks_lgn.insertConnectUserCnt", loginVO);
	}
	// 로그인 분기 처리
	public LoginVO selectLogin(Map<String, String> args) throws Exception
	{
		String sqlPath = "wrks_lgn.selectLogin";
		String dbEncryptTag = args.get("dbEncryptTag");
		String ssoLoginTag = args.get("ssoLoginTag");

		sqlPath = sqlPath + dbEncryptTag + ssoLoginTag;
		return (LoginVO)selectOne(sqlPath, args);
	}
}
