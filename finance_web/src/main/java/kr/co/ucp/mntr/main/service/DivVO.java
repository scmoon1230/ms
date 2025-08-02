package kr.co.ucp.mntr.main.service;

import kr.co.ucp.mntr.cmm.service.CommonVO;

public class DivVO extends CommonVO {
	private static final long serialVersionUID = 5238672565582351182L;
	private String divId;
	private String mntrTyId;
	private String mntrTyNm;
	private String divTitle;
	private String divFileNm;
	private String divTyCd;
	private String updUserId;
	private String updDate;
	private String rgsUserId;
	private String rgsDate;
	private String evtId;
	private String sysCd;
	private String usvcGrpCd;
	private String divLc;
	private String divLcSrlNo;
	private String divConts;

	public DivVO() {

	}

	public DivVO(String divId, String divTitle, String divFileNm, String divTyCd) {
		this.divId = divId;
		this.divTitle = divTitle;
		this.divFileNm = divFileNm;
		this.divTyCd = divTyCd;
	}

	public DivVO(String sysCd) {
		this.sysCd = sysCd;
	}

	public String getDivId() {
		return divId;
	}

	public void setDivId(String divId) {
		this.divId = divId;
	}

	public String getMntrTyId() {
		return mntrTyId;
	}

	public void setMntrTyId(String mntrTyId) {
		this.mntrTyId = mntrTyId;
	}

	public String getMntrTyNm() {
		return mntrTyNm;
	}

	public void setMntrTyNm(String mntrTyNm) {
		this.mntrTyNm = mntrTyNm;
	}

	public String getDivTitle() {
		return divTitle;
	}

	public void setDivTitle(String divTitle) {
		this.divTitle = divTitle;
	}

	public String getDivFileNm() {
		return divFileNm;
	}

	public void setDivFileNm(String divFileNm) {
		this.divFileNm = divFileNm;
	}

	public String getDivTyCd() {
		return divTyCd;
	}

	public void setDivTyCd(String divTyCd) {
		this.divTyCd = divTyCd;
	}

	public String getUpdUserId() {
		return updUserId;
	}

	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}

	public String getUpdDate() {
		return updDate;
	}

	public void setUpdDate(String updDate) {
		this.updDate = updDate;
	}

	public String getRgsUserId() {
		return rgsUserId;
	}

	public void setRgsUserId(String rgsUserId) {
		this.rgsUserId = rgsUserId;
	}

	public String getRgsDate() {
		return rgsDate;
	}

	public void setRgsDate(String rgsDate) {
		this.rgsDate = rgsDate;
	}

	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}

	public String getSysCd() {
		return sysCd;
	}

	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}

	public String getUsvcGrpCd() {
		return usvcGrpCd;
	}

	public void setUsvcGrpCd(String usvcGrpCd) {
		this.usvcGrpCd = usvcGrpCd;
	}

	public String getDivLc() {
		return divLc;
	}

	public void setDivLc(String divLc) {
		this.divLc = divLc;
	}

	public String getDivLcSrlNo() {
		return divLcSrlNo;
	}

	public void setDivLcSrlNo(String divLcSrlNo) {
		this.divLcSrlNo = divLcSrlNo;
	}

	public String getDivConts() {
		return divConts;
	}

	public void setDivConts(String divConts) {
		this.divConts = divConts;
	}

	@Override
	public String toString() {
		return "DivVO [divId=" + divId + ", mntrTyId=" + mntrTyId + ", mntrTyNm=" + mntrTyNm + ", divTitle=" + divTitle + ", divFileNm=" + divFileNm + ", divTyCd=" + divTyCd + ", updUserId=" + updUserId + ", updDate=" + updDate + ", rgsUserId=" + rgsUserId + ", rgsDate="
				+ rgsDate + ", evtId=" + evtId + ", sysCd=" + sysCd + ", usvcGrpCd=" + usvcGrpCd + ", divLc=" + divLc + ", divLcSrlNo=" + divLcSrlNo + ", divConts=" + divConts + "]";
	}
}
