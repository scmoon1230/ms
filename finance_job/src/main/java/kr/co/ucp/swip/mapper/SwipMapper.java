package kr.co.ucp.swip.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SwipMapper {

	List<HashMap<String, Object>> selectCmTcFcltUsed(Map<String, Object> map);

	List<HashMap<String, Object>> selectCmFacility();
	
}
