package kr.co.ucp.geo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.ucp.cmns.BeanUtil;
import kr.co.ucp.cmns.CommUtil;
import kr.co.ucp.cmns.ConfigManager;
import kr.co.ucp.cmns.GeoUtils;
import kr.co.ucp.geo.service.GeoService;
import kr.co.ucp.pve.service.PveService;

public class GeoController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private GeoService geoService = (GeoService) BeanUtil.getBean("geoService");
	private PveService pveService = (PveService) BeanUtil.getBean("pveService");

	//private String updateTy = ConfigManager.getProperty("UPDATE_TY").trim();
	//private double pointDistance = CommUtil.objStrToDouble(ConfigManager.getProperty("POINT_DISTANCE").trim());
	private String updateTy = "3";
	private double pointDistance = 0.5;

	private String[] arrFcltUsedTyCd;
	private String[] arrFcltUsedTyNm;
	private int pveCctvCnt = 0;

	public GeoController() throws Exception {
		logger.info("### GeoController >>>> UPDATE_TY      : {}", updateTy);
		logger.info("### GeoController >>>> POINT_DISTANCE : {}", pointDistance);
		
	}
	
	public String geoJobStart() {
		String rtn = "success";
		logger.debug("==== geoJobStart >>>> ");

		int gisCctvCnt = 0;
		String gisFcltUid = "";
		String gisFcltLblNm = "";
		String gisFcltUsedTyNm = "";
		String gisFcltUsedTyCd = "";
		String gisPointX = "";
		String gisPointY = "";
		String gisFcltKndDtlCd = "";
		String gisLotnoAdresNm = "";
		String gisRoadAdresNm = "";
		String gisAngle = "0";
		
		String compareItemsGis = "";
		int upd_cnt1 = 0;
		int upd_cnt2 = 0;
		int upd_cnt31 = 0; //방향각 업데이트 대상건수
		int upd_cnt32 = 0; //방향각 업데이트 건수
		int err_cnt = 0;

		try {
			String strFcltUsedTyCd = "";	// CCTV용도 코드
			String strFcltUsedTyNm = "";	// CCTV용도 명
			
			HashMap<String, Object> paraMap = new HashMap<String, Object>();
			List<HashMap<String, Object>> fcltUsedTyList = pveService.selectFcltUsedTyInfo(paraMap);	// 용도정보를 가져온다.
			if ( 0 < fcltUsedTyList.size()) {
				for ( int i=0 ; i<fcltUsedTyList.size() ; i++ ) {
					strFcltUsedTyCd += ","+fcltUsedTyList.get(i).get("fcltUsedTyCd").toString();
					strFcltUsedTyNm += ","+fcltUsedTyList.get(i).get("fcltUsedTyNm").toString();
				}
				logger.info("### strFcltUsedTyCd:{}", strFcltUsedTyCd.substring(1));
				logger.info("### strFcltUsedTyNm:{}", strFcltUsedTyNm.substring(1));
				
				arrFcltUsedTyCd = strFcltUsedTyCd.substring(1).split(",");
				arrFcltUsedTyNm = strFcltUsedTyNm.substring(1).split(",");
				
			} else {
				logger.error("### 등록된 카메라 용도 정보가 없습니다!!! ###");
				return "fcltUsedTy Info data empty";
			}
			
			
			
			List<HashMap<String, Object>> gisCctvList = geoService.selectCctvInfoList();	// 각 사이트별 카메라 리스트
			if (gisCctvList == null || gisCctvList.isEmpty()) {
				logger.error("### gis에 등록된 카메라 정보가 없습니다!!!! ###");
				return "gis data empty";
			} else {
				pveService.dumpCmFacilityGis(gisCctvList);	// gis 카메라 정보를 영상반출 db에 받은 그대로 담아둔다.
			}
			gisCctvCnt = gisCctvList.size();			
			
			
			
			List<HashMap<String, Object>> pveCctvList = pveService.selectCctvList();	// cm_facility 카메라 리스트
			if (pveCctvList == null || pveCctvList.isEmpty()) {
				logger.error("### pve에 등록된 카메라 정보가 없습니다!!! ###");
				return "cctv list empty";
			}
			pveCctvCnt = pveCctvList.size();

			

			List<HashMap<String, Object>> fcltCtvUpdateList  	= new ArrayList<HashMap<String, Object>>();		// 좌표&용도 수정대상
			List<HashMap<String, Object>> fcltCtvAgUpdateList  	= new ArrayList<HashMap<String, Object>>();		// 방향각 수정대상

			//gisCctvCnt = 0;		// 테스트용, 이후 작업을 진행하지 않는다. ##################################################
			
			for (int k = 0; k < gisCctvCnt; k++) {
				logger.info("==== facility ( {} / {} )",k,gisCctvCnt);
				
				gisFcltUid		= CommUtil.objNullToVal(gisCctvList.get(k).get("fcltUid"),"");		// VMS시설물고유식별번호
				gisFcltLblNm	= CommUtil.objNullToVal(gisCctvList.get(k).get("fcltLblNm"),"");		// 시설물명
			//	gisFcltUsedTyCd	= CommUtil.objNullToVal(gisCctvList.get(k).get("fcltUsedTyCd"),"");	// 용도코드	//  gis에서 용도코드를 가져오지 않는다.
				gisFcltUsedTyNm	= CommUtil.objNullToVal(gisCctvList.get(k).get("fcltUsedTyNm"),"");	// 용도명
				gisPointX		= CommUtil.objNullToVal(gisCctvList.get(k).get("pointX"),"0");		// 경도좌표
				gisPointY		= CommUtil.objNullToVal(gisCctvList.get(k).get("pointY"),"0");		// 위도좌표
				gisFcltKndDtlCd	= CommUtil.objNullToVal(gisCctvList.get(k).get("fcltKndDtlCd"),"");	// 고정회전
				gisLotnoAdresNm	= CommUtil.objNullToVal(gisCctvList.get(k).get("lotnoAdresNm"),"");	// 지번주소
				gisRoadAdresNm	= CommUtil.objNullToVal(gisCctvList.get(k).get("readAdresNm"),"");	// 도로명주소
				gisAngle		= CommUtil.objNullToVal(gisCctvList.get(k).get("angle"),"0");		// 방향각

				if (!CommUtil.isNum(gisPointX)){
					logger.error("### gisPointX error!!! >>>> {},{} ", gisFcltUid, gisPointX);
					err_cnt = err_cnt + 1;												continue;
				}
				if (!CommUtil.isNum(gisPointY)){
					logger.error("### gisPointY error!!! >>>> {},{} ", gisFcltUid, gisPointY);
					err_cnt = err_cnt + 1;												continue;
				}
				if (!CommUtil.isNum(gisAngle)){
					logger.error("### gisAngle error!!! >>>> {},{} ", gisFcltUid, gisAngle);
					gisAngle = "0";
				}

				// 용도체크
				if ( "".equalsIgnoreCase(strFcltUsedTyCd)) {
					gisFcltUsedTyCd = "C99";		// 미분류
					
					if ( !"".contentEquals(gisFcltUsedTyNm)) {		// 용도명이 있을 때
						for (int i = 0; i < arrFcltUsedTyCd.length; i++) {
							if (gisFcltUsedTyNm.equalsIgnoreCase(arrFcltUsedTyNm[i])) {
								gisFcltUsedTyCd = arrFcltUsedTyCd[i];
								break;
							}
						}
					}
				}
				logger.info("==== facility ( {} / {} ) {}, {}, {}, {}", k, gisCctvCnt, gisFcltUid, gisFcltLblNm, gisFcltUsedTyNm, gisFcltUsedTyCd);
				
				
				
//				FCLT_UID || FCLT_USED_TY_CD || POINT_X || POINT_Y as COMPARE_ITEMS
				compareItemsGis = gisFcltUid + gisFcltUsedTyCd + gisPointX + gisPointY;
				
				String updYn = "N";
				String updAgYn = "N";
				String fcltId = "";
				Double dAg = Double.valueOf(gisAngle);
				int cctvOsvtAg = (int) Math.round(dAg);
				if (cctvOsvtAg < 0 || cctvOsvtAg > 360) {
					cctvOsvtAg = 0;
				}
				
				// 동일 고유번호 체크
				for (int i = 0; i < pveCctvCnt; i++) {
					if (gisFcltUid.equalsIgnoreCase((pveCctvList.get(i).get("fcltUid").toString()))) {
						logger.info("### equals {}/{} => {} == {}", i, pveCctvCnt, gisFcltUid, pveCctvList.get(i).get("fcltUid").toString());
						
						fcltId = pveCctvList.get(i).get("fcltId").toString();
						gisFcltKndDtlCd = pveCctvList.get(i).get("fcltKndDtlCd").toString();	/* 고정회전 */
						// 위치,용도 체크

						logger.info("### compar {}/{} => {} == {}", i, pveCctvCnt, compareItemsGis, pveCctvList.get(i).get("compareItems").toString());
						if (!compareItemsGis.equals(pveCctvList.get(i).get("compareItems").toString())) {
							updYn = "Y";
						}
						// 방향각 체크
						// 시설물종류세부코드	FCLT_KND_DTL_CD = 'FT' 고정형이고 방향각이 0보다 크면 방향각 산출 업데이트
//						dAg = Double.valueOf(angle);
//						if (dAg>0 && dAg<360 && "FT".equals(fcltKndDtlCd)) {
						// 고정형과 관계없이 값이 있으면 방향각 처리
						updAgYn = "Y";
//						if ("FT".equals(fcltKndDtlCd)) {
//							updAgYn = "Y";
//						}
						break;
					}
				}
				
				// 위치 용도
				if ("Y".equals(updYn)) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("fcltId", fcltId);
					//map.put("fcltUid", fcltUid);
					map.put("pointX", gisPointX);
					map.put("pointY", gisPointY);
					map.put("fcltUsedTyCd", gisFcltUsedTyCd);
					map.put("fcltKndDtlCd", gisFcltKndDtlCd);
					map.put("lotnoAdresNm", gisLotnoAdresNm);
					map.put("roadAdresNm" , gisRoadAdresNm);

					upd_cnt1 = upd_cnt1 + 1;
					fcltCtvUpdateList.add(map);
				}

				if ("Y".equals(updAgYn)) {
					HashMap<String, Object> mapAg = new HashMap<String, Object>();
					mapAg.put("fcltId", fcltId);
					double dPointX=Double.valueOf(gisPointX);
					double dPointY=Double.valueOf(gisPointY);

					//double cctvOsvtX=Double.valueOf(gisPointX);
					//double cctvOsvtY=Double.valueOf(gisPointY);
					if (cctvOsvtAg > 0) {
						// 방향각 좌표 계산
						double[] newCoordinates = GeoUtils.calculateNewCoordinates(dPointY, dPointX, pointDistance, dAg);
						logger.debug("### 각도좌표: (" + newCoordinates[0] + ", " + newCoordinates[1] + ")");

						mapAg.put("cctvOsvtX", newCoordinates[1]);
						mapAg.put("cctvOsvtY", newCoordinates[0]);
						mapAg.put("cctvOsvtAg", cctvOsvtAg);
					} else {
						mapAg.put("cctvOsvtX", 0);
						mapAg.put("cctvOsvtY", 0);
						mapAg.put("cctvOsvtAg", 0);
					}

					upd_cnt31 = upd_cnt31 + 1;
					fcltCtvAgUpdateList.add(mapAg);
				}
			}
			
			String updYn = "";
			String agUpdYn = "";
			if ("0".equals(updateTy)) {
				updYn = "N";
				agUpdYn = "N";
			} else if ("1".equals(updateTy)) {
				updYn = "Y";
			} else if ("2".equals(updateTy)) {
				agUpdYn = "Y";
			} else if ("3".equals(updateTy)) {
				updYn = "Y";
				agUpdYn = "Y";
			}
			
			upd_cnt2 = pveService.updatePointXY(fcltCtvUpdateList, updYn);			// 위치&용도 수정하기
			
			upd_cnt32 = pveService.updatePointAgXY(fcltCtvAgUpdateList, agUpdYn);	// 방향각 수정하기

		} catch (Exception e) {
			logger.error("### Exception >>>> {} ###",e.getMessage());
			e.printStackTrace();
			rtn = "fail";
		}
		logger.info("============================================");
		logger.info("==== gis cctv count >>>> {}", gisCctvCnt);
		logger.info("==== error count    >>>> {}", err_cnt);
		logger.info("==== update 위치/용도  >>>> 합계:{}, 업데이트:{}", upd_cnt1,upd_cnt2);
		logger.info("==== update 방향각      >>>> 합계:{}, 업데이트:{}", upd_cnt31,upd_cnt32);
		logger.info("============================================");
		
		return rtn;
	}
}
