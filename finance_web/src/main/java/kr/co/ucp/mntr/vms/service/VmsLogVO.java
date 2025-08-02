/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : VmsLogVO.java
 * @Description : 
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 11. 25. SaintJuny@ubolt.co.kr 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.vms.service;

import kr.co.ucp.mntr.cmm.service.CommonVO;

public class VmsLogVO extends CommonVO {
	private long seqNo;
	private String evtOcrNo;
	private String fcltId;
	private String userId;
	private String connDate;
	private String endDate;
	private String connTime;
	private String ptzDate;
	private String ptzCmd;
	private String viewRqstNo;
	private String videoSeaYmdHmsFr;
	private String videoSeaYmdHmsTo;
	
	public long getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(long seqNo) {
		this.seqNo = seqNo;
	}
	public String getEvtOcrNo() {
		return evtOcrNo;
	}
	public void setEvtOcrNo(String evtOcrNo) {
		this.evtOcrNo = evtOcrNo;
	}
	public String getFcltId() {
		return fcltId;
	}
	public void setFcltId(String fcltId) {
		this.fcltId = fcltId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getConnDate() {
		return connDate;
	}
	public void setConnDate(String connDate) {
		this.connDate = connDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getConnTime() {
		return connTime;
	}
	public void setConnTime(String connTime) {
		this.connTime = connTime;
	}
	public String getPtzDate() {
		return ptzDate;
	}
	public void setPtzDate(String ptzDate) {
		this.ptzDate = ptzDate;
	}
	public String getPtzCmd() {
		return ptzCmd;
	}
	public void setPtzCmd(String ptzCmd) {
		this.ptzCmd = ptzCmd;
	}
	public String getViewRqstNo() {
		return viewRqstNo;
	}
	public void setViewRqstNo(String viewRqstNo) {
		this.viewRqstNo = viewRqstNo;
	}
	public String getVideoSeaYmdHmsFr() {
		return videoSeaYmdHmsFr;
	}
	public void setVideoSeaYmdHmsFr(String videoSeaYmdHmsFr) {
		this.videoSeaYmdHmsFr = videoSeaYmdHmsFr;
	}
	public String getVideoSeaYmdHmsTo() {
		return videoSeaYmdHmsTo;
	}
	public void setVideoSeaYmdHmsTo(String videoSeaYmdHmsTo) {
		this.videoSeaYmdHmsTo = videoSeaYmdHmsTo;
	}
	@Override
	public String toString() {
		return "VmsLogVO [seqNo=" + seqNo + ", evtOcrNo=" + evtOcrNo + ", fcltId=" + fcltId + ", userId=" + userId + ", connDate=" + connDate + ", endDate=" + endDate + ", connTime=" + connTime + ", ptzDate=" + ptzDate + ", ptzCmd=" + ptzCmd + ", viewRqstNo=" + viewRqstNo
				+ ", videoSeaYmdHmsFr=" + videoSeaYmdHmsFr + ", videoSeaYmdHmsTo=" + videoSeaYmdHmsTo + "]";
	}
}
