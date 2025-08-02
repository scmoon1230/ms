package kr.co.ucp.pve.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PveService {


	HashMap<String, String> selectCmPrprts(HashMap<String, String> params) throws Exception;

	// 암호화대기 상태의 파일을 DB에서 조회하여 암호화를 요청한다.
	void requestMaEncVdo(HashMap<String, String> params) throws Exception;
	
	// 마크애니 영상입수 결과파일 result.xml 을 읽어서 DB에 등록하고 결과파일은 이동한다.
	void workMaOrgVdoResult(HashMap<String, String> params) throws Exception;
	
	
	
	
	int dumpCmTcFcltUsed(List<HashMap<String, Object>> listMap) throws Exception;

	int dumpCmFacility(List<HashMap<String, Object>> listMap) throws Exception;

	
	
	
	
	
	
	List<HashMap<String, Object>> selectCctvList() throws Exception;

	int updatePointXY(List<HashMap<String, Object>> listMap, String updateYn) throws Exception;

	int updatePointAgXY(List<HashMap<String, Object>> listMap, String updateYn) throws Exception;

	List<HashMap<String, Object>> selectFcltUsedTyInfo(Map<String, Object> map) throws Exception;
	
	int dumpCmFacilityGis(List<HashMap<String, Object>> listMap) throws Exception;

}
