package kr.co.ucp.cmm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Class Name : LoginVO.java
 * @Description : Login VO class
 * @Modification Information
 * @ @ 수정일 수정자 수정내용 @ ------- -------- --------------------------- @ 2009.03.03
 *   박지욱 최초 생성
 *
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.03
 * @version 1.0
 * @see
 *
 */
public class LoginVO implements Serializable {

	private static final long serialVersionUID = -8274004534207618049L;

	private String rowPerPage;
	
	private String userId;
	private String userPwd;
	private String userName;
	private String positionCode;
	private String positionName;
	private String userGb;
	private String useYn;
	private String acctGb;
	private String acctGbName;
	private String telNo;

	private String dbName;
	private String progId;
	
	
	private String ssl;
	private String dstrtCd;
	private String userNmKo;
	private String userNmEn;
	private String useTyCd;
	private String moblNo;
	private String email;
	private String ipAdres;
	private String offcTelNo;
	private String rankNm;
	private String deptNm;
	private String insttNm;
	//private String chrgWork;
	private String remark;
	private String rgsUserId;
	private String rgsDate;
	private String updUserId;
	private String updDate;
	private String ipTyCd;
	private String ipCd;
	
	private String grpId;
	private String grpNmKo;
	private String authLvl;
	private String authLvlNm;
//	private String authType;
	
	private String sysId;
    private String gSysId;
	private String sysCd;
	private String menuSysNm;
	private String uniqId;
	private String grpTy;
	
	private String mainPage = "";

	private Map<String, Object> leftMenuMap;
    private Map<String, Object> titleMenuMap;
	private List<Map<String, String>> menuList;
	private List<Map<String, String>> topMenuList;
	
    private String loginDplctnYn = "X";
    private String userApproveTy;
    private String approveUserId;
    private String userApproveDt;
    private String pwChgDtSys;
    private String pwChgDtUser;
    private String pwChgRequiredYn;
    private String latestLoginDt;

	public String getRowPerPage() {
        if (rowPerPage != null) {
            return rowPerPage;
        } else {
            return "500";	// 기본값
        }
	}
	public void setRowPerPage(String rowPerPage) {
		this.rowPerPage = rowPerPage;
	}
	public String getUserId() {
		if (userId == null)
			return "";
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPositionCode() {
		return positionCode;
	}
	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getUserGb() {
		return userGb;
	}
	public void setUserGb(String userGb) {
		this.userGb = userGb;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getAcctGb() {
		return acctGb;
	}
	public void setAcctGb(String acctGb) {
		this.acctGb = acctGb;
	}
	public String getAcctGbName() {
		return acctGbName;
	}
	public void setAcctGbName(String acctGbName) {
		this.acctGbName = acctGbName;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getProgId() {
		return progId;
	}
	public void setProgId(String progId) {
		this.progId = progId;
	}
	public String getSsl() {
		return ssl;
	}
	public void setSsl(String ssl) {
		this.ssl = ssl;
	}
	public String getDstrtCd() {
		return dstrtCd;
	}
	public void setDstrtCd(String dstrtCd) {
		this.dstrtCd = dstrtCd;
	}
	public String getUserNmKo() {
		return userNmKo;
	}
	public void setUserNmKo(String userNmKo) {
		this.userNmKo = userNmKo;
	}
	public String getUserNmEn() {
		return userNmEn;
	}
	public void setUserNmEn(String userNmEn) {
		this.userNmEn = userNmEn;
	}
	public String getUseTyCd() {
		return useTyCd;
	}
	public void setUseTyCd(String useTyCd) {
		this.useTyCd = useTyCd;
	}
	public String getMoblNo() {
		return moblNo;
	}
	public void setMoblNo(String moblNo) {
		this.moblNo = moblNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIpAdres() {
		return ipAdres;
	}
	public void setIpAdres(String ipAdres) {
		this.ipAdres = ipAdres;
	}
	public String getOffcTelNo() {
		return offcTelNo;
	}
	public void setOffcTelNo(String offcTelNo) {
		this.offcTelNo = offcTelNo;
	}
	public String getRankNm() {
		return rankNm;
	}
	public void setRankNm(String rankNm) {
		this.rankNm = rankNm;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getInsttNm() {
		return insttNm;
	}
	public void setInsttNm(String insttNm) {
		this.insttNm = insttNm;
	}
//	public String getChrgWork() {
//		return chrgWork;
//	}
//	public void setChrgWork(String chrgWork) {
//		this.chrgWork = chrgWork;
//	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getIpTyCd() {
		return ipTyCd;
	}
	public void setIpTyCd(String ipTyCd) {
		this.ipTyCd = ipTyCd;
	}
	public String getIpCd() {
		return ipCd;
	}
	public void setIpCd(String ipCd) {
		this.ipCd = ipCd;
	}
	public String getGrpId() {
		return grpId;
	}
	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}
	public String getGrpNmKo() {
		return grpNmKo;
	}
	public void setGrpNmKo(String grpNmKo) {
		this.grpNmKo = grpNmKo;
	}
	public String getAuthLvl() {
		return authLvl;
	}
	public void setAuthLvl(String authLvl) {
		this.authLvl = authLvl;
	}
	public String getAuthLvlNm() {
		return authLvlNm;
	}
	public void setAuthLvlNm(String authLvlNm) {
		this.authLvlNm = authLvlNm;
	}
//	public String getAuthType() {
//		return authType;
//	}
//	public void setAuthType(String authType) {
//		this.authType = authType;
//	}
	
	public List<Map<String, String>> getTopMenuList() {
		return topMenuList;
	}
	public void setTopMenuList(List<Map<String, String>> topMenuList) {
		this.topMenuList = topMenuList;
	}
	public Map<String, Object> getLeftMenuMap() {
		return leftMenuMap;
	}
	public void setLeftMenuMap(Map<String, Object> leftMenuList) {
		this.leftMenuMap = leftMenuList;
	}
	public Map<String, Object> getTitleMenuMap() {
		return titleMenuMap;
	}
	public void setTitleMenuMap(Map<String, Object> titleMenuMap) {
		this.titleMenuMap = titleMenuMap;
	}
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	public String getgSysId() {
		return gSysId;
	}
	public void setgSysId(String gSysId) {
		this.gSysId = gSysId;
	}
	public String getMenuSysNm() {
		return menuSysNm;
	}
	public void setMenuSysNm(String menuSysNm) {
		this.menuSysNm = menuSysNm;
	}
	public String getSysCd() {
		return sysCd;
	}
	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}
	public List<Map<String, String>> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<Map<String, String>> menuList) {
		this.menuList = menuList;
	}
	public String getUniqId() {
		return uniqId;
	}
	public void setUniqId(String uniqId) {
		this.uniqId = uniqId;
	}
	public String getMainPage() {
		return mainPage;
	}
	public void setMainPage(String mainPage) {
		this.mainPage = mainPage;
	}
	public String getGrpTy() {
		return grpTy;
	}
	public void setGrpTy(String grpTy) {
		this.grpTy = grpTy;
	}
	public String getLoginDplctnYn() {
		return loginDplctnYn;
	}
	public void setLoginDplctnYn(String loginDplctnYn) {
		this.loginDplctnYn = loginDplctnYn;
	}
	public String getUserApproveTy() {
		return userApproveTy;
	}
	public void setUserApproveTy(String userApproveTy) {
		this.userApproveTy = userApproveTy;
	}
	public String getApproveUserId() {
		return approveUserId;
	}
	public void setApproveUserId(String approveUserId) {
		this.approveUserId = approveUserId;
	}
	public String getUserApproveDt() {
		return userApproveDt;
	}
	public void setUserApproveDt(String userApproveDt) {
		this.userApproveDt = userApproveDt;
	}
	public String getPwChgDtSys() {
		return pwChgDtSys;
	}
	public void setPwChgDtSys(String pwChgDtSys) {
		this.pwChgDtSys = pwChgDtSys;
	}
	public String getPwChgDtUser() {
		return pwChgDtUser;
	}
	public void setPwChgDtUser(String pwChgDtUser) {
		this.pwChgDtUser = pwChgDtUser;
	}
	public String getPwChgRequiredYn() {
		return pwChgRequiredYn;
	}
	public void setPwChgRequiredYn(String pwChgRequiredYn) {
		this.pwChgRequiredYn = pwChgRequiredYn;
	}
	public String getLatestLoginDt() {
		return latestLoginDt;
	}
	public void setLatestLoginDt(String latestLoginDt) {
		this.latestLoginDt = latestLoginDt;
	}
}
