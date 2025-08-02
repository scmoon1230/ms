/**
* ----------------------------------------------------------------------------------------------
* @Class Name : ApiServiceImpl.java
* @Description : Api와 관련된 요청을 처리하는 검색 VO
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

import java.util.ArrayList;

import kr.co.ucp.mntr.cmm.service.CommonVO;

public class ApiSrchVO extends CommonVO {
	private static final long serialVersionUID = -8327098032570848825L;
	private String pointX;
	private String pointY;

	private String keyword;

	private String roadNm, sidoNm, sigunguNm, lgEmdNm, lgLiNm;

	private String mainNo;
	private String subNo;

	private String fcltUsedTyCd;
	private String radius;

	private String lgDongCd;
	private String isMntn;
	private String jibunMainNo;
	private String jibunSubNo;

	private String pnu;

	private ArrayList<String> buldNm;

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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getRoadNm() {
		return roadNm;
	}

	public void setRoadNm(String roadNm) {
		this.roadNm = roadNm;
	}

	public String getSidoNm() {
		return sidoNm;
	}

	public void setSidoNm(String sidoNm) {
		this.sidoNm = sidoNm;
	}

	public String getSigunguNm() {
		return sigunguNm;
	}

	public void setSigunguNm(String sigunguNm) {
		this.sigunguNm = sigunguNm;
	}

	public String getLgEmdNm() {
		return lgEmdNm;
	}

	public void setLgEmdNm(String lgEmdNm) {
		this.lgEmdNm = lgEmdNm;
	}

	public String getLgLiNm() {
		return lgLiNm;
	}

	public void setLgLiNm(String lgLiNm) {
		this.lgLiNm = lgLiNm;
	}

	public String getMainNo() {
		return mainNo;
	}

	public void setMainNo(String mainNo) {
		this.mainNo = mainNo;
	}

	public String getSubNo() {
		return subNo;
	}

	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}

	public String getFcltUsedTyCd() {
		return fcltUsedTyCd;
	}

	public void setFcltUsedTyCd(String fcltUsedTyCd) {
		this.fcltUsedTyCd = fcltUsedTyCd;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getLgDongCd() {
		return lgDongCd;
	}

	public void setLgDongCd(String lgDongCd) {
		this.lgDongCd = lgDongCd;
	}

	public String getIsMntn() {
		return isMntn;
	}

	public void setIsMntn(String isMntn) {
		this.isMntn = isMntn;
	}

	public String getJibunMainNo() {
		return jibunMainNo;
	}

	public void setJibunMainNo(String jibunMainNo) {
		this.jibunMainNo = jibunMainNo;
	}

	public String getJibunSubNo() {
		return jibunSubNo;
	}

	public void setJibunSubNo(String jibunSubNo) {
		this.jibunSubNo = jibunSubNo;
	}

	public String getPnu() {
		return pnu;
	}

	public void setPnu(String pnu) {
		this.pnu = pnu;
	}

	public ArrayList<String> getBuldNm() {
		return buldNm;
	}

	public void setBuldNm(ArrayList<String> buldNm) {
		this.buldNm = buldNm;
	}

	@Override
	public String toString() {
		return "ApiSrchVO [pointX=" + pointX + ", pointY=" + pointY + ", keyword=" + keyword + ", roadNm=" + roadNm
				+ ", sidoNm=" + sidoNm + ", sigunguNm=" + sigunguNm + ", lgEmdNm=" + lgEmdNm + ", lgLiNm=" + lgLiNm
				+ ", mainNo=" + mainNo + ", subNo=" + subNo + ", fcltUsedTyCd=" + fcltUsedTyCd + ", radius=" + radius
				+ ", lgDongCd=" + lgDongCd + ", isMntn=" + isMntn + ", jibunMainNo=" + jibunMainNo + ", jibunSubNo="
				+ jibunSubNo + ", pnu=" + pnu + ", buldNm=" + buldNm + "]";
	}
}
