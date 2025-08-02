/**
* --------------------------------------------------------------------------------------------------------------
* @Class Name : EventStsMapper.java
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
package kr.co.ucp.wrks.info.sts.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("userConnStsMapper")
public interface UserConnStsMapper
{

	public List<Map<String, String>> getUserConnStatsMonthData(Map<String, String> args);
	public List<Map<String, String>> getUserConnStatsDayData(Map<String, String> args);
	public List<Map<String, String>> getUserConnStatsMonthExcelData(Map<String, String> args);
	public List<Map<String, String>> getUserConnStatsDayExcelData(Map<String, String> args);
	public List<Map<String, String>> getConnectUser(Map<String, String> args);
	public List<Map<String, String>> userConnectStatsYear(Map<String, String> args);



}
