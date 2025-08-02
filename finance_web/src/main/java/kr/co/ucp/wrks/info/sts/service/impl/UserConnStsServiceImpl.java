/**
* --------------------------------------------------------------------------------------------------------------
* @Class Name : EventStsServiceImpl.java
* @Description :
* @Version : 1.0
* Copyright (c) 2015 by KR.CO.UCP.CNU All rights reserved.
* @Modification Information
* --------------------------------------------------------------------------------------------------------------
* DATE           AUTHOR     DESCRIPTION
* --------------------------------------------------------------------------------------------------------------
* 2015. 10. 1.   inhan29    최초작성
*
* --------------------------------------------------------------------------------------------------------------
*/
package kr.co.ucp.wrks.info.sts.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.wrks.info.sts.service.UserConnStsMapper;
import kr.co.ucp.wrks.info.sts.service.UserConnStsService;

import org.springframework.stereotype.Service;

@Service("userConnStsService")
public class UserConnStsServiceImpl implements UserConnStsService
{
	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource (name="userConnStsMapper")
	private UserConnStsMapper userConnStsMapper;

	@Override
	public List<Map<String, String>> getConnectUser(Map<String, String> args) throws Exception {
		return userConnStsMapper.getConnectUser(args);
	}

	@Override
	public List<Map<String, String>> userConnectStatsYear(Map<String, String> args) throws Exception {
		return userConnStsMapper.userConnectStatsYear(args);
	}

	@Override
	public List<Map<String, String>> getUserConnStatsData(Map<String, String> args) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			args.put("saltText", saltText);
		}

		String searchMonth = EgovStringUtil.nullConvert(args.get("searchMonth"));
    	if ("".equals(searchMonth)) {
    		return userConnStsMapper.getUserConnStatsMonthData(args);
		} else {
			return userConnStsMapper.getUserConnStatsDayData(args);
		}
	}
	
	public List<Map<String, String>> getUserConnStatsExcelData(Map<String, String> args) throws Exception {
	
		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			args.put("saltText", saltText);
		}

		String searchMonth = EgovStringUtil.nullConvert(args.get("searchMonth"));
    	if ("".equals(searchMonth)) {
    		return userConnStsMapper.getUserConnStatsMonthExcelData(args);
		} else {
			return userConnStsMapper.getUserConnStatsDayExcelData(args);
		}
	}


}