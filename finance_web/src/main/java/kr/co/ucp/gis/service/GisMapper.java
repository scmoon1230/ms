package kr.co.ucp.gis.service;

import java.util.HashMap;
import java.util.List;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("gisMapper")
public interface GisMapper {

	String getGisProjection(String gisEngine);
	
	List<HashMap<String, Object>> selectFcltPointList(HashMap<String, Object> params);

	void updateFcltPointProjection(HashMap<String, Object> map);
	
}


