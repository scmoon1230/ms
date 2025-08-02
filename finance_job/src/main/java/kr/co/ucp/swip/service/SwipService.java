package kr.co.ucp.swip.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmns.ConfigManager;
import kr.co.ucp.swip.mapper.SwipMapper;

@Service("swipService")
public class SwipService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "swipMapper")
    private SwipMapper swipMapper;

	public  List<HashMap<String, Object>> selectCmTcFcltUsed(Map<String, Object> map) throws Exception {
		return swipMapper.selectCmTcFcltUsed(map);
	}
    
	public List<HashMap<String, Object>> selectCmFacility()  throws Exception {
		List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
		listMap = swipMapper.selectCmFacility();
		return listMap;
	}

}
