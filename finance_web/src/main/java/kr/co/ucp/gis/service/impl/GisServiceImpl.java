package kr.co.ucp.gis.service.impl;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmmn.CommUtil;
import kr.co.ucp.cmmn.EgovStringUtil;
import kr.co.ucp.gis.service.GisMapper;
import kr.co.ucp.gis.service.GisService;
import kr.co.ucp.mntr.gis.service.GeoDataMapper;
import kr.co.ucp.mntr.gis.service.GeoDataService;
import kr.co.ucp.mntr.gis.util.GisUtil;

@Service("gisService")
public class GisServiceImpl implements GisService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

//    @Resource(name = "geoDataMapper")
//    private GeoDataMapper geoDataMapper;

    @Resource(name = "gisMapper")
    private GisMapper gisMapper;

    @Resource(name = "gisUtil")
    private GisUtil gisUtil;

    public int updatePointProjection(String dstrtCd) throws Exception {
		logger.debug("==== updatePointProjection >>>>");
		int updCnt = 0;
		try {
			String gisEngine = prprtsService.getString("GIS_ENGINE");
			
	    	String projection = gisMapper.getGisProjection(gisEngine);
	        //String projection = geoDataMapper.getGisProjection(gisEngine);

	        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	        HashMap<String, Object> params = new HashMap<>();
            params.put("dstrtCd", dstrtCd);
			list = gisMapper.selectFcltPointList(params);

			if (list == null || list.isEmpty()) {
				logger.debug("==== 업데이트할 내용이 없습니다. >>>>");
				return 0;
			}
	        for (HashMap<String, Object> map : list) {
	            Point2D.Double tp = new Point2D.Double();

	            String strLon = CommUtil.nullConvert(map.get("pointX"));
	            String strLat = CommUtil.nullConvert(map.get("pointY"));
	    		try {
		            if (!"".equals(strLon) && !"".equals(strLat)) {
		                tp = gisUtil.convertByWgs84(strLon, strLat, projection);
		                map.put("pointXPrjctn",tp.x);
		                map.put("pointYPrjctn",tp.y);
		                gisMapper.updateFcltPointProjection(map);
		                updCnt++;
		            }
	    		} catch (Exception e) {
	    			logger.error("==== Exception >>>> {}", EgovStringUtil.nullConvert(map.get("fcltId")));
	    		}
			}
		} catch (Exception e) {
			logger.error("==== Exception >>>> {}", e.getMessage());
		}
		logger.debug("==== 좌표변환 업데이트 건수 >>>> {}", updCnt);
        return updCnt;
    }

}
