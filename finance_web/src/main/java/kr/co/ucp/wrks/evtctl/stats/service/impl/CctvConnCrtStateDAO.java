/**
 *---------------------------------------------------------------------------------- 
 * @File Name    : CctvConnCrtStateMapper.java
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("cctvConnCrtStateDAO")
public class CctvConnCrtStateDAO extends EgovAbstractMapper
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	public List<Map<String, String>> getYearList() {
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getYearList", null);
	}
	
	public List<Map<String, String>> getEventList(Map<String, Object> args) {
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getEventList", args);
	}

	public List<Map<String, String>> getMonthTypeList(Map<String, String> args) {
		logger.debug("###########searchYear=" + args.get("searchYear"));
		logger.debug("###########searchSysCd=" + args.get("searchSysCd"));
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getMonthTypeList", args);
	}

	public List<Map<String, String>> getMonthTypeExcel(Map<String, String> args) {
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getMonthTypeExcel", args);
	}

	public List<Map<String, String>> getMonthUserList(Map<String, String> args) {
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getMonthUserList", args);
	}

	public List<Map<String, String>> getMonthUserExcel(Map<String, String> args) {
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getMonthUserExcel", args);
	}

	public List<Map<String, String>> getTearmTypeList(Map<String, String> args) {
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getTearmTypeList", args);
	}

	public List<Map<String, String>> getTearmTypeExcel(Map<String, String> args) {
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getTearmTypeExcel", args);
	}

	public List<Map<String, String>> getTearmUserList(Map<String, String> args) {
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getTearmUserList", args);
	}

	public List<Map<String, String>> getTearmUserExcel(Map<String, String> args) {
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getTearmUserExcel", args);
	}

	public List<Map<String, String>> getConnDetailList(Map<String, String> args) {
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getConnDetailList", args);
	}

	public List<Map<String, String>> getConnDetailExcel(Map<String, String> args) {
		return selectList("wrks_evtctl_stats_cctv_conn_crt_state.getConnDetailExcel", args);
	}
}
