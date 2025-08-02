/**
 *----------------------------------------------------------------------------------
 * @File Name    : CctvConnCrtStateServiceImpl.java
 * @Description  : 긴급영상제공통계 CCTV 접속현황
 * @author       : seungJun
 * @since        : 2017. 2. 22.
 * @version      : 1.0
 *----------------------------------------------------------------------------------
 * @Copyright (c)2017 UCP, WideCUBE, All Rights Reserved.
 *----------------------------------------------------------------------------------
 * Data           Author      Description
 *----------------------------------------------------------------------------------
 * 2017. 2. 22.    seungJun    최초작성
 *----------------------------------------------------------------------------------
 */
package kr.co.ucp.wrks.evtctl.stats.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.wrks.evtctl.stats.service.CctvConnListMapper;
import kr.co.ucp.wrks.evtctl.stats.service.CctvConnListService;

import org.springframework.stereotype.Service;

@Service("cctvConnListService")
public class CctvConnListServiceImpl implements CctvConnListService
{
	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name = "cctvConnListMapper")
	private CctvConnListMapper cctvConnListMapper;

	@Override
	public List<Map<String, String>> cctvConnList(Map<String, String> args) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			args.put("saltText", saltText);
		}

		return cctvConnListMapper.cctvConnList(args);
	}

	@Override
	public List<Map<String, String>> cctvConnListExcel(Map<String, String> args) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			args.put("saltText", saltText);
		}

		return cctvConnListMapper.cctvConnListExcel(args);
	}
	@Override
	public List<Map<String, String>> getList(Map<String, String> args) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			args.put("saltText", saltText);
		}

		String searchMonth = EgovStringUtil.nullConvert(args.get("searchMonth"));
		if ("".equals(searchMonth)) {
			return cctvConnListMapper.selectMonthList(args);
		} else {
			return cctvConnListMapper.selectDayList(args);
		}
	}
	@Override
	public List<Map<String, String>> getExcel(Map<String, String> args) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			args.put("saltText", saltText);
		}

		String searchMonth = EgovStringUtil.nullConvert(args.get("searchMonth"));
		if ("".equals(searchMonth)) {
			return cctvConnListMapper.selectMonthExcel(args);
		} else {
			return cctvConnListMapper.selectDayExcel(args);
		}
	}
}
