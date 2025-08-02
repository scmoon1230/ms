/**
* ----------------------------------------------------------------------------------------------
* @Class Name : ConfigureVO.java
* @Description :
* @Version : 1.0
* Copyright (c) 2014 by KR.CO.UCP.CNU All rights reserved.
* @Modification Information
* ----------------------------------------------------------------------------------------------
* DATE AUTHOR DESCRIPTION
* ----------------------------------------------------------------------------------------------
* 2014. 11. 6. ubolt 최초작성
*
* ----------------------------------------------------------------------------------------------
*/
package kr.co.ucp.mntr.cmm.service;

public class ConfigureVO extends CommonVO {
	private static final long serialVersionUID = 6025917681001253877L;
    // FOR SYSTEM
	//private String exeEnv;
	private String sysId;
    // FOR USER
	private String ucpId;
	private String ucpNm;
	private String dstrtCd;
	private String dstrtNm;
    // FOR EVENT
	private String divMoveSet;
	private String evtLcMoveYn;
	private String radsClmt;
	private String radsRoute;
	private String setTime;
	private String cctvViewRads;
    // FOR GIS
    private String gisEngine;
	private String gisIp;
	private String gisLevel;
	private String gisTy;
	private String gisLabelViewScale;
	private String gisFeatureViewScale;
	private String iconSize;
	private String iconTy;
	private String mapAltitud;
	private String mapDirect;
	private String mapTilt;
	private String pointX;
	private String pointY;
	private String pointZ;
	private String checkYn;
	private String layerId;
	private String etcYn;
    private String gisZoomIndex;
    private String gisZoomMin;
    private String gisZoomMax;
    private String uuUserId; //사용자항목 시스템(사용자)아이디
    private String ucUserId; //공통항목 시스템아이디
    // FOR PAGE
	private String headerTy;
	private String leftDivHdnYn;
	private String mntrTyId;
	private String mntrViewBottom;
	private String mntrViewLeft;
	private String mntrViewRight;
	private String popHeight;
	private String popRoute;
	private String popWidth;
    // FOR CCTV
	private String vmsIp;
	private String ptzCntrTy;
	private String cctvAccessYn;
	private String cctvSearchYn;
    private String snapShotYn;
	private String mtsEvtRgsYn;
	private String playTime;
	private String basePlaybacktime;
	private String basePlayfronttime;
    private String maxBfPlaybacktimeNow;
	private String maxBfPlaybacktime;
	private String maxAfPlaybacktime;
	private String updatedPlayTime;
	private String updatedBasePlaybacktime;
	private String updatedMaxBfPlaybacktime;
	private String updatedMaxAfPlaybacktime;
	private String maxPlaybacktime;
	private String cnOsvtOpt;
	private String autoEndTime;
	private String cnOsvtPlaytimeStopYn;
	private String fullScreenCloseYn;
    // ETC	
    private String fcltBaseItem;
	private String networkId;
	private String networkNm;
	private String networkTy;
	private String networkIp;
	private String networkMpIp;
    private String imageEss;
    private String imageEvent;
    private String imageSearch;
    private String imageLpr;
    private String confUserId;
	private String sysCd;
    private String webSocketSoundUseYn;
    
    private String userApproveYn;
    private String menuOrdrTy;

	public ConfigureVO() {

	}

	public ConfigureVO(String userId, String dstrtCd, String sysId) {
		super.setUserId(userId);
		this.setDstrtCd(dstrtCd);
		this.setSysId(sysId);
	}

	public ConfigureVO(String userId) {
		super.setUserId(userId);
	}
	public String getDivMoveSet() {
		return divMoveSet;
	}
	public void setDivMoveSet(String divMoveSet) {
		this.divMoveSet = divMoveSet;
	}
	public String getDstrtCd() {
		return dstrtCd;
	}
	public String getDstrtNm() {
		return dstrtNm;
	}
	public void setDstrtNm(String dstrtNm) {
		this.dstrtNm = dstrtNm;
	}
	public void setDstrtCd(String dstrtCd) {
		this.dstrtCd = dstrtCd;
	}
	public String getEvtLcMoveYn() {
		return evtLcMoveYn;
	}
	public void setEvtLcMoveYn(String evtLcMoveYn) {
		this.evtLcMoveYn = evtLcMoveYn;
	}
	public String getGisLevel() {
		return gisLevel;
	}
	public void setGisLevel(String gisLevel) {
		this.gisLevel = gisLevel;
	}
	public String getGisTy() {
		return gisTy;
	}
	public void setGisTy(String gisTy) {
		this.gisTy = gisTy;
	}
	public String getGisLabelViewScale() {
		return gisLabelViewScale;
	}
	public void setGisLabelViewScale(String gisLabelViewScale) {
		this.gisLabelViewScale = gisLabelViewScale;
	}
	public String getGisFeatureViewScale() {
		return gisFeatureViewScale;
	}
	public void setGisFeatureViewScale(String gisFeatureViewScale) {
		this.gisFeatureViewScale = gisFeatureViewScale;
	}
	public String getIconSize() {
		return iconSize;
	}
	public void setIconSize(String iconSize) {
		this.iconSize = iconSize;
	}
	public String getIconTy() {
		return iconTy;
	}
	public void setIconTy(String iconTy) {
		this.iconTy = iconTy;
	}
	public String getHeaderTy() {
		return headerTy;
	}
	public void setHeaderTy(String headerTy) {
		this.headerTy = headerTy;
	}
	public String getLeftDivHdnYn() {
		return leftDivHdnYn;
	}
	public void setLeftDivHdnYn(String leftDivHdnYn) {
		this.leftDivHdnYn = leftDivHdnYn;
	}
	public String getMapAltitud() {
		return mapAltitud;
	}
	public void setMapAltitud(String mapAltitud) {
		this.mapAltitud = mapAltitud;
	}
	public String getMapDirect() {
		return mapDirect;
	}
	public void setMapDirect(String mapDirect) {
		this.mapDirect = mapDirect;
	}
	public String getMapTilt() {
		return mapTilt;
	}
	public void setMapTilt(String mapTilt) {
		this.mapTilt = mapTilt;
	}
	public String getMntrTyId() {
		return mntrTyId;
	}
	public void setMntrTyId(String mntrTyId) {
		this.mntrTyId = mntrTyId;
	}
	public String getMntrViewBottom() {
		return mntrViewBottom;
	}
	public void setMntrViewBottom(String mntrViewBottom) {
		this.mntrViewBottom = mntrViewBottom;
	}
	public String getMntrViewLeft() {
		return mntrViewLeft;
	}
	public void setMntrViewLeft(String mntrViewLeft) {
		this.mntrViewLeft = mntrViewLeft;
	}
	public String getMntrViewRight() {
		return mntrViewRight;
	}
	public void setMntrViewRight(String mntrViewRight) {
		this.mntrViewRight = mntrViewRight;
	}
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
	public String getPointZ() {
		return pointZ;
	}
	public void setPointZ(String pointZ) {
		this.pointZ = pointZ;
	}
	public String getPopHeight() {
		return popHeight;
	}
	public void setPopHeight(String popHeight) {
		this.popHeight = popHeight;
	}
	public String getPopRoute() {
		return popRoute;
	}
	public void setPopRoute(String popRoute) {
		this.popRoute = popRoute;
	}
	public String getPopWidth() {
		return popWidth;
	}
	public void setPopWidth(String popWidth) {
		this.popWidth = popWidth;
	}
	public String getRadsClmt() {
		return radsClmt;
	}
	public void setRadsClmt(String radsClmt) {
		this.radsClmt = radsClmt;
	}
	public String getRadsRoute() {
		return radsRoute;
	}
	public void setRadsRoute(String radsRoute) {
		this.radsRoute = radsRoute;
	}
	public String getSetTime() {
		return setTime;
	}
	public void setSetTime(String setTime) {
		this.setTime = setTime;
	}
	public String getCctvViewRads() {
		return cctvViewRads;
	}
	public void setCctvViewRads(String cctvViewRads) {
		this.cctvViewRads = cctvViewRads;
	}
	public String getVmsIp() {
		return vmsIp;
	}
	public void setVmsIp(String vmsIp) {
		this.vmsIp = vmsIp;
	}

	public String getGisEngine() {
		return gisEngine;
	}

	public void setGisEngine(String gisEngine) {
		this.gisEngine = gisEngine;
	}

	public String getGisIp() {
		return gisIp;
	}

	public void setGisIp(String gisIp) {
		this.gisIp = gisIp;
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public String getNetworkNm() {
		return networkNm;
	}

	public void setNetworkNm(String networkNm) {
		this.networkNm = networkNm;
	}

	public String getNetworkTy() {
		return networkTy;
	}

	public void setNetworkTy(String networkTy) {
		this.networkTy = networkTy;
	}

	public String getNetworkIp() {
		return networkIp;
	}

	public void setNetworkIp(String networkIp) {
		this.networkIp = networkIp;
	}

	public String getNetworkMpIp() {
		return networkMpIp;
	}

	public void setNetworkMpIp(String networkMpIp) {
		this.networkMpIp = networkMpIp;
	}

	public String getImageEss() {
		return imageEss;
	}

	public void setImageEss(String imageEss) {
		this.imageEss = imageEss;
	}

	public String getImageEvent() {
		return imageEvent;
	}

	public void setImageEvent(String imageEvent) {
		this.imageEvent = imageEvent;
	}

	public String getImageSearch() {
		return imageSearch;
	}

	public void setImageSearch(String imageSearch) {
		this.imageSearch = imageSearch;
	}

	public String getImageLpr() {
		return imageLpr;
	}

	public void setImageLpr(String imageLpr) {
		this.imageLpr = imageLpr;
	}

	public String getConfUserId() {
		return confUserId;
	}

	public void setConfUserId(String confUserId) {
		this.confUserId = confUserId;
	}

	public String getUcpId() {
		return ucpId;
	}

	public void setUcpId(String ucpId) {
		this.ucpId = ucpId;
	}

	public String getUcpNm() {
		return ucpNm;
	}

	public void setUcpNm(String ucpNm) {
		this.ucpNm = ucpNm;
	}

	public String getSysCd() {
		return sysCd;
	}

	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}

//	public String getExeEnv() {
//		return exeEnv;
//	}
//
//	public void setExeEnv(String exeEnv) {
//		this.exeEnv = exeEnv;
//	}

	public String getPtzCntrTy() {
		return ptzCntrTy;
	}

	public void setPtzCntrTy(String ptzCntrTy) {
		this.ptzCntrTy = ptzCntrTy;
	}

	public String getCctvAccessYn() {
		return cctvAccessYn;
	}

	public void setCctvAccessYn(String cctvAccessYn) {
		this.cctvAccessYn = cctvAccessYn;
	}

	public String getCctvSearchYn() {
		return cctvSearchYn;
	}

	public void setCctvSearchYn(String cctvSearchYn) {
		this.cctvSearchYn = cctvSearchYn;
	}

	public String getSnapShotYn() {
		return snapShotYn;
	}

	public void setSnapShotYn(String snapShotYn) {
		this.snapShotYn = snapShotYn;
	}

	public String getMtsEvtRgsYn() {
		return mtsEvtRgsYn;
	}

	public void setMtsEvtRgsYn(String mtsEvtRgsYn) {
		this.mtsEvtRgsYn = mtsEvtRgsYn;
	}

	public String getPlayTime() {
		return playTime;
	}

	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}

	public String getBasePlaybacktime() {
		return basePlaybacktime;
	}

	public void setBasePlaybacktime(String basePlaybacktime) {
		this.basePlaybacktime = basePlaybacktime;
	}

	public String getBasePlayfronttime() {
		return basePlayfronttime;
	}

	public void setBasePlayfronttime(String basePlayfronttime) {
		this.basePlayfronttime = basePlayfronttime;
	}

	public String getMaxBfPlaybacktimeNow() {
		return maxBfPlaybacktimeNow;
	}

	public void setMaxBfPlaybacktimeNow(String maxBfPlaybacktimeNow) {
		this.maxBfPlaybacktimeNow = maxBfPlaybacktimeNow;
	}

	public String getMaxBfPlaybacktime() {
		return maxBfPlaybacktime;
	}
	public void setMaxBfPlaybacktime(String maxBfPlaybacktime) {
		this.maxBfPlaybacktime = maxBfPlaybacktime;
	}
	public String getMaxAfPlaybacktime() {
		return maxAfPlaybacktime;
	}
	public void setMaxAfPlaybacktime(String maxAfPlaybacktime) {
		this.maxAfPlaybacktime = maxAfPlaybacktime;
	}
	public String getUpdatedPlayTime() {
		return updatedPlayTime;
	}
	public void setUpdatedPlayTime(String updatedPlayTime) {
		this.updatedPlayTime = updatedPlayTime;
	}
	public String getUpdatedBasePlaybacktime() {
		return updatedBasePlaybacktime;
	}
	public void setUpdatedBasePlaybacktime(String updatedBasePlaybacktime) {
		this.updatedBasePlaybacktime = updatedBasePlaybacktime;
	}
	public String getUpdatedMaxBfPlaybacktime() {
		return updatedMaxBfPlaybacktime;
	}
	public void setUpdatedMaxBfPlaybacktime(String updatedMaxBfPlaybacktime) {
		this.updatedMaxBfPlaybacktime = updatedMaxBfPlaybacktime;
	}
	public String getUpdatedMaxAfPlaybacktime() {
		return updatedMaxAfPlaybacktime;
	}
	public void setUpdatedMaxAfPlaybacktime(String updatedMaxAfPlaybacktime) {
		this.updatedMaxAfPlaybacktime = updatedMaxAfPlaybacktime;
	}
	public String getMaxPlaybacktime() {
		return maxPlaybacktime;
	}
	public void setMaxPlaybacktime(String maxPlaybacktime) {
		this.maxPlaybacktime = maxPlaybacktime;
	}
	public String getCnOsvtOpt() {
		return cnOsvtOpt;
	}
	public void setCnOsvtOpt(String cnOsvtOpt) {
		this.cnOsvtOpt = cnOsvtOpt;
	}
	public String getAutoEndTime() {
		return autoEndTime;
	}
	public void setAutoEndTime(String autoEndTime) {
		this.autoEndTime = autoEndTime;
	}
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	public String getCheckYn() {
		return checkYn;
	}
	public void setCheckYn(String checkYn) {
		this.checkYn = checkYn;
	}
	public String getLayerId() {
		return layerId;
	}
	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}
	public String getEtcYn() {
		return etcYn;
	}
	public void setEtcYn(String etcYn) {
		this.etcYn = etcYn;
	}
	public String getGisZoomIndex() {
		return gisZoomIndex;
	}
	public void setGisZoomIndex(String gisZoomIndex) {
		this.gisZoomIndex = gisZoomIndex;
	}
	public String getGisZoomMin() {
		return gisZoomMin;
	}
	public void setGisZoomMin(String gisZoomMin) {
		this.gisZoomMin = gisZoomMin;
	}
	public String getGisZoomMax() {
		return gisZoomMax;
	}
	public void setGisZoomMax(String gisZoomMax) {
		this.gisZoomMax = gisZoomMax;
	}
	public String getUuUserId() {
		return uuUserId;
	}
	public void setUuUserId(String uuUserId) {
		this.uuUserId = uuUserId;
	}
	public String getUcUserId() {
		return ucUserId;
	}
	public void setUcUserId(String ucUserId) {
		this.ucUserId = ucUserId;
	}
	public String getFcltBaseItem() {
		return fcltBaseItem;
	}
	public void setFcltBaseItem(String fcltBaseItem) {
		this.fcltBaseItem = fcltBaseItem;
	}
	public String getCnOsvtPlaytimeStopYn() {
		return cnOsvtPlaytimeStopYn;
	}
	public void setCnOsvtPlaytimeStopYn(String cnOsvtPlaytimeStopYn) {
		this.cnOsvtPlaytimeStopYn = cnOsvtPlaytimeStopYn;
	}
	public String getFullScreenCloseYn() {
		return fullScreenCloseYn;
	}
	public void setFullScreenCloseYn(String fullScreenCloseYn) {
		this.fullScreenCloseYn = fullScreenCloseYn;
	}
	public String getWebSocketSoundUseYn() {
		return webSocketSoundUseYn;
	}
	public void setWebSocketSoundUseYn(String webSocketSoundUseYn) {
		this.webSocketSoundUseYn = webSocketSoundUseYn;
	}
	public String getUserApproveYn() {
		return userApproveYn;
	}
	public void setUserApproveYn(String userApproveYn) {
		this.userApproveYn = userApproveYn;
	}
	public String getMenuOrdrTy() {
		return menuOrdrTy;
	}
	public void setMenuOrdrTy(String menuOrdrTy) {
		this.menuOrdrTy = menuOrdrTy;
	}
	@Override
	public String toString() {
		return "ConfigureVO [sysId=" + sysId + ", ucpId=" + ucpId + ", ucpNm=" + ucpNm + ", dstrtCd=" + dstrtCd + ", dstrtNm=" + dstrtNm + ", divMoveSet=" + divMoveSet + ", evtLcMoveYn=" + evtLcMoveYn + ", radsClmt=" + radsClmt + ", radsRoute=" + radsRoute + ", setTime="
				+ setTime + ", cctvViewRads=" + cctvViewRads + ", gisEngine=" + gisEngine + ", gisIp=" + gisIp + ", gisLevel=" + gisLevel + ", gisTy=" + gisTy + ", gisLabelViewScale=" + gisLabelViewScale + ", gisFeatureViewScale=" + gisFeatureViewScale + ", iconSize=" + iconSize
				+ ", iconTy=" + iconTy + ", mapAltitud=" + mapAltitud + ", mapDirect=" + mapDirect + ", mapTilt=" + mapTilt + ", pointX=" + pointX + ", pointY=" + pointY + ", pointZ=" + pointZ + ", checkYn=" + checkYn + ", layerId=" + layerId + ", etcYn=" + etcYn
				+ ", gisZoomIndex=" + gisZoomIndex + ", gisZoomMin=" + gisZoomMin + ", gisZoomMax=" + gisZoomMax + ", uuUserId=" + uuUserId + ", ucUserId=" + ucUserId + ", headerTy=" + headerTy + ", leftDivHdnYn=" + leftDivHdnYn + ", mntrTyId=" + mntrTyId + ", mntrViewBottom="
				+ mntrViewBottom + ", mntrViewLeft=" + mntrViewLeft + ", mntrViewRight=" + mntrViewRight + ", popHeight=" + popHeight + ", popRoute=" + popRoute + ", popWidth=" + popWidth + ", vmsIp=" + vmsIp + ", ptzCntrTy=" + ptzCntrTy + ", cctvAccessYn=" + cctvAccessYn
				+ ", cctvSearchYn=" + cctvSearchYn + ", snapShotYn=" + snapShotYn + ", mtsEvtRgsYn=" + mtsEvtRgsYn + ", playTime=" + playTime + ", basePlaybacktime=" + basePlaybacktime + ", basePlayfronttime=" + basePlayfronttime + ", maxBfPlaybacktimeNow=" + maxBfPlaybacktimeNow
				+ ", maxBfPlaybacktime=" + maxBfPlaybacktime + ", maxAfPlaybacktime=" + maxAfPlaybacktime + ", updatedPlayTime=" + updatedPlayTime + ", updatedBasePlaybacktime=" + updatedBasePlaybacktime + ", updatedMaxBfPlaybacktime=" + updatedMaxBfPlaybacktime
				+ ", updatedMaxAfPlaybacktime=" + updatedMaxAfPlaybacktime + ", maxPlaybacktime=" + maxPlaybacktime + ", cnOsvtOpt=" + cnOsvtOpt + ", autoEndTime=" + autoEndTime + ", cnOsvtPlaytimeStopYn=" + cnOsvtPlaytimeStopYn + ", fullScreenCloseYn=" + fullScreenCloseYn
				+ ", fcltBaseItem=" + fcltBaseItem + ", networkId=" + networkId + ", networkNm=" + networkNm + ", networkTy=" + networkTy + ", networkIp=" + networkIp + ", networkMpIp=" + networkMpIp + ", imageEss=" + imageEss + ", imageEvent=" + imageEvent + ", imageSearch="
				+ imageSearch + ", imageLpr=" + imageLpr + ", confUserId=" + confUserId + ", sysCd=" + sysCd + ", webSocketSoundUseYn=" + webSocketSoundUseYn + ", userApproveYn=" + userApproveYn + ", menuOrdrTy=" + menuOrdrTy + "]";
	}
}
