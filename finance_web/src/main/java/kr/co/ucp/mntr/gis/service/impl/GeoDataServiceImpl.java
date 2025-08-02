/**
 * ----------------------------------------------------------------------------------------------
 *
 * @Class Name : GeoDataServiceImpl.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 3. 17. SaintJuny@ubolt.co.kr 최초작성
 * <p>
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.gis.service.impl;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import egovframework.rte.fdl.string.EgovNumericUtil;
import kr.co.ucp.mntr.cmm.service.ConfigureVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.FcltVO;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.mntr.cmm.util.StringUtils;
import kr.co.ucp.mntr.gis.service.GeoDataMapper;
import kr.co.ucp.mntr.gis.service.GeoDataService;
import kr.co.ucp.mntr.gis.util.GisUtil;

@Service("geoDataService")
public class GeoDataServiceImpl implements GeoDataService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

    @Resource(name = "geoDataMapper")
    private GeoDataMapper geoDataMapper;

    @Resource(name = "gisUtil")
    private GisUtil gisUtil;

    @Override
    public List<EgovMap> selectFcltGeoData(FcltVO vo) throws Exception {
        String dstrtCd = prprtsService.getString("DSTRT_CD");
        vo.setDstrtCd(dstrtCd);
        
        LoginVO loginVO = SessionUtil.getUserInfo();
        vo.setUserId(loginVO.getUserId());
        vo.setGrpId(loginVO.getGrpId());
        vo.setAuthLvl(loginVO.getAuthLvl());


        String searchFcltId = EgovStringUtil.nullConvert(vo.getSearchFcltId());
        if (!"".equals(searchFcltId)) {
            vo.setSearchFcltId(StringUtils.avoidRegexp(searchFcltId));
        }

        String vmsLinkYn = EgovStringUtil.nullConvert(prprtsService.getString("VMS_LINK_YN"));
        if ("Y".equals(vmsLinkYn)) {
            vo.setVmsLinkYn(vmsLinkYn);
        }

        Point2D.Double tp1 = new Point2D.Double();

        String strLon = EgovStringUtil.nullConvert(vo.getLon());
        String strLat = EgovStringUtil.nullConvert(vo.getLat());

        if (!"".equals(strLon) && !"".equals(strLat)) {

            tp1 = gisUtil.convertToWgs84(strLon, strLat);

            vo.setRadius(vo.getRadius() / 1000);
            vo.setLon(String.valueOf(tp1.x));
            vo.setLat(String.valueOf(tp1.y));
        }

        String bBoxStr = EgovStringUtil.nullConvert(vo.getBbox());
        String[] bBox = bBoxStr.split(",");

        if (!"".equals(bBoxStr) && bBox.length == 4) {
            Point2D.Double tp = new Point2D.Double();
            Point2D.Double tp2 = new Point2D.Double();

            tp = gisUtil.convertToWgs84(bBox[0], bBox[1]);
            tp2 = gisUtil.convertToWgs84(bBox[2], bBox[3]);

            vo.setMinX(String.valueOf(tp.x));
            vo.setMaxX(String.valueOf(tp2.x));
            vo.setMinY(String.valueOf(tp.y));
            vo.setMaxY(String.valueOf(tp2.y));
            logger.debug("CONVERT BBOX = {}, {}, {}, {}", String.valueOf(tp.x), String.valueOf(tp2.x), String.valueOf(tp.y), String.valueOf(tp2.y));
        } else {
            return new ArrayList<EgovMap>();
        }

        Object objConfigure = SessionUtil.getAttribute("configure");
        if (objConfigure != null && objConfigure.getClass().isAssignableFrom(ConfigureVO.class)) {
            ConfigureVO configureVO = (ConfigureVO) objConfigure;
            vo.setNetworkIp(configureVO.getNetworkIp());
        } else if (objConfigure != null && objConfigure.getClass() == EgovMap.class) {
    		EgovMap configureVO = (EgovMap) objConfigure;
            vo.setNetworkIp(configureVO.get("networkIp").toString());
        }
        logger.info("--> userId : {} , networkIp : {}", vo.getUserId(), vo.getNetworkIp());
		
        return geoDataMapper.selectFcltGeoData(vo);
    }

    @Override
    public EgovMap selectFcltById(FcltVO vo) throws Exception {
        Object objConfigure = SessionUtil.getAttribute("configure");
        if (objConfigure != null && objConfigure.getClass().isAssignableFrom(ConfigureVO.class)) {
            ConfigureVO configureVO = (ConfigureVO) objConfigure;
            vo.setNetworkIp(configureVO.getNetworkIp());
        } else if (objConfigure != null && objConfigure.getClass() == EgovMap.class) {
    		EgovMap configureVO = (EgovMap) objConfigure;
            vo.setNetworkIp(configureVO.get("networkIp").toString());
        }
		
		EgovMap map = geoDataMapper.selectFcltById(vo);

        LoginVO loginVO = SessionUtil.getUserInfo();
		logger.info("--> selectFcltById(), UserId       : {}", loginVO.getUserId());
		logger.info("--> selectFcltById(), networkIp    : {}", vo.getNetworkIp());
		logger.info("--> selectFcltById(), vrsWebrtcAddr: {}", map.get("vrsWebrtcAddr").toString());
		
        return map;
    }

    @Override
    public EgovMap selectFcltByUid(Map<String, String> params) throws Exception {
        String vmsLinkYn = EgovStringUtil.nullConvert(prprtsService.getString("VMS_LINK_YN"));
        if ("Y".equals(vmsLinkYn)) {
            params.put("vmsLinkYn", vmsLinkYn);
        }
        return geoDataMapper.selectFcltByUid(params);
    }

    @Override
    public EgovMap selectNearestCctv(FcltVO vo) throws Exception {
        LoginVO loginVO = SessionUtil.getUserInfo();
        vo.setUserId(loginVO.getUserId());
        vo.setGrpId(loginVO.getGrpId());
        vo.setAuthLvl(loginVO.getAuthLvl());

        String vmsLinkYn = EgovStringUtil.nullConvert(prprtsService.getString("VMS_LINK_YN"));
        if ("Y".equals(vmsLinkYn)) {
            vo.setVmsLinkYn(vmsLinkYn);
        }
        return geoDataMapper.selectNearestCctv(vo);
    }

    @Override
    public int selectNearestCctvListTotCnt(FcltVO vo) throws Exception {
        LoginVO loginVO = SessionUtil.getUserInfo();
        vo.setUserId(loginVO.getUserId());
        vo.setGrpId(loginVO.getGrpId());
        vo.setAuthLvl(loginVO.getAuthLvl());
        return geoDataMapper.selectNearestCctvListTotCnt(vo);
    }

    @Override
    public List<EgovMap> selectNearestCctvList(FcltVO vo) throws Exception {
        LoginVO loginVO = SessionUtil.getUserInfo();
        vo.setUserId(loginVO.getUserId());
        vo.setGrpId(loginVO.getGrpId());
        vo.setAuthLvl(loginVO.getAuthLvl());

        String vmsLinkYn = EgovStringUtil.nullConvert(prprtsService.getString("VMS_LINK_YN"));
        if ("Y".equals(vmsLinkYn)) {
            vo.setVmsLinkYn(vmsLinkYn);
        }
        return geoDataMapper.selectNearestCctvList(vo);
    }

    @Override
    public List<EgovMap> selectFcltByMngSn(FcltVO vo) throws Exception {
        String vmsLinkYn = EgovStringUtil.nullConvert(prprtsService.getString("VMS_LINK_YN"));
        if ("Y".equals(vmsLinkYn)) {
            vo.setVmsLinkYn(vmsLinkYn);
        }
        return geoDataMapper.selectFcltByMngSn(vo);
    }

    @Override
    public Map<String, Object> selectCastNetCctvList(FcltVO vo) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        LoginVO loginVO = SessionUtil.getUserInfo();
        vo.setUserId(loginVO.getUserId());
        vo.setGrpId(loginVO.getGrpId());
        vo.setAuthLvl(loginVO.getAuthLvl());

        String boundsLeft = EgovStringUtil.nullConvert(prprtsService.getString("GIS_EXTENT_LEFT"));
        String boundsBottom = EgovStringUtil.nullConvert(prprtsService.getString("GIS_EXTENT_BOTTOM"));
        String boundsRight = EgovStringUtil.nullConvert(prprtsService.getString("GIS_EXTENT_RIGHT"));
        String boundsTop = EgovStringUtil.nullConvert(prprtsService.getString("GIS_EXTENT_TOP"));

        //vo.setUcpId(ucpId);
        vo.setBoundsLeft(boundsLeft);
        vo.setBoundsBottom(boundsBottom);
        vo.setBoundsRight(boundsRight);
        vo.setBoundsTop(boundsTop);

        // 투망감시 범위 설정 cctvViewRads 범의 내의 CCTV 조회
        Object objConfigure = SessionUtil.getAttribute("configure");
        if (objConfigure != null && objConfigure.getClass().isAssignableFrom(ConfigureVO.class)) {
            ConfigureVO configureVO = (ConfigureVO) objConfigure;
            vo.setNetworkIp(configureVO.getNetworkIp());
            
            String cctvViewRads = configureVO.getCctvViewRads();
            if (EgovNumericUtil.isNumber(cctvViewRads)) {
                double km = Double.parseDouble(cctvViewRads) / 1000;
                String[] bbox = getBboxByKm(vo.getPointX(), vo.getPointY(), km);
                vo.setRadius(km);
                vo.setMinX(bbox[0]);
                vo.setMaxX(bbox[1]);
                vo.setMinY(bbox[2]);
                vo.setMaxY(bbox[3]);
                logger.debug("--> radius is number : {}", vo.getRadius());
            } else {
                logger.debug("--> radius is not number");
            }
        } else if (objConfigure != null && objConfigure.getClass() == EgovMap.class) {
    		EgovMap configureVO = (EgovMap) objConfigure;
            vo.setNetworkIp(configureVO.get("networkIp").toString());
            
            String cctvViewRads = configureVO.get("cctvViewRads").toString();
            if (EgovNumericUtil.isNumber(cctvViewRads)) {
                double km = Double.parseDouble(cctvViewRads) / 1000;
                String[] bbox = getBboxByKm(vo.getPointX(), vo.getPointY(), km);
                vo.setRadius(km);
                vo.setMinX(bbox[0]);
                vo.setMaxX(bbox[1]);
                vo.setMinY(bbox[2]);
                vo.setMaxY(bbox[3]);
                logger.debug("--> radius is number : {}", vo.getRadius());
            } else {
                logger.debug("--> radius is not number");
            }
            
            
            
        }


        String vmsLinkYn = EgovStringUtil.nullConvert(prprtsService.getString("VMS_LINK_YN"));
        if ("Y".equals(vmsLinkYn)) {
            vo.setVmsLinkYn(vmsLinkYn);
        }

        List<EgovMap> presetList = geoDataMapper.selectNearestPresetForCastNet(vo);
        List<EgovMap> cctvList = geoDataMapper.selectNearestCctvForCastNet(vo);

        String fcltId = EgovStringUtil.nullConvert(vo.getFcltId());
        if (!"".equals(fcltId)) {
            // 0번에 발생한 카메라가 참여.
            EgovMap fcltOcr = selectFcltById(vo);

            String fcltKndCd = EgovStringUtil.nullConvert(fcltOcr.get("fcltKndCd"));
            if ("CTV".equals(fcltKndCd)) {
                // 발생 카메라 상태가 비정상이라면...
                String fcltSttus = EgovStringUtil.nullConvert(fcltOcr.get("fcltSttus"));
                String viewerTyCd = EgovStringUtil.nullConvert(fcltOcr.get("viewerTyCd"));
                if (!("0").equals(fcltSttus)) {
                    // 같은 개소에서 정상 카메라 중하나 선택 ( 주카메라 우선 )
                    List<EgovMap> fcltList = selectFcltByMngSn(vo);
                    EgovMap fcltTemp = null;
                    if (fcltList != null && fcltList.size() != 0) {
                        for (EgovMap fclt : fcltList) {
                            String fcltSttus2 = EgovStringUtil.nullConvert(fclt.get("fcltSttus"));
                            String fcltGdsdtTy = EgovStringUtil.nullConvert(fclt.get("fcltGdsdtTy"));
                            // 주카메라가 있다면 해당 카메라가 참여
                            if ("0".equals(fcltSttus2) && "0".equals(fcltGdsdtTy)) {
                                fcltTemp = fclt;
                                break;
                            }
                            // 보조 카메라라면 로직상 마지막 카메라가 참여
                            else if ("0".equals(fcltSttus2) && !"0".equals(fcltGdsdtTy)) {
                                fcltTemp = fclt;
                            }
                        }
                    }
                    // 모두 비정상이라면 발생 카메라가 참여 VIEWER_TY_CD = RTSP인경우 PASS
                    // 2017.03.13 SPACE
                    if (fcltTemp == null && !("RTSP".equals(viewerTyCd))) {
                        fcltOcr.put("presetNum", "0");
                        fcltOcr.put("distance", new BigDecimal(0));
                        for (int i = 0; i < presetList.size(); i++) {
                            String presetFcltId = EgovStringUtil.nullConvert(presetList.get(i).get("fcltId"));
                            if (presetFcltId.equals(fcltId)) {
                                presetList.remove(i);
                            }
                        }
                        presetList.add(0, fcltOcr);
                    }
                    // 개소 카메라가 중 정상인 카메라가 참여
                    else {
                        if (fcltTemp != null) {
                            fcltTemp.put("presetNum", "0");
                            fcltTemp.put("distance", new BigDecimal(0));
                            for (int i = 0; i < presetList.size(); i++) {
                                String presetFcltId = EgovStringUtil.nullConvert(presetList.get(i).get("fcltId"));
                                if (presetFcltId.equals(fcltId)) {
                                    presetList.remove(i);
                                }
                            }
                            presetList.add(0, fcltTemp);
                        }
                    }
                } else {
                    // 2017.03.13 space
                    // rtsp 인경우 투망감시에서 제외 처리
                    if (!"RTSP".equals(viewerTyCd)) {
                        fcltOcr.put("presetNum", "0");
                        fcltOcr.put("distance", new BigDecimal(0));
                        for (int i = 0; i < presetList.size(); i++) {
                            String presetFcltId = EgovStringUtil.nullConvert(presetList.get(i).get("fcltId"));
                            if (presetFcltId.equals(fcltId)) {
                                presetList.remove(i);
                            }
                        }
                        presetList.add(0, fcltOcr);
                    }
                }
            }
        }

        // 등록된 프리셋이 없다면, cctvList가 참여
        if (presetList == null || presetList.isEmpty()) {
            Map<String, Object> cctvMap = gisUtil.createGeoJson(cctvList, "pointX", "pointY");
            resultMap.put("cctv", cctvList);
            resultMap.put("geoJson", cctvMap);
        }
        // 등록된 프리셋이 있다면,
        else {
            // presetList와 중복된 카메라 제외.
            for (EgovMap preset : presetList) {
                String presetFcltId = EgovStringUtil.nullConvert(preset.get("fcltId"));
                for (int i = 0; i < cctvList.size(); i++) {
                    String cctvFcltId = EgovStringUtil.nullConvert(cctvList.get(i).get("fcltId"));
                    if (presetFcltId.equals(cctvFcltId)) {
                        cctvList.remove(i);
                    }
                }
            }
            // 그리고 합침.
            presetList.addAll(presetList.size(), cctvList);
            // 거리순으로 솔팅.
            Collections.sort(presetList, new DistanceAscCompare());
            // 5개만 가져와서 리스팅.
            int nSize = presetList.size();
            if (nSize < 5) {
                presetList = presetList.subList(0, nSize);
            } else {
                presetList = presetList.subList(0, 5);
            }

            Map<String, Object> presetMap = gisUtil.createGeoJson(presetList, "pointX", "pointY");
            vo.setPresetList(presetList);

            List<EgovMap> presets = geoDataMapper.selectNearestPresetListForCastNet(vo);
            // 선택된 프리셋 정보
            Map<String, Object> presetsMap = gisUtil.createGeoJson(presets, "pointX", "pointY");
            resultMap.put("cctv", presetList);
            resultMap.put("preset", presetsMap);
            resultMap.put("geoJson", presetMap);
        }
        return resultMap;
    }

    @Override
    public List<EgovMap> selectFcltAngleGeoData(FcltVO vo) throws Exception {
        String dstrtCd = prprtsService.getString("DSTRT_CD");
        LoginVO loginVO = SessionUtil.getUserInfo();
        vo.setUserId(loginVO.getUserId());
        vo.setGrpId(loginVO.getGrpId());
        vo.setAuthLvl(loginVO.getAuthLvl());
        vo.setDstrtCd(dstrtCd);

        String vmsLinkYn = EgovStringUtil.nullConvert(prprtsService.getString("VMS_LINK_YN"));
        if ("Y".equals(vmsLinkYn)) {
            vo.setVmsLinkYn(vmsLinkYn);
        }

        String bBoxStr = EgovStringUtil.nullConvert(vo.getBbox());
        String[] bBox = bBoxStr.split(",");

        if (!"".equals(bBoxStr) && bBox.length == 4) {

            Point2D.Double tp = gisUtil.convertToWgs84(bBox[0], bBox[1]);
            Point2D.Double tp2 = gisUtil.convertToWgs84(bBox[2], bBox[3]);

            vo.setMinX(String.valueOf(tp.x));
            vo.setMaxX(String.valueOf(tp2.x));
            vo.setMinY(String.valueOf(tp.y));
            vo.setMaxY(String.valueOf(tp2.y));
            List<EgovMap> list = geoDataMapper.selectFcltAngleGeoData(vo);
            return list;

        } else {
            return new ArrayList<EgovMap>();
        }
    }

    @Override
    public List<EgovMap> selectLcInfoGeoData(Map<String, String> params) throws Exception {
        String bBoxStr = EgovStringUtil.nullConvert(params.get("bbox"));
        String[] bBox = bBoxStr.split(",");

        if (!"".equals(bBoxStr) && bBox.length == 4) {

            Point2D.Double tp = gisUtil.convertToWgs84(bBox[0], bBox[1]);
            Point2D.Double tp2 = gisUtil.convertToWgs84(bBox[2], bBox[3]);

            params.put("minX", String.valueOf(tp.x));
            params.put("maxX", String.valueOf(tp2.x));
            params.put("minY", String.valueOf(tp.y));
            params.put("maxY", String.valueOf(tp2.y));
        } else {
            return new ArrayList<EgovMap>();
        }

        return geoDataMapper.selectLcInfoGeoData(params);
    }

    private String[] getBboxByKm(String lon, String lat, double km) {
        double R = 6371;  // earth radius in km
        double x = Double.parseDouble(lon);
        double y = Double.parseDouble(lat);
        double minX = x - Math.toDegrees(km / R / Math.cos(Math.toRadians(y)));
        double maxX = x + Math.toDegrees(km / R / Math.cos(Math.toRadians(y)));
        double minY = y - Math.toDegrees(km / R);
        double maxY = y + Math.toDegrees(km / R);
        String bbox[] = new String[]{String.valueOf(minX), String.valueOf(maxX), String.valueOf(minY), String.valueOf(maxY)};
        return bbox;
    }

    @Override
    public int updatePointProjection(String gisEngine) throws Exception {
        int updCnt = 0;
        String projection = geoDataMapper.getGisProjection(gisEngine);

        List<EgovMap> list = new ArrayList<EgovMap>();
        list = geoDataMapper.selectFcltPointList();
        for (EgovMap map : list) {
            Point2D.Double tp = new Point2D.Double();

            String strLon = EgovStringUtil.nullConvert(map.get("pointX"));
            String strLat = EgovStringUtil.nullConvert(map.get("pointY"));
            try {
                if (!"".equals(strLon) && !"".equals(strLat)) {
                    tp = gisUtil.convertByWgs84(strLon, strLat, projection);
                    map.put("pointXPrjctn", tp.x);
                    map.put("pointYPrjctn", tp.y);
                    geoDataMapper.updateFcltPointProjection(map);
                    updCnt++;
                }
            } catch (Exception e) {
                logger.error("--> Exception >>>> {}", EgovStringUtil.nullConvert(map.get("fcltId")));
            }
        }
        logger.debug("--> 좌표변환 업데이트 건수 >>>> {}", updCnt);
        return updCnt;
    }

}

/**
 * No 오름차순
 *
 * @author falbb
 */
class DistanceAscCompare implements Comparator<EgovMap> {

    /**
     * 오름차순(ASC)
     */
    @Override
    public int compare(EgovMap arg0, EgovMap arg1) {
        double d0 = 9999.0;
        double d1 = 9999.0;

        String s0 = "";
        String s1 = "";
        try {
            s0 = EgovStringUtil.nullConvert(arg0.get("distance"));
            s1 = EgovStringUtil.nullConvert(arg1.get("distance"));
            if (NumberUtils.isCreatable(s0)) d0 = Double.parseDouble(s0);
            if (NumberUtils.isCreatable(s1)) d1 = Double.parseDouble(s1);
        } catch (ClassCastException cce) {
            if (arg0.get("distance").getClass() == Double.class) d0 = (Double) arg0.get("distance");
            if (arg1.get("distance").getClass() == Double.class) d1 = (Double) arg1.get("distance");
        }
        return d0 < d1 ? -1 : d0 > d1 ? 1 : 0;
    }
}
