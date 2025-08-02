/**
* --------------------------------------------------------------------------------------------------------------
* @Class Name : CalamityCctvMapper.java
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
package kr.co.ucp.wrks.info.state.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("calamityCctvMapper")
public class CalamityCctvMapper extends EgovAbstractMapper
{

	public List<Map<String, String>> calamityCctvListData(Map<String, String> args) {
		return selectList("es_state_calamity_cctv_list.calamityCctvListData", args);
	}

	public List<Map<String, String>> calDetail(Map<String, String> args) {
		return selectList("es_state_calamity_cctv_list.calamityCctvListData", args);
	}


}

