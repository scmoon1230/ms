/**
* ----------------------------------------------------------------------------------------------
* @Class Name : ApiService.java
* @Description : Api와 관련된 요청을 처리하는 서비스 인터페이스
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
package kr.co.ucp.mntr.api.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface ApiService {

	public EgovMap selectCoordToAddrList(ApiSrchVO vo) throws Exception;

	public EgovMap selectAddrToCoordList(ApiSrchVO vo) throws Exception;

	public EgovMap selectKeywordToCoordList(ApiSrchVO vo) throws Exception;

	public Object selectAddrListByPnu(ApiSrchVO vo) throws Exception;
}
