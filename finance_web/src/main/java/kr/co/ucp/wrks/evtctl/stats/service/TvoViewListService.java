/**
 *----------------------------------------------------------------------------------
 * @File Name    : TvoViewCrtStateService.java
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

public interface TvoViewListService
{

	List<Map<String, String>> tvoViewList(Map<String, String> args) throws Exception;

	List<Map<String, String>> tvoViewListExcel(Map<String, String> args) throws Exception;

}
