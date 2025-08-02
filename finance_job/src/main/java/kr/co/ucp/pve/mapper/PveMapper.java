package kr.co.ucp.pve.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PveMapper {

	
	HashMap<String, String> selectCmPrprts(HashMap<String, String> params);
	List<HashMap> selectPrprtsList(String prprtsId);
	List<HashMap> selectPrprtsListStartWith(String prprtsKey);

	List<HashMap<String, String>> selectOutFileList(HashMap<String, String> map);
	List<HashMap<String, String>> selectOutFileWaitingList(HashMap<String, Object> map);
	HashMap<String, Object> selectOutFileSeq(HashMap<String, Object> map);
	HashMap<String, Object> selectOutFileSeqMax(HashMap<String, Object> map);
	int mergeOutFile(Map<String, String> params) throws Exception;
	int updateOutFile(Map<String, String> params) throws Exception;

	HashMap<String, String> selectOutRqstDtl(HashMap<String, String> map);
	int updateOutRqst(Map<String, String> params) throws Exception;

	
	
	HashMap<String, String> selectUserInfo(HashMap<String, String> params) throws Exception;

	
	
	

	List<HashMap<String, Object>> selectFcltUsedTyInfo(Map<String, Object> map);
	int insertCmTcFcltUsed(HashMap<String, Object> map);
	int deleteCmTcFcltUsed(HashMap<String, Object> map);

	List<HashMap<String, Object>> selectCmFacility();
	int insertCmFacility(HashMap<String, Object> map);
	int deleteCmFacility(HashMap<String, Object> map);

	int insertCmFacilityGis(HashMap<String, Object> map);
	int deleteCmFacilityGis(HashMap<String, Object> map);

	int updatePointXY(Map<String, Object> map);
	int updatePointAgXY(HashMap<String, Object> map);
	
}
