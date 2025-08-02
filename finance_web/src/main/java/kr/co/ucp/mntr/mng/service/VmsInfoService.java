package kr.co.ucp.mntr.mng.service;

import java.util.List;
import java.util.Map;

public interface VmsInfoService {
	List<Map<String, Object>> selectVmsList(Map<String, Object> params) throws Exception;

	List<Map<String, String>> vmsGrpList(Map<String, Object> params) throws Exception;

	List<Map<String, Object>> dstrtNmCd(Map<String, Object> params) throws Exception;

	int insertVmsInfo(Map<String, Object> map) throws Exception;

	int updateVmsInfo(Map<String, Object> map) throws Exception;

	int deleteVmsInfo(Map<String, String> map) throws Exception;
}
