/**
* --------------------------------------------------------------------------------------------------------------
* @Class Name : EventStsService.java
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

public interface UserConnStsService 
{
	List<Map<String, String>> getUserConnStatsData(Map<String, String> args) throws Exception;
	List<Map<String, String>> getUserConnStatsExcelData(Map<String, String> args) throws Exception;
	List<Map<String, String>> getConnectUser(Map<String, String> args) throws Exception;
	List<Map<String, String>> userConnectStatsYear(Map<String, String> args) throws Exception;
}
