package kr.co.ucp.mntr.fclt.service;

import java.util.Arrays;

import kr.co.ucp.mntr.cmm.service.FcltVO;

public class FcltSrchVO extends FcltVO {

	private static final long serialVersionUID = -6663791092689723643L;

	/* For Search */
	private String searchCondition = "";
	private String searchKeyword = "";

	private String searchTermStart = "";
	private String searchTermEnd = "";

	private String searchFcltGubun = "";
	private String searchFcltUsedTyCd = "";
	private String searchStatusGubun = "";
	private String searchStatusNm = "";

	private String searchFcltId = "";
	private String searchFcltAddr = "";

	private String searchYear = "";
	private String searchMonth = "";
	private String searchUserType = "";
	private String searchEventType = "";

	private String searchFcltSttus = "";
	private String searchPlcPtrDiv = "";

	private String searchIncludeFcltUsedTyCdYn = "";
	private String searchIncludeMissingPlcPtrDivYn = "";
	private String searchIncludeResultToMapYn = "";
	private String searchLprOnlyYn = "";

	/* For Authority */
	private String viewerTyCd;
	private String viewerPtzYn;

	/* For Search Radius */
	private String lon;
	private String lat;
	private double radius;

	/* Excel Download */
	private String columnName;
	private String nullColumn;
	private String selectFcltUsed;
	private String[] chkFcltUseTy;
	private String titleNm;
	private String titleNmChk;

	/* For Video Stream */
	private String vmsLinkYn;
	private String rtspUrl;

	/* Fclt Regist History */
	private String srlNo;
	private int allCnt;
	private int regCnt;
	private int updCnt;
	private int delCnt;
	private String userNm;
	private String uplCol;

	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getSearchTermStart() {
		return searchTermStart;
	}

	public void setSearchTermStart(String searchTermStart) {
		this.searchTermStart = searchTermStart;
	}

	public String getSearchTermEnd() {
		return searchTermEnd;
	}

	public void setSearchTermEnd(String searchTermEnd) {
		this.searchTermEnd = searchTermEnd;
	}

	public String getSearchFcltGubun() {
		return searchFcltGubun;
	}

	public void setSearchFcltGubun(String searchFcltGubun) {
		this.searchFcltGubun = searchFcltGubun;
	}

	public String getSearchFcltUsedTyCd() {
		return searchFcltUsedTyCd;
	}

	public void setSearchFcltUsedTyCd(String searchFcltUsedTyCd) {
		this.searchFcltUsedTyCd = searchFcltUsedTyCd;
	}

	public String getSearchStatusGubun() {
		return searchStatusGubun;
	}

	public void setSearchStatusGubun(String searchStatusGubun) {
		this.searchStatusGubun = searchStatusGubun;
	}

	public String getSearchStatusNm() {
		return searchStatusNm;
	}

	public void setSearchStatusNm(String searchStatusNm) {
		this.searchStatusNm = searchStatusNm;
	}

	public String getSearchFcltId() {
		return searchFcltId;
	}

	public void setSearchFcltId(String searchFcltId) {
		this.searchFcltId = searchFcltId;
	}

	public String getSearchFcltAddr() {
		return searchFcltAddr;
	}

	public void setSearchFcltAddr(String searchFcltAddr) {
		this.searchFcltAddr = searchFcltAddr;
	}

	public String getSearchYear() {
		return searchYear;
	}

	public void setSearchYear(String searchYear) {
		this.searchYear = searchYear;
	}

	public String getSearchMonth() {
		return searchMonth;
	}

	public void setSearchMonth(String searchMonth) {
		this.searchMonth = searchMonth;
	}

	public String getSearchUserType() {
		return searchUserType;
	}

	public void setSearchUserType(String searchUserType) {
		this.searchUserType = searchUserType;
	}

	public String getSearchEventType() {
		return searchEventType;
	}

	public void setSearchEventType(String searchEventType) {
		this.searchEventType = searchEventType;
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

	public String getSearchLprOnlyYn() {
		return searchLprOnlyYn;
	}

	public void setSearchLprOnlyYn(String searchLprOnlyYn) {
		this.searchLprOnlyYn = searchLprOnlyYn;
	}

	public String getViewerTyCd() {
		return viewerTyCd;
	}

	public void setViewerTyCd(String viewerTyCd) {
		this.viewerTyCd = viewerTyCd;
	}

	public String getViewerPtzYn() {
		return viewerPtzYn;
	}

	public void setViewerPtzYn(String viewerPtzYn) {
		this.viewerPtzYn = viewerPtzYn;
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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getNullColumn() {
		return nullColumn;
	}

	public void setNullColumn(String nullColumn) {
		this.nullColumn = nullColumn;
	}

	public String getSelectFcltUsed() {
		return selectFcltUsed;
	}

	public void setSelectFcltUsed(String selectFcltUsed) {
		this.selectFcltUsed = selectFcltUsed;
	}

	public String[] getChkFcltUseTy() {
		return chkFcltUseTy;
	}

	public void setChkFcltUseTy(String[] chkFcltUseTy) {
		this.chkFcltUseTy = chkFcltUseTy;
	}

	public String getTitleNm() {
		return titleNm;
	}

	public void setTitleNm(String titleNm) {
		this.titleNm = titleNm;
	}

	public String getTitleNmChk() {
		return titleNmChk;
	}

	public void setTitleNmChk(String titleNmChk) {
		this.titleNmChk = titleNmChk;
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

	public String getSrlNo() {
		return srlNo;
	}

	public void setSrlNo(String srlNo) {
		this.srlNo = srlNo;
	}

	public int getAllCnt() {
		return allCnt;
	}

	public void setAllCnt(int allCnt) {
		this.allCnt = allCnt;
	}

	public int getRegCnt() {
		return regCnt;
	}

	public void setRegCnt(int regCnt) {
		this.regCnt = regCnt;
	}

	public int getUpdCnt() {
		return updCnt;
	}

	public void setUpdCnt(int updCnt) {
		this.updCnt = updCnt;
	}

	public int getDelCnt() {
		return delCnt;
	}

	public void setDelCnt(int delCnt) {
		this.delCnt = delCnt;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getUplCol() {
		return uplCol;
	}

	public void setUplCol(String uplCol) {
		this.uplCol = uplCol;
	}

	@Override
	public String toString() {
		return "FcltSrchVO [searchCondition=" + searchCondition + ", searchKeyword=" + searchKeyword + ", searchTermStart=" + searchTermStart + ", searchTermEnd=" + searchTermEnd + ", searchFcltGubun=" + searchFcltGubun + ", searchFcltUsedTyCd=" + searchFcltUsedTyCd + ", searchStatusGubun=" + searchStatusGubun + ", searchStatusNm=" + searchStatusNm + ", searchFcltId=" + searchFcltId + ", searchFcltAddr=" + searchFcltAddr + ", searchYear=" + searchYear + ", searchMonth=" + searchMonth + ", searchUserType=" + searchUserType + ", searchEventType=" + searchEventType + ", searchFcltSttus=" + searchFcltSttus + ", searchPlcPtrDiv=" + searchPlcPtrDiv + ", searchIncludeFcltUsedTyCdYn=" + searchIncludeFcltUsedTyCdYn + ", searchIncludeMissingPlcPtrDivYn=" + searchIncludeMissingPlcPtrDivYn + ", searchIncludeResultToMapYn=" + searchIncludeResultToMapYn + ", searchLprOnlyYn=" + searchLprOnlyYn + ", viewerTyCd=" + viewerTyCd
				+ ", viewerPtzYn=" + viewerPtzYn + ", lon=" + lon + ", lat=" + lat + ", radius=" + radius + ", columnName=" + columnName + ", nullColumn=" + nullColumn + ", selectFcltUsed=" + selectFcltUsed + ", chkFcltUseTy=" + Arrays.toString(chkFcltUseTy) + ", titleNm=" + titleNm + ", titleNmChk=" + titleNmChk + ", vmsLinkYn=" + vmsLinkYn + ", rtspUrl=" + rtspUrl + ", srlNo=" + srlNo + ", allCnt=" + allCnt + ", regCnt=" + regCnt + ", updCnt=" + updCnt + ", delCnt=" + delCnt + ", userNm=" + userNm + ", uplCol=" + uplCol + "]";
	}
}
