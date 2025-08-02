package kr.co.ucp.mntr.fclt.service;

import kr.co.ucp.mntr.cmm.service.CommonVO;

public class InfrCommonVO  extends CommonVO {
	private String regDate;
	private String regUserId;
	private String useYn;

    private String searchCondition = "";
    private String searchKeyword = "";

    private String searchTermStart = "";
    private String searchTermEnd = "";

    private String searchFcltGubun = "";
    private String searchStatusNm = "";
    private String searchStatusGubun = "";

    private String searchFcltId = "";

    private String searchBookMark = "";

    private String searchYear = "";
    private String searchMonth = "";
    private String searchUserType = "TO";
    private String searchEventType = "";

    private String searchDate = "";
    private String searchTime = "";

	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getRegUserId() {
		return regUserId;
	}
	public void setRegUserId(String regUserId) {
		this.regUserId = regUserId;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
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
	public String getSearchStatusNm() {
		return searchStatusNm;
	}
	public void setSearchStatusNm(String searchStatusNm) {
		this.searchStatusNm = searchStatusNm;
	}
	public String getSearchStatusGubun() {
		return searchStatusGubun;
	}
	public void setSearchStatusGubun(String searchStatusGubun) {
		this.searchStatusGubun = searchStatusGubun;
	}
	public String getSearchFcltId() {
		return searchFcltId;
	}
	public void setSearchFcltId(String searchFcltId) {
		this.searchFcltId = searchFcltId;
	}
	public String getSearchBookMark() {
		return searchBookMark;
	}
	public void setSearchBookMark(String searchBookMark) {
		this.searchBookMark = searchBookMark;
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
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}
	public String getSearchTime() {
		return searchTime;
	}
	public void setSearchTime(String searchTime) {
		this.searchTime = searchTime;
	}


}
