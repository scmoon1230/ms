/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : GeoDataMapper.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 3. 17. SaintJuny@ubolt.co.kr 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.gis.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.mntr.cmm.service.FcltVO;

@Mapper("geoDataMapper")
public interface GeoDataMapper {

	public List<EgovMap> selectFcltGeoData(FcltVO vo) throws Exception;
	public List<EgovMap> selectFcltAngleGeoData(FcltVO vo) throws Exception;
	public List<EgovMap> selectLcInfoGeoData(Map<String, String> params) throws Exception;

	public EgovMap selectFcltById(FcltVO vo) throws Exception;
	public EgovMap selectFcltByUid(Map<String, String> params) throws Exception;
	public EgovMap selectNearestCctv(FcltVO vo) throws Exception;

	public List<EgovMap> selectNearestCctvList(FcltVO vo) throws Exception;
	public int selectNearestCctvListTotCnt(FcltVO vo) throws Exception;
	public List<EgovMap> selectFcltByMngSn(FcltVO vo) throws Exception;

	public List<EgovMap> selectNearestPresetForCastNet(FcltVO vo) throws Exception;
	public List<EgovMap> selectNearestPresetListForCastNet(FcltVO vo) throws Exception;
	public List<EgovMap> selectNearestCctvForCastNet(FcltVO vo) throws Exception;

	String getGisProjection(String gisEngine);
	List<EgovMap> selectFcltPointList();
	void updateFcltPointProjection(EgovMap map);

}


