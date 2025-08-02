package kr.co.ucp.geo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmns.ConfigManager;
import kr.co.ucp.geo.mapper.GeoMapper;

@Service("geoService")
public class GeoService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "geoMapper")
    private GeoMapper geoMapper;

	public List<HashMap<String, Object>> selectCctvInfoList()  throws Exception {

		//String dstrtCd = ConfigManager.getProperty("DSTRT_CD").trim();
		//logger.info("## selectCctvInfoList(), dstrtCd : {}",dstrtCd);
		
		List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
		
		listMap = geoMapper.selectCctvInfoList();

		return listMap;
	}

}
