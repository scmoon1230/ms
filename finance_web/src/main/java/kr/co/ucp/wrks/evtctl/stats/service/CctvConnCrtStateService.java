/**
 *---------------------------------------------------------------------------------- 
 * @File Name    : CctvConnCrtStateService.java
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
package kr.co.ucp.wrks.evtctl.stats.service;

import java.util.List;
import java.util.Map;

public interface CctvConnCrtStateService
{
	List<Map<String, String>> getYearList() throws Exception;
	List<Map<String, String>> getEventList(Map<String, Object> args) throws Exception;
	List<Map<String, String>> getMonthTypeList(Map<String, String> args) throws Exception;
	List<Map<String, String>> getMonthTypeExcel(Map<String, String> args) throws Exception;
	List<Map<String, String>> getMonthUserList(Map<String, String> args) throws Exception;
	List<Map<String, String>> getMonthUserExcel(Map<String, String> args) throws Exception;
	List<Map<String, String>> getTearmTypeList(Map<String, String> args) throws Exception;
	List<Map<String, String>> getTearmTypeExcel(Map<String, String> args) throws Exception;
	List<Map<String, String>> getTearmUserList(Map<String, String> args) throws Exception;
	List<Map<String, String>> getTearmUserExcel(Map<String, String> args) throws Exception;
	List<Map<String, String>> getConnDetailList(Map<String, String> args) throws Exception;
	List<Map<String, String>> getConnDetailExcel(Map<String, String> args) throws Exception;
}
