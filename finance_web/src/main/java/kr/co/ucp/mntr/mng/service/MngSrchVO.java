package kr.co.ucp.mntr.mng.service;

import kr.co.ucp.mntr.cmm.service.FcltVO;

public class MngSrchVO extends FcltVO {

	private static final long serialVersionUID = 7443550487264868750L;
	private String searchFcltUsedTyCd;
	private String searchFcltGdsdtTy;
	private String searchFcltKndDtlCd;
	private String searchPresetYn;
	private String searchKeyword;
	private String searchGbn;
	private int searchPage;

	public String getSearchFcltUsedTyCd() {
		return searchFcltUsedTyCd;
	}
	public void setSearchFcltUsedTyCd(String searchFcltUsedTyCd) {
		this.searchFcltUsedTyCd = searchFcltUsedTyCd;
	}
	public String getSearchFcltGdsdtTy() {
		return searchFcltGdsdtTy;
	}
	public void setSearchFcltGdsdtTy(String searchFcltGdsdtTy) {
		this.searchFcltGdsdtTy = searchFcltGdsdtTy;
	}
	public String getSearchFcltKndDtlCd() {
		return searchFcltKndDtlCd;
	}
	public void setSearchFcltKndDtlCd(String searchFcltKndDtlCd) {
		this.searchFcltKndDtlCd = searchFcltKndDtlCd;
	}
	public String getSearchPresetYn() {
		return searchPresetYn;
	}
	public void setSearchPresetYn(String searchPresetYn) {
		this.searchPresetYn = searchPresetYn;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public String getSearchGbn() {
		return searchGbn;
	}
	public void setSearchGbn(String searchGbn) {
		this.searchGbn = searchGbn;
	}
	public int getSearchPage() { return searchPage; }
	public void setSearchPage(int searchPage) { this.searchPage = searchPage;}

	@Override
	public String toString() {
		return "MngSrchVO{" + "searchFcltUsedTyCd='" + searchFcltUsedTyCd + '\'' + ", searchFcltGdsdtTy='" + searchFcltGdsdtTy + '\'' + ", searchFcltKndDtlCd='" + searchFcltKndDtlCd + '\'' + ", searchPresetYn='" + searchPresetYn + '\'' + ", searchKeyword='" + searchKeyword + '\'' + ", searchGbn='" + searchGbn + '\'' + ", searchPage=" + searchPage + '}';
	}
}
