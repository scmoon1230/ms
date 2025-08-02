package kr.co.ucp.tvo.out.service;

import kr.co.ucp.mntr.cmm.service.CommonVO;

public class OutSrchVO extends CommonVO {
	
	private static final long serialVersionUID = 1879312116155327189L;

	private String tvoPrgrsYmdhms;
	private String tvoPrgrsCd;
	private String tvoPrgrsDtl;

	private String viewRqstNo;
	private String cctvId;
	private String vdoYmdhmsFr;
	private String vdoYmdhmsTo;
	private String evtNo;
	private String evtNm;
	private String evtGrd;
	private String evtTy;
	private String evtYmdhms;
	private String evtAddr;
	private String evtPointX;
	private String evtPointY;
	private String rqstRsnTyCd;
	private String rqstRsnDtl;
	private String emrgYn;
	private String paperNo;
	private String paperNm;
	private String paperFileNm;
	
	private String viewRqstYmdhms;
	private String viewRqstYmdhmsFr;
	private String viewRqstYmdhmsTo;
	private String viewEndYmdhms;
	private String viewEndYmdhmsWant;
	private String viewRqstUserId;
	private String viewAprvUserId;
	private String viewExtnRqstUserId;
	private String rqstSysCd;
	private String rqstTyCd;
//	private String useRsltRgstYn;

	private String outRqstNo;
	private String outRqstYmdhms;
	private String outRqstYmdhmsFr;
	private String outRqstYmdhmsTo;
	private String outRqstUserId;
	private String outExtnRqstUserId;
	private String playEndYmdhms;
	private String fcltLblNm;
	private String maskingEndYn;

	private String outFilePrgrsCd;

	private String viewExtnRqstYmdhms;
	private String viewExtnRqstRsn;
	private String outExtnRqstYmdhms;
	private String outExtnRqstRsn;

	private String expireCompFlag;
	private String expireYmdhms;
	private String outFileDelYn;
	
	private String prgrs;
	private String saltText;
	

	public String getTvoPrgrsYmdhms() {
		return tvoPrgrsYmdhms;
	}
	public void setTvoPrgrsYmdhms(String tvoPrgrsYmdhms) {
		this.tvoPrgrsYmdhms = tvoPrgrsYmdhms;
	}
	public String getTvoPrgrsCd() {
		return tvoPrgrsCd;
	}
	public void setTvoPrgrsCd(String tvoPrgrsCd) {
		this.tvoPrgrsCd = tvoPrgrsCd;
	}
	public String getTvoPrgrsDtl() {
		return tvoPrgrsDtl;
	}
	public void setTvoPrgrsDtl(String tvoPrgrsDtl) {
		this.tvoPrgrsDtl = tvoPrgrsDtl;
	}
	public String getViewRqstNo() {
		return viewRqstNo;
	}
	public void setViewRqstNo(String viewRqstNo) {
		this.viewRqstNo = viewRqstNo;
	}
	public String getCctvId() {
		return cctvId;
	}
	public void setCctvId(String cctvId) {
		this.cctvId = cctvId;
	}
	public String getVdoYmdhmsFr() {
		return vdoYmdhmsFr;
	}
	public void setVdoYmdhmsFr(String vdoYmdhmsFr) {
		this.vdoYmdhmsFr = vdoYmdhmsFr;
	}
	public String getVdoYmdhmsTo() {
		return vdoYmdhmsTo;
	}
	public void setVdoYmdhmsTo(String vdoYmdhmsTo) {
		this.vdoYmdhmsTo = vdoYmdhmsTo;
	}
	public String getEvtNo() {
		return evtNo;
	}
	public void setEvtNo(String evtNo) {
		this.evtNo = evtNo;
	}
	public String getEvtNm() {
		return evtNm;
	}
	public void setEvtNm(String evtNm) {
		this.evtNm = evtNm;
	}
	public String getEvtGrd() {
		return evtGrd;
	}
	public void setEvtGrd(String evtGrd) {
		this.evtGrd = evtGrd;
	}
	public String getEvtTy() {
		return evtTy;
	}
	public void setEvtTy(String evtTy) {
		this.evtTy = evtTy;
	}
	public String getEvtYmdhms() {
		return evtYmdhms;
	}
	public void setEvtYmdhms(String evtYmdhms) {
		this.evtYmdhms = evtYmdhms;
	}
	public String getEvtAddr() {
		return evtAddr;
	}
	public void setEvtAddr(String evtAddr) {
		this.evtAddr = evtAddr;
	}
	public String getEvtPointX() {
		return evtPointX;
	}
	public void setEvtPointX(String evtPointX) {
		this.evtPointX = evtPointX;
	}
	public String getEvtPointY() {
		return evtPointY;
	}
	public void setEvtPointY(String evtPointY) {
		this.evtPointY = evtPointY;
	}
	public String getRqstRsnTyCd() {
		return rqstRsnTyCd;
	}
	public void setRqstRsnTyCd(String rqstRsnTyCd) {
		this.rqstRsnTyCd = rqstRsnTyCd;
	}
	public String getRqstRsnDtl() {
		return rqstRsnDtl;
	}
	public void setRqstRsnDtl(String rqstRsnDtl) {
		this.rqstRsnDtl = rqstRsnDtl;
	}
	public String getEmrgYn() {
		return emrgYn;
	}
	public void setEmrgYn(String emrgYn) {
		this.emrgYn = emrgYn;
	}
	public String getPaperNo() {
		return paperNo;
	}
	public void setPaperNo(String paperNo) {
		this.paperNo = paperNo;
	}
	public String getPaperNm() {
		return paperNm;
	}
	public void setPaperNm(String paperNm) {
		this.paperNm = paperNm;
	}
	public String getPaperFileNm() {
		return paperFileNm;
	}
	public void setPaperFileNm(String paperFileNm) {
		this.paperFileNm = paperFileNm;
	}
	public String getViewRqstYmdhms() {
		return viewRqstYmdhms;
	}
	public void setViewRqstYmdhms(String viewRqstYmdhms) {
		this.viewRqstYmdhms = viewRqstYmdhms;
	}
	public String getViewRqstYmdhmsFr() {
		return viewRqstYmdhmsFr;
	}
	public void setViewRqstYmdhmsFr(String viewRqstYmdhmsFr) {
		this.viewRqstYmdhmsFr = viewRqstYmdhmsFr;
	}
	public String getViewRqstYmdhmsTo() {
		return viewRqstYmdhmsTo;
	}
	public void setViewRqstYmdhmsTo(String viewRqstYmdhmsTo) {
		this.viewRqstYmdhmsTo = viewRqstYmdhmsTo;
	}
	public String getViewEndYmdhms() {
		return viewEndYmdhms;
	}
	public void setViewEndYmdhms(String viewEndYmdhms) {
		this.viewEndYmdhms = viewEndYmdhms;
	}
	public String getViewEndYmdhmsWant() {
		return viewEndYmdhmsWant;
	}
	public void setViewEndYmdhmsWant(String viewEndYmdhmsWant) {
		this.viewEndYmdhmsWant = viewEndYmdhmsWant;
	}
	public String getViewRqstUserId() {
		return viewRqstUserId;
	}
	public void setViewRqstUserId(String viewRqstUserId) {
		this.viewRqstUserId = viewRqstUserId;
	}
	public String getViewAprvUserId() {
		return viewAprvUserId;
	}
	public void setViewAprvUserId(String viewAprvUserId) {
		this.viewAprvUserId = viewAprvUserId;
	}
	public String getViewExtnRqstUserId() {
		return viewExtnRqstUserId;
	}
	public void setViewExtnRqstUserId(String viewExtnRqstUserId) {
		this.viewExtnRqstUserId = viewExtnRqstUserId;
	}
	public String getRqstSysCd() {
		return rqstSysCd;
	}
	public void setRqstSysCd(String rqstSysCd) {
		this.rqstSysCd = rqstSysCd;
	}
	public String getRqstTyCd() {
		return rqstTyCd;
	}
	public void setRqstTyCd(String rqstTyCd) {
		this.rqstTyCd = rqstTyCd;
	}
//	public String getUseRsltRgstYn() {
//		return useRsltRgstYn;
//	}
//	public void setUseRsltRgstYn(String useRsltRgstYn) {
//		this.useRsltRgstYn = useRsltRgstYn;
//	}
	public String getOutRqstNo() {
		return outRqstNo;
	}
	public void setOutRqstNo(String outRqstNo) {
		this.outRqstNo = outRqstNo;
	}
	public String getOutRqstYmdhms() {
		return outRqstYmdhms;
	}
	public void setOutRqstYmdhms(String outRqstYmdhms) {
		this.outRqstYmdhms = outRqstYmdhms;
	}
	public String getOutRqstYmdhmsFr() {
		return outRqstYmdhmsFr;
	}
	public void setOutRqstYmdhmsFr(String outRqstYmdhmsFr) {
		this.outRqstYmdhmsFr = outRqstYmdhmsFr;
	}
	public String getOutRqstYmdhmsTo() {
		return outRqstYmdhmsTo;
	}
	public void setOutRqstYmdhmsTo(String outRqstYmdhmsTo) {
		this.outRqstYmdhmsTo = outRqstYmdhmsTo;
	}
	public String getOutRqstUserId() {
		return outRqstUserId;
	}
	public void setOutRqstUserId(String outRqstUserId) {
		this.outRqstUserId = outRqstUserId;
	}
	public String getOutExtnRqstUserId() {
		return outExtnRqstUserId;
	}
	public void setOutExtnRqstUserId(String outExtnRqstUserId) {
		this.outExtnRqstUserId = outExtnRqstUserId;
	}
	public String getPlayEndYmdhms() {
		return playEndYmdhms;
	}
	public void setPlayEndYmdhms(String playEndYmdhms) {
		this.playEndYmdhms = playEndYmdhms;
	}
	public String getFcltLblNm() {
		return fcltLblNm;
	}
	public void setFcltLblNm(String fcltLblNm) {
		this.fcltLblNm = fcltLblNm;
	}
	public String getMaskingEndYn() {
		return maskingEndYn;
	}
	public void setMaskingEndYn(String maskingEndYn) {
		this.maskingEndYn = maskingEndYn;
	}
	public String getOutFilePrgrsCd() {
		return outFilePrgrsCd;
	}
	public void setOutFilePrgrsCd(String outFilePrgrsCd) {
		this.outFilePrgrsCd = outFilePrgrsCd;
	}
	public String getViewExtnRqstYmdhms() {
		return viewExtnRqstYmdhms;
	}
	public void setViewExtnRqstYmdhms(String viewExtnRqstYmdhms) {
		this.viewExtnRqstYmdhms = viewExtnRqstYmdhms;
	}
	public String getViewExtnRqstRsn() {
		return viewExtnRqstRsn;
	}
	public void setViewExtnRqstRsn(String viewExtnRqstRsn) {
		this.viewExtnRqstRsn = viewExtnRqstRsn;
	}
	public String getOutExtnRqstYmdhms() {
		return outExtnRqstYmdhms;
	}
	public void setOutExtnRqstYmdhms(String outExtnRqstYmdhms) {
		this.outExtnRqstYmdhms = outExtnRqstYmdhms;
	}
	public String getOutExtnRqstRsn() {
		return outExtnRqstRsn;
	}
	public void setOutExtnRqstRsn(String outExtnRqstRsn) {
		this.outExtnRqstRsn = outExtnRqstRsn;
	}
	public String getExpireCompFlag() {
		return expireCompFlag;
	}
	public void setExpireCompFlag(String expireCompFlag) {
		this.expireCompFlag = expireCompFlag;
	}
	public String getExpireYmdhms() {
		return expireYmdhms;
	}
	public void setExpireYmdhms(String expireYmdhms) {
		this.expireYmdhms = expireYmdhms;
	}
	public String getOutFileDelYn() {
		return outFileDelYn;
	}
	public void setOutFileDelYn(String outFileDelYn) {
		this.outFileDelYn = outFileDelYn;
	}
	public String getPrgrs() {
		return prgrs;
	}
	public void setPrgrs(String prgrs) {
		this.prgrs = prgrs;
	}
	public String getSaltText() {
		return saltText;
	}
	public void setSaltText(String saltText) {
		this.saltText = saltText;
	}
}
