package kr.co.ucp.mntr.fclt.service;

public class FcltRegHisVO extends InfrCommonVO {

	private String srlNo;
	private int allCnt;
	private int regCnt;
	private int updCnt;
	private int delCnt;
	private String userNm;
	private String uplCol;
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

}
