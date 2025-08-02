/**
 * --------------------------------------------------------------------------------------------------------------
 * @Class Name : LgnLoginServiceImpl.java
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.wrks.lgn.service.LgnLoginMapper;
import kr.co.ucp.wrks.lgn.service.LgnLoginService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("lgnLoginService")
public class LgnLoginServiceImpl extends EgovAbstractServiceImpl implements LgnLoginService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="lgnLoginMapper")
	private LgnLoginMapper lgnLoginMapper;

/*	
	// 일반 로그인을 처리한다
    @Override
	public LoginVO login(Map<String, String> args) throws Exception {
		// 2. 아이디와 암호화된 비밀번호가 DB와 일치하는지 확인한다.
		LoginVO loginVO = lgnLoginMapper.login(args);
		// 3. 결과를 리턴한다.
		if (loginVO != null && !loginVO.getUserId().equals("") && !loginVO.getPassword().equals("")) {
			return loginVO;
		} else {
			loginVO = new LoginVO();
		}
		return loginVO;
	}
	
	// 일반 로그인을 처리한다
    @Override
	public LoginVO loginEncrypt(Map<String, String> args) throws Exception {
		// 2. 아이디와 암호화된 비밀번호가 DB와 일치하는지 확인한다.
		LoginVO loginVO = lgnLoginMapper.loginEncrypt(args);
		// 3. 결과를 리턴한다.
		if (loginVO != null && !loginVO.getUserId().equals("") && !loginVO.getPassword().equals("")) {
			return loginVO;
		} else {
			loginVO = new LoginVO();
		}
		return loginVO;
	}
	
	// SSO 로그인을 처리한다
    @Override
	public LoginVO loginSSO(Map<String, String> args) throws Exception {
		// 2. 아이디와 암호화된 비밀번호가 DB와 일치하는지 확인한다.
		LoginVO loginVO = lgnLoginMapper.loginSSO(args);
		// 3. 결과를 리턴한다.
		if (loginVO != null && !loginVO.getUserId().equals("") && !loginVO.getPassword().equals("")) {
			return loginVO;
		} else {
			loginVO = new LoginVO();
		}
		return loginVO;
	}
*/
//    @Override
//	public LoginVO getUserInfo(Map<String, String> args) throws Exception {
//		// 2. 아이디와 암호화된 비밀번호가 DB와 일치하는지 확인한다.
//		LoginVO loginVO = lgnLoginMapper.getUserInfo(args);
//		// 3. 결과를 리턴한다.
//		if (loginVO != null && !loginVO.getUserId().equals("") && !loginVO.getPassword().equals("")) {
//			return loginVO;
//		} else {
//			loginVO = new LoginVO();
//		}
//		return loginVO;
//	}
    
	// 회원가입 신청
    @Override
	public LoginVO apply(Map<String, String> args) throws Exception {
		LoginVO loginVO = lgnLoginMapper.findPwd(args);
		// 3. 결과를 리턴한다.
		if (loginVO != null && !loginVO.getUserId().equals("") && !loginVO.getUserPwd().equals("")) {
			return loginVO;
		} else {
			loginVO = new LoginVO();
		}
		return loginVO;
	}

	// 패스워드 찾기
    @Override
	public LoginVO findPwd(Map<String, String> args) throws Exception {
		LoginVO loginVO = lgnLoginMapper.findPwd(args);
		// 3. 결과를 리턴한다.
		if (loginVO != null && !loginVO.getUserId().equals("") && !loginVO.getUserPwd().equals("")) {
//			Map<String, String> tmp = new HashMap<String, String>();
//			tmp.put("AAA", "111");
//			lgnLoginSmsDAO.insert(tmp);
			return loginVO;
		} else {
			loginVO = new LoginVO();
		}
		return loginVO;
	}

	// 비밀번호 변경
    @Override
	public int changePwd(Map<String, String> args) throws Exception {
		return lgnLoginMapper.changePwd(args);
	}

	// 메뉴목록조회
    @Override
	public List<Map<String, String>> getMenuList( Map<String, String> args) throws Exception {
		List<Map<String, String>> list = lgnLoginMapper.getMenuList(args);
		return list;
	}

	// 로그인 이력 저장
    @Override
	public int insertConnectUserCnt(LoginVO loginVO) throws Exception {
		return lgnLoginMapper.insertConnectUserCnt(loginVO);
		
	}

	// 프로퍼티에 따라 목록조회
    @Override
	public LoginVO selectLogin(Map<String, String> args) throws Exception
	{
//		LoginVO loginVO = lgnLoginDAO.selectLogin(args);
		args.put("loginTag", args.get("dbEncryptTag") + args.get("ssoLoginTag"));

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			args.put("saltText", saltText);
		}

		LoginVO loginVO = lgnLoginMapper.selectLogin(args);
		
		if (loginVO != null && !loginVO.getUserId().equals("") && !loginVO.getUserPwd().equals("")) {
			return loginVO;
		} else {
			loginVO = new LoginVO();
		}
		return loginVO;
	}
    
//	// 프로퍼티에 따라 목록조회
//    @Override
//	public LoginVO selectLoginPass(Map<String, String> args) throws Exception
//	{
////		LoginVO loginVO = lgnLoginDAO.selectLogin(args);
//		args.put("loginTag", args.get("dbEncryptTag") + args.get("ssoLoginTag"));
//		
//		LoginVO loginVO = lgnLoginMapper.selectLoginPass(args);
//		
//		if (loginVO != null && !loginVO.getUserId().equals("") && !loginVO.getPassword().equals("")) {
//			return loginVO;
//		} else {
//			loginVO = new LoginVO();
//		}
//		return loginVO;
//	}

	@Override
	public Map<String, String> selectSystemInfo(Map<String, String> args) {
		return lgnLoginMapper.selectSystemInfo(args);
	}

	@Override
	public String selectRepTelNo(Map<String, String> args) throws Exception {
		return lgnLoginMapper.selectRepTelNo(args);
	}

}