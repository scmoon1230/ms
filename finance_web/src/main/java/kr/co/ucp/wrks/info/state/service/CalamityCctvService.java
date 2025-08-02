/**
* --------------------------------------------------------------------------------------------------------------
* @Class Name : CalamityCctvService.java
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
package kr.co.ucp.wrks.info.state.service;

import java.util.List;
import java.util.Map;

public interface CalamityCctvService 
{
	// 재난번호별 CCTV목록데이터 조회
		List<Map<String, String>> calamityCctvListData(Map<String, String> args) throws Exception;
		
		List<Map<String, String>> calDetail(Map<String, String> args) throws Exception;
		
}
