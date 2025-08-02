/**
* --------------------------------------------------------------------------------------------------------------
* @Class Name : CalamityCctvServiceImpl.java
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

import javax.annotation.Resource;

import kr.co.ucp.wrks.info.state.service.CalamityCctvService;

import org.springframework.stereotype.Service;


@Service("calamityCctvService")
public class CalamityCctvServiceImpl implements CalamityCctvService
{

	@Resource (name="calamityCctvMapper")
	private CalamityCctvMapper calamityCctvMapper;
	
	@Override
	public List<Map<String, String>> calamityCctvListData(Map<String, String> args) throws Exception  {
		return calamityCctvMapper.calamityCctvListData(args);
	}
		

		@Override
		public List<Map<String, String>> calDetail(Map<String, String> args) throws Exception  {
			return calamityCctvMapper.calDetail(args);
	}
}


