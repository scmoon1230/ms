/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : FcltVO.java
 * @Description : 시설물 관련 VO
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
package kr.co.ucp.mntr.cmm.service;

import java.util.List;

public class FcltVO extends CommonVO {
	private static final long serialVersionUID = 453661755860353512L;
	
	private String fcltId;
	private String fcltNm;
	private String fcltKndCd;
	private String fcltKndNm;
	private String sysCd;
	private String sysNm;
	private String roadAdresNm;
	private String dstrtCd;
	private String lotnoAdresNm;
	private String pointX;
	private String pointY;
	private String fcltSttus;
	private String fcltSttusNm;
	private String fcltUid;
	private String presetBdwStartNum;
	private String presetNum;
	/* For Search Radius */
	private String lon;
	private String lat;
	private double radius;
	private List<?> presetList;
	
	private String searchFcltKndCd;
	private String searchFcltUsedTyCd;
	private String searchFcltId;
	private String searchDstrtCd;
	private String searchTrmsSysCd;
	private String searchEvtList;

	private String searchFcltSttus;
	private String searchPlcPtrDiv;

	private String searchIncludeFcltUsedTyCdYn;
	private String searchIncludeMissingPlcPtrDivYn;
	private String searchIncludeResultToMapYn;
	private String searchIgnoreIconGisDspYn;
	private String searchIgnoreLayerMngYn;
	private String searchIgnoreScaleYn;
	private String searchLprOnlyYn;
	private String searchAreaCd;
	
	private String searchTvoTrgtYn;		// 영상반출대상여부
	

	private boolean excludeFcltError;
	private String searchFcltViewerType;
	private String vmsLinkYn;
	private String rtspUrl;

	public String getFcltId() {
		return fcltId;
	}

	public void setFcltId(String fcltId) {
		this.fcltId = fcltId;
	}

	public String getFcltNm() {
		return fcltNm;
	}

	public void setFcltNm(String fcltNm) {
		this.fcltNm = fcltNm;
	}

	public String getFcltKndCd() {
		return fcltKndCd;
	}

	public void setFcltKndCd(String fcltKndCd) {
		this.fcltKndCd = fcltKndCd;
	}

	public String getFcltKndNm() {
		return fcltKndNm;
	}

	public void setFcltKndNm(String fcltKndNm) {
		this.fcltKndNm = fcltKndNm;
	}

	public String getSysCd() {
		return sysCd;
	}

	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}

	public String getSysNm() {
		return sysNm;
	}

	public void setSysNm(String sysNm) {
		this.sysNm = sysNm;
	}

	public String getRoadAdresNm() {
		return roadAdresNm;
	}

	public void setRoadAdresNm(String roadAdresNm) {
		this.roadAdresNm = roadAdresNm;
	}

	public String getDstrtCd() {
		return dstrtCd;
	}

	public void setDstrtCd(String dstrtCd) {
		this.dstrtCd = dstrtCd;
	}

	public String getLotnoAdresNm() {
		return lotnoAdresNm;
	}

	public void setLotnoAdresNm(String lotnoAdresNm) {
		this.lotnoAdresNm = lotnoAdresNm;
	}

	public String getPointX() {
		return pointX;
	}

	public void setPointX(String pointX) {
		this.pointX = pointX;
	}

	public String getPointY() {
		return pointY;
	}

	public void setPointY(String pointY) {
		this.pointY = pointY;
	}

	public String getFcltSttus() {
		return fcltSttus;
	}

	public void setFcltSttus(String fcltSttus) {
		this.fcltSttus = fcltSttus;
	}

	public String getFcltSttusNm() {
		return fcltSttusNm;
	}

	public void setFcltSttusNm(String fcltSttusNm) {
		this.fcltSttusNm = fcltSttusNm;
	}

	public String getFcltUid() {
		return fcltUid;
	}

	public void setFcltUid(String fcltUid) {
		this.fcltUid = fcltUid;
	}

	public String getPresetBdwStartNum() {
		return presetBdwStartNum;
	}

	public void setPresetBdwStartNum(String presetBdwStartNum) {
		this.presetBdwStartNum = presetBdwStartNum;
	}

	public String getPresetNum() {
		return presetNum;
	}

	public void setPresetNum(String presetNum) {
		this.presetNum = presetNum;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public List<?> getPresetList() {
		return presetList;
	}

	public void setPresetList(List<?> presetList) {
		this.presetList = presetList;
	}

	public String getSearchFcltKndCd() {
		return searchFcltKndCd;
	}

	public void setSearchFcltKndCd(String searchFcltKndCd) {
		this.searchFcltKndCd = searchFcltKndCd;
	}

	public String getSearchFcltUsedTyCd() {
		return searchFcltUsedTyCd;
	}

	public void setSearchFcltUsedTyCd(String searchFcltUsedTyCd) {
		this.searchFcltUsedTyCd = searchFcltUsedTyCd;
	}

	public String getSearchFcltId() {
		return searchFcltId;
	}

	public void setSearchFcltId(String searchFcltId) {
		this.searchFcltId = searchFcltId;
	}

	public String getSearchDstrtCd() {
		return searchDstrtCd;
	}

	public void setSearchDstrtCd(String searchDstrtCd) {
		this.searchDstrtCd = searchDstrtCd;
	}

	public String getSearchTrmsSysCd() {
		return searchTrmsSysCd;
	}

	public void setSearchTrmsSysCd(String searchTrmsSysCd) {
		this.searchTrmsSysCd = searchTrmsSysCd;
	}

	public String getSearchEvtList() {
		return searchEvtList;
	}

	public void setSearchEvtList(String searchEvtList) {
		this.searchEvtList = searchEvtList;
	}

	public String getSearchFcltSttus() {
		return searchFcltSttus;
	}

	public void setSearchFcltSttus(String searchFcltSttus) {
		this.searchFcltSttus = searchFcltSttus;
	}

	public String getSearchPlcPtrDiv() {
		return searchPlcPtrDiv;
	}

	public void setSearchPlcPtrDiv(String searchPlcPtrDiv) {
		this.searchPlcPtrDiv = searchPlcPtrDiv;
	}

	public String getSearchIncludeFcltUsedTyCdYn() {
		return searchIncludeFcltUsedTyCdYn;
	}

	public void setSearchIncludeFcltUsedTyCdYn(String searchIncludeFcltUsedTyCdYn) {
		this.searchIncludeFcltUsedTyCdYn = searchIncludeFcltUsedTyCdYn;
	}

	public String getSearchIncludeMissingPlcPtrDivYn() {
		return searchIncludeMissingPlcPtrDivYn;
	}

	public void setSearchIncludeMissingPlcPtrDivYn(String searchIncludeMissingPlcPtrDivYn) {
		this.searchIncludeMissingPlcPtrDivYn = searchIncludeMissingPlcPtrDivYn;
	}

	public String getSearchIncludeResultToMapYn() {
		return searchIncludeResultToMapYn;
	}

	public void setSearchIncludeResultToMapYn(String searchIncludeResultToMapYn) {
		this.searchIncludeResultToMapYn = searchIncludeResultToMapYn;
	}

	public String getSearchIgnoreIconGisDspYn() {
		return searchIgnoreIconGisDspYn;
	}

	public void setSearchIgnoreIconGisDspYn(String searchIgnoreIconGisDspYn) {
		this.searchIgnoreIconGisDspYn = searchIgnoreIconGisDspYn;
	}

	public String getSearchIgnoreLayerMngYn() {
		return searchIgnoreLayerMngYn;
	}

	public void setSearchIgnoreLayerMngYn(String searchIgnoreLayerMngYn) {
		this.searchIgnoreLayerMngYn = searchIgnoreLayerMngYn;
	}

	public String getSearchIgnoreScaleYn() {
		return searchIgnoreScaleYn;
	}

	public void setSearchIgnoreScaleYn(String searchIgnoreScaleYn) {
		this.searchIgnoreScaleYn = searchIgnoreScaleYn;
	}

	public String getSearchLprOnlyYn() {
		return searchLprOnlyYn;
	}

	public void setSearchLprOnlyYn(String searchLprOnlyYn) {
		this.searchLprOnlyYn = searchLprOnlyYn;
	}

	public String getSearchAreaCd() {
		return searchAreaCd;
	}

	public void setSearchAreaCd(String searchAreaCd) {
		this.searchAreaCd = searchAreaCd;
	}

	public String getSearchTvoTrgtYn() {
		return searchTvoTrgtYn;
	}

	public void setSearchTvoTrgtYn(String searchTvoTrgtYn) {
		this.searchTvoTrgtYn = searchTvoTrgtYn;
	}

	public boolean isExcludeFcltError() {
		return excludeFcltError;
	}

	public void setExcludeFcltError(boolean excludeFcltError) {
		this.excludeFcltError = excludeFcltError;
	}

	public String getSearchFcltViewerType() {
		return searchFcltViewerType;
	}

	public void setSearchFcltViewerType(String searchFcltViewerType) {
		this.searchFcltViewerType = searchFcltViewerType;
	}

	public String getVmsLinkYn() {
		return vmsLinkYn;
	}

	public void setVmsLinkYn(String vmsLinkYn) {
		this.vmsLinkYn = vmsLinkYn;
	}

	public String getRtspUrl() {
		return rtspUrl;
	}

	public void setRtspUrl(String rtspUrl) {
		this.rtspUrl = rtspUrl;
	}

	@Override
	public String toString() {
		return "FcltVO [fcltId=" + fcltId + ", fcltNm=" + fcltNm + ", fcltKndCd=" + fcltKndCd + ", fcltKndNm=" + fcltKndNm + ", sysCd=" + sysCd + ", sysNm=" + sysNm + ", roadAdresNm=" + roadAdresNm + ", dstrtCd=" + dstrtCd + ", lotnoAdresNm=" + lotnoAdresNm + ", pointX=" + pointX
				+ ", pointY=" + pointY + ", fcltSttus=" + fcltSttus + ", fcltSttusNm=" + fcltSttusNm + ", fcltUid=" + fcltUid + ", presetBdwStartNum=" + presetBdwStartNum + ", presetNum=" + presetNum + ", lon=" + lon + ", lat=" + lat + ", radius=" + radius + ", presetList="
				+ presetList + ", searchFcltKndCd=" + searchFcltKndCd + ", searchFcltUsedTyCd=" + searchFcltUsedTyCd + ", searchFcltId=" + searchFcltId + ", searchDstrtCd=" + searchDstrtCd + ", searchTrmsSysCd=" + searchTrmsSysCd + ", searchEvtList=" + searchEvtList
				+ ", searchFcltSttus=" + searchFcltSttus + ", searchPlcPtrDiv=" + searchPlcPtrDiv + ", searchIncludeFcltUsedTyCdYn=" + searchIncludeFcltUsedTyCdYn + ", searchIncludeMissingPlcPtrDivYn=" + searchIncludeMissingPlcPtrDivYn + ", searchIncludeResultToMapYn="
				+ searchIncludeResultToMapYn + ", searchIgnoreIconGisDspYn=" + searchIgnoreIconGisDspYn + ", searchIgnoreLayerMngYn=" + searchIgnoreLayerMngYn + ", searchIgnoreScaleYn=" + searchIgnoreScaleYn + ", searchLprOnlyYn=" + searchLprOnlyYn + ", searchAreaCd="
				+ searchAreaCd + ", excludeFcltError=" + excludeFcltError + ", searchFcltViewerType=" + searchFcltViewerType + ", vmsLinkYn=" + vmsLinkYn + ", rtspUrl=" + rtspUrl + "]";
	}
}
