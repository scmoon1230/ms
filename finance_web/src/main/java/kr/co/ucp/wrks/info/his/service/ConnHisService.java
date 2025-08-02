/**
* --------------------------------------------------------------------------------------------------------------
* @Class Name : ConnHisService.java
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
package kr.co.ucp.wrks.info.his.service;

import java.util.List;
import java.util.Map;

public interface ConnHisService 
{

	// 접속이력 조회
	List<Map<String, String>> connHisListData(Map<String, String> args) throws Exception;

	List<Map<String, String>> connHisListExcel(Map<String, String> args) throws Exception;

	List<Map<String, String>> menuUsedListData(Map<String, String> args) throws Exception;

	List<Map<String, String>> menuUsedListExcel(Map<String, String> args) throws Exception;
	
}
