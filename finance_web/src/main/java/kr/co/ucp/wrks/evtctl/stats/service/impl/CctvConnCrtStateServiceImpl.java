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

import kr.co.ucp.wrks.evtctl.stats.service.CctvConnCrtStateMapper;
import kr.co.ucp.wrks.evtctl.stats.service.CctvConnCrtStateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("cctvConnCrtStateService")
public class CctvConnCrtStateServiceImpl implements CctvConnCrtStateService
{
	@Resource(name="cctvConnCrtStateMapper")
	private CctvConnCrtStateMapper cctvConnCrtStateMapper;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public List<Map<String, String>> getYearList() throws Exception {
		return cctvConnCrtStateMapper.getYearList();
	}

	@Override
	public List<Map<String, String>> getEventList(Map<String, Object> args) throws Exception {
		return cctvConnCrtStateMapper.getEventList(args);
	}

	@Override
	public List<Map<String, String>> getMonthTypeList(Map<String, String> args) throws Exception {
		logger.debug("###########searchYear=" + args.get("searchYear"));
		logger.debug("###########searchSysCd=" + args.get("searchSysCd"));		
		return cctvConnCrtStateMapper.getMonthTypeList(args);
	}

	@Override
	public List<Map<String, String>> getMonthTypeExcel(Map<String, String> args) throws Exception {
		return cctvConnCrtStateMapper.getMonthTypeExcel(args);
	}

	@Override
	public List<Map<String, String>> getMonthUserList(Map<String, String> args) throws Exception {
		return cctvConnCrtStateMapper.getMonthUserList(args);
	}

	@Override
	public List<Map<String, String>> getMonthUserExcel(Map<String, String> args) throws Exception {
		return cctvConnCrtStateMapper.getMonthUserExcel(args);
	}

	@Override
	public List<Map<String, String>> getTearmTypeList(Map<String, String> args) throws Exception {
		return cctvConnCrtStateMapper.getTearmTypeList(args);
	}

	@Override
	public List<Map<String, String>> getTearmTypeExcel(Map<String, String> args) throws Exception {
		return cctvConnCrtStateMapper.getTearmTypeExcel(args);
	}

	@Override
	public List<Map<String, String>> getTearmUserList(Map<String, String> args) throws Exception {
		return cctvConnCrtStateMapper.getTearmUserList(args);
	}

	@Override
	public List<Map<String, String>> getTearmUserExcel(Map<String, String> args) throws Exception {
		return cctvConnCrtStateMapper.getTearmUserExcel(args);
	}

	@Override
	public List<Map<String, String>> getConnDetailList(Map<String, String> args) throws Exception {
		return cctvConnCrtStateMapper.getConnDetailList(args);
	}

	@Override
	public List<Map<String, String>> getConnDetailExcel(Map<String, String> args) throws Exception {
		return cctvConnCrtStateMapper.getConnDetailExcel(args);
	}
}
