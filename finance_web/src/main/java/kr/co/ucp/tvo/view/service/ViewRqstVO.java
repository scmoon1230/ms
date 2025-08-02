package kr.co.ucp.tvo.view.service;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import kr.co.ucp.tvo.view.service.ViewSrchVO;

public class ViewRqstVO extends ViewSrchVO {

	private static final long serialVersionUID = 4815964174976460413L;

	@Length(min = 2, max = 40, message = "사건번호는 최소 2자 최대 40자 까지 입력 가능합니다.")
	private String evtNo;
	@Length(min = 2, max = 40, message = "사건명은 최소 2자 최대 40자 까지 입력 가능합니다.")
	private String evtNm;
	@Length(min = 2, max = 80, message = "사건등급은 최소 2자 최대 80자 까지 입력 가능합니다.")
	private String evtGrd;
	@Length(min = 2, max = 80, message = "사건유형은 최소 2자 최대 80자 까지 입력 가능합니다.")
	private String evtTy;
	@Length(min = 14, max = 14, message = "잘못된 날자 형식입니다.")
	private String evtYmdhms;
	@Range(min = 125, max = 132, message = "경도 범위는 125.0 ~ 132.0 까지 입니다.")
	private String evtPointX;
	@Range(min = 32, max = 39, message = "위도 범위는 32.0 ~ 39.0 까지 입니다.")
	private String evtPointY;
	@Length(min = 2, max = 2, message = "잘못된 신청사유 코드입니다.")
	private String rqstRsnTyCd;
	@Length(min = 1, max = 1, message = "잘못된 긴급구분 선택입니다.")
	private String emrgYn;
	@Length(min = 14, max = 14, message = "잘못된 날자 형식입니다.")
	private String viewEndYmdhmsWant;

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
	public String getEmrgYn() {
		return emrgYn;
	}
	public void setEmrgYn(String emrgYn) {
		this.emrgYn = emrgYn;
	}
	public String getViewEndYmdhmsWant() {
		return viewEndYmdhmsWant;
	}
	public void setViewEndYmdhmsWant(String viewEndYmdhmsWant) {
		this.viewEndYmdhmsWant = viewEndYmdhmsWant;
	}
}
