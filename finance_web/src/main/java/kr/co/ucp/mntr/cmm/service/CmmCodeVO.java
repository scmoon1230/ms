/**
* ----------------------------------------------------------------------------------------------
* @Class Name : CmmCodeVo.java
* @Description :
* @Version : 1.0
* Copyright (c) 2014 by KR.CO.UCP.CNU All rights reserved.
* @Modification Information
* ----------------------------------------------------------------------------------------------
* DATE AUTHOR DESCRIPTION
* ----------------------------------------------------------------------------------------------
* 2014. 11. 7. is 최초작성
*
* ----------------------------------------------------------------------------------------------
*/
package kr.co.ucp.mntr.cmm.service;

import java.io.Serializable;

public class CmmCodeVO implements Serializable {

	private static final long serialVersionUID = 8898840135862832176L;
	private String gCodeId;
	private String codeId;
	private String codeNm;

	public CmmCodeVO() {

	}

	public CmmCodeVO(String gCodeId) {
		this.gCodeId = gCodeId;
	}

	public CmmCodeVO(String gCodeId, String codeId) {
		this.gCodeId = gCodeId;
		this.codeId = codeId;
	}

	public String getgCodeId() {
		return gCodeId;
	}

	public void setgCodeId(String gCodeId) {
		this.gCodeId = gCodeId;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getCodeNm() {
		return codeNm;
	}

	public void setCodeNm(String codeNm) {
		this.codeNm = codeNm;
	}
}
