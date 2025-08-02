/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : GeoJsonUtility.java
 * @Description : GeoJson 관련 유틸리티
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 3. 17. SaintJuny@ubolt.co.kr 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */

package kr.co.ucp.mntr.gis.util;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import kr.co.ucp.cmm.util.EgovStringUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public class GeoJsonUtility {

	@Resource(name="gisUtil")
	private GisUtil gisUtil;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> createGeoJson(List<EgovMap> list, String lon, String lat) {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

		resultMap.put("type", "FeatureCollection");

		List features = new LinkedList();

		for(EgovMap map : list) {
			Point2D.Double tp = new Point2D.Double();

			String strLon = EgovStringUtil.nullConvert(map.get(lon));
			String strLat = EgovStringUtil.nullConvert(map.get(lat));

			if (!"".equals(strLon) && !"".equals(strLat)) {

				tp = gisUtil.convertByWgs84(strLon, strLat);

				List coordinates = new LinkedList();
				coordinates.add(tp.x);
				coordinates.add(tp.y);

				Map geometry = new HashMap();
				geometry.put("type", "Point");
				geometry.put("coordinates", coordinates);

				Map feature = new HashMap();
				feature.put("type", "Feature");
				feature.put("geometry", geometry);
				feature.put("properties", map);
				features.add(feature);
			}
			else {
				continue;
			}
		}

		resultMap.put("features", features);

		return resultMap;
	}
}