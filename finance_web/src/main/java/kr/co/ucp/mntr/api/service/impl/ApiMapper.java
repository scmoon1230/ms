/**
* ----------------------------------------------------------------------------------------------
* @Class Name : ApiServiceImpl.java
* @Description : Api와 관련된 요청을 처리하는 MyBatis Mapper
* @Version : 1.0
* Copyright (c) 2017 by KR.CO.WIDECUBE All rights reserved.
* @Modification Information
* ----------------------------------------------------------------------------------------------
* DATE AUTHOR DESCRIPTION
* ----------------------------------------------------------------------------------------------
* 2017. 10. 25. saintjuny 최초작성
*
* ----------------------------------------------------------------------------------------------
*/
package kr.co.ucp.mntr.api.service.impl;

import java.util.List;

import kr.co.ucp.mntr.api.service.ApiSrchVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper("apiMapper")
public interface ApiMapper {

	public List<EgovMap> selectCoordToAddrList(ApiSrchVO vo) throws Exception;

	public List<EgovMap> selectAddrToCoordList(ApiSrchVO vo) throws Exception;

	public List<EgovMap> selectFcltListByCoord(ApiSrchVO vo) throws Exception;

	public List<EgovMap> selectKeywordToCoordList(ApiSrchVO vo) throws Exception;

	public List<EgovMap> selectAddrListByPnu(ApiSrchVO vo) throws Exception;
}
