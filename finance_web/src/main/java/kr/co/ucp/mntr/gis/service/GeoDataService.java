/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : GeoDataService.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 10. 28. SaintJuny@ubolt.co.kr 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.gis.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.mntr.cmm.service.FcltVO;

public interface GeoDataService {

	List<EgovMap> selectFcltGeoData(FcltVO vo) throws Exception;
	List<EgovMap> selectFcltAngleGeoData(FcltVO vo) throws Exception;
	List<EgovMap> selectLcInfoGeoData(Map<String, String> params) throws Exception;

	EgovMap selectFcltById(FcltVO vo) throws Exception;
	EgovMap selectFcltByUid(Map<String, String> params) throws Exception;
	EgovMap selectNearestCctv(FcltVO vo) throws Exception;

	List<EgovMap> selectNearestCctvList(FcltVO vo) throws Exception;
	int selectNearestCctvListTotCnt(FcltVO vo) throws Exception;
	List<EgovMap> selectFcltByMngSn(FcltVO vo) throws Exception;

	Map<String, Object> selectCastNetCctvList(FcltVO vo) throws Exception;

	int updatePointProjection(String gisEngine) throws Exception;
}
